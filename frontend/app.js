const getTrimmedValue = (input) => input.value.trim();

const coerceValue = (field, value) => {
  const type = field.dataset.type;
  if (type === "integer") {
    const parsed = Number.parseInt(value, 10);
    return Number.isNaN(parsed) ? value : parsed;
  }
  if (type === "decimal") {
    const parsed = Number.parseFloat(value);
    return Number.isNaN(parsed) ? value : parsed;
  }
  return value;
};

const buildPayload = (form) => {
  const payload = {};
  const fields = Array.from(form.querySelectorAll("[name]"));

  fields.forEach((field) => {
    if (field.dataset.path === "true") {
      return;
    }
    if (field.type === "checkbox") {
      payload[field.name] = field.checked;
      return;
    }
    const raw = getTrimmedValue(field);
    if (!raw) {
      return;
    }
    payload[field.name] = coerceValue(field, raw);
  });

  if (form.dataset.lineItems === "true") {
    const items = [];
    const rows = Array.from(form.querySelectorAll(".line-item-row"));

    rows.forEach((row) => {
      const description = getTrimmedValue(row.querySelector("[name='description']"));
      if (!description) {
        return;
      }
      const quantity = getTrimmedValue(row.querySelector("[name='quantity']"));
      const unitPrice = getTrimmedValue(row.querySelector("[name='unitPrice']"));
      const sortOrder = getTrimmedValue(row.querySelector("[name='sortOrder']"));

      items.push({
        description,
        quantity: quantity ? Number.parseInt(quantity, 10) : 1,
        unitPrice: unitPrice ? Number.parseFloat(unitPrice) : 0,
        sortOrder: sortOrder ? Number.parseInt(sortOrder, 10) : 0,
      });
    });

    if (items.length) {
      payload.lineItems = items;
    }
  }

  return payload;
};

const formatJson = (data) => {
  if (data === null || data === undefined) {
    return "";
  }
  return JSON.stringify(data, null, 2);
};

const createStatusHandler = (responseStatus, responseMeta) => {
  return (state, message) => {
    responseStatus.dataset.state = state;
    responseStatus.textContent = message;
    responseMeta.textContent = message;
  };
};

const resolveEndpoint = (form) => {
  let endpoint = form.dataset.endpoint || "";
  const missing = [];

  endpoint = endpoint.replace(/\{(\w+)\}/g, (match, key) => {
    const input = form.querySelector(`[name='${key}']`);
    const value = input ? getTrimmedValue(input) : "";
    if (!value) {
      missing.push(key);
      return match;
    }
    return encodeURIComponent(value);
  });

  if (missing.length) {
    throw new Error(`Missing required path field(s): ${missing.join(", ")}`);
  }

  return endpoint;
};

const WORKFLOW_PAGE_SIZE = 8;

let workflowModulesPromise = null;

const loadWorkflowModules = () => {
  if (!workflowModulesPromise) {
    workflowModulesPromise = Promise.all([
      import("https://esm.sh/react"),
      import("https://esm.sh/react-dom/client"),
      import("./BookingWorkflowPanel.js"),
    ]).then(([reactModule, reactDomModule, panelModule]) => ({
      React: reactModule.default || reactModule,
      createRoot: reactDomModule.createRoot,
      BookingWorkflowPanel: panelModule.default || panelModule,
    }));
  }
  return workflowModulesPromise;
};

document.addEventListener("DOMContentLoaded", () => {
  const baseUrlInput = document.getElementById("baseUrl");
  const tenantIdInput = document.getElementById("tenantId");
  const responseBody = document.getElementById("responseBody");
  const responseMeta = document.getElementById("responseMeta");
  const requestMeta = document.getElementById("requestMeta");
  const requestBody = document.getElementById("requestBody");
  const responseStatus = document.getElementById("responseStatus");
  const clearResponse = document.getElementById("clearResponse");
  const flowTabs = Array.from(document.querySelectorAll("[data-flow-target]"));
  const flowPanels = Array.from(document.querySelectorAll(".flow-panel"));
  const activeBookingBanner = document.getElementById("activeBookingBanner");
  const activeBookingMeta = document.getElementById("activeBookingMeta");
  const workflowRoot = document.getElementById("workflowRoot");
  const workflowTableBody = document.getElementById("workflowTableBody");
  const workflowPrev = document.getElementById("workflowPrev");
  const workflowNext = document.getElementById("workflowNext");
  const workflowPageMeta = document.getElementById("workflowPageMeta");
  const workflowSelectionMeta = document.getElementById("workflowSelectionMeta");

  if (!baseUrlInput || !tenantIdInput) {
    return;
  }

  const setStatus = createStatusHandler(responseStatus, responseMeta);
  const forms = Array.from(document.querySelectorAll("form[data-endpoint]"));

  const activeBookingState = {
    selection: null,
  };

  const setActiveFlow = (flowId) => {
    flowTabs.forEach((tab) => {
      const isActive = tab.dataset.flowTarget === flowId;
      tab.setAttribute("aria-selected", isActive ? "true" : "false");
    });
    flowPanels.forEach((panel) => {
      const isActive = panel.dataset.flow === flowId;
      panel.classList.toggle("active", isActive);
      panel.setAttribute("aria-hidden", isActive ? "false" : "true");
    });
  };

  const resolveInitialFlow = () => {
    const hash = window.location.hash.replace("#", "");
    if (hash && flowPanels.some((panel) => panel.dataset.flow === hash)) {
      return hash;
    }
    const defaultPanel = flowPanels.find((panel) => panel.classList.contains("active"));
    return defaultPanel ? defaultPanel.dataset.flow : "";
  };

  const resetResponse = () => {
    responseBody.value = "No response yet.";
    requestBody.value = "No payload yet.";
    requestMeta.textContent = "Request details will appear here.";
    setStatus("idle", "Waiting for a request...");
  };

  const sendRequest = async (form) => {
    const baseUrl = getTrimmedValue(baseUrlInput).replace(/\/$/, "");
    const tenantId = getTrimmedValue(tenantIdInput);
    const method = (form.dataset.method || "POST").toUpperCase();
    const payload = buildPayload(form);
    const skipBookingRequirement = form.dataset.skipBooking === "true";
    const activateBookingOnSuccess = form.dataset.activateBooking === "true";

    if (!baseUrl) {
      setStatus("error", "Base URL is required.");
      return;
    }

    if (!tenantId) {
      setStatus("error", "Tenant ID is required.");
      return;
    }

    if (method !== "GET" && method !== "HEAD" && !activeBookingState.selection && !skipBookingRequirement) {
      setStatus("error", "Select an active booking in the Workflow table before making changes.");
      return;
    }

    let endpoint = "";
    try {
      endpoint = resolveEndpoint(form);
    } catch (error) {
      setStatus("error", error instanceof Error ? error.message : String(error));
      return;
    }

    const url = `${baseUrl}${endpoint}`;
    const payloadText = Object.keys(payload).length ? formatJson(payload) : "<empty payload>";
    const hasBody = !["GET", "HEAD"].includes(method);

    requestMeta.textContent = `${method} ${url}`;
    requestBody.value = payloadText;
    responseBody.value = "";
    setStatus("pending", "Sending request...");

    const submitButton = form.querySelector("button[type='submit']");
    if (submitButton) {
      submitButton.disabled = true;
    }

    try {
      const headers = {
        "X-Tenant-Id": tenantId,
      };
      if (hasBody) {
        headers["Content-Type"] = "application/json";
      }

      const response = await fetch(url, {
        method,
        headers,
        body: hasBody ? JSON.stringify(payload) : undefined,
      });

      const contentType = response.headers.get("content-type") || "";
      let bodyText = "";

      let parsedJson = null;
      if (contentType.includes("application/json")) {
        parsedJson = await response.json();
        bodyText = formatJson(parsedJson);
      } else {
        bodyText = await response.text();
      }

      const statusLabel = `Status ${response.status} ${response.statusText}`.trim();
      responseBody.value = bodyText || "<empty response>";
      responseMeta.textContent = statusLabel;
      responseStatus.dataset.state = response.ok ? "success" : "error";
      responseStatus.textContent = response.ok ? "Success" : "Failed";

      const bookingId = parsedJson ? parsedJson.bookingId ?? parsedJson.id : null;
      if (response.ok && activateBookingOnSuccess && bookingId) {
        activeBookingState.selection = {
          id: bookingId,
          label: parsedJson.label || `Booking ${bookingId}`,
          state: parsedJson.state || "INQUIRY",
        };
        updateActiveBookingMeta();
        updateWorkflowSelection(activeBookingState.selection);
        renderWorkflowPanel();
        workflowState.pageParam = 1;
        workflowState.pageLabel = 1;
        fetchWorkflowPage(1);
      }
    } catch (error) {
      responseBody.value = error instanceof Error ? error.message : String(error);
      responseStatus.dataset.state = "error";
      responseStatus.textContent = "Failed";
      responseMeta.textContent = "Request failed.";
    } finally {
      if (submitButton) {
        submitButton.disabled = false;
      }
      updateBookingRequiredForms();
    }
  };

  forms.forEach((form) => {
    form.addEventListener("submit", (event) => {
      event.preventDefault();
      sendRequest(form);
    });
  });

  flowTabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      const flowId = tab.dataset.flowTarget;
      if (!flowId) {
        return;
      }
      setActiveFlow(flowId);
      window.location.hash = flowId;
    });
  });

  const initialFlow = resolveInitialFlow();
  if (initialFlow) {
    setActiveFlow(initialFlow);
  }

  if (clearResponse) {
    clearResponse.addEventListener("click", resetResponse);
  }

  const updateActiveBookingMeta = () => {
    if (!activeBookingMeta || !activeBookingBanner) {
      return;
    }
    if (!activeBookingState.selection) {
      activeBookingMeta.textContent = "No booking selected.";
      activeBookingBanner.dataset.state = "empty";
      updateBookingRequiredForms();
      return;
    }

    const selection = activeBookingState.selection;
    activeBookingMeta.textContent = `Booking ${selection.id}: ${selection.label} (${selection.state})`;
    activeBookingBanner.dataset.state = "active";
    updateBookingRequiredForms();
  };

  const updateBookingRequiredForms = () => {
    forms.forEach((form) => {
      const method = (form.dataset.method || "POST").toUpperCase();
      if (form.dataset.skipBooking === "true") {
        return;
      }
      if (method === "GET" || method === "HEAD") {
        return;
      }
      const submitButtons = Array.from(form.querySelectorAll("button[type='submit']"));
      submitButtons.forEach((button) => {
        button.disabled = !activeBookingState.selection;
      });
    });
  };

  const updateWorkflowSelection = (selection) => {
    if (!workflowSelectionMeta) {
      return;
    }
    if (!selection) {
      workflowSelectionMeta.textContent = "No active booking selected.";
      return;
    }
    workflowSelectionMeta.textContent = `Active booking: ${selection.label} (${selection.state})`;
  };

  const setWorkflowTableMessage = (message) => {
    if (!workflowTableBody) {
      return;
    }
    workflowTableBody.innerHTML = "";
    const row = document.createElement("tr");
    const cell = document.createElement("td");
    cell.colSpan = 4;
    cell.className = "muted workflow-empty";
    cell.textContent = message;
    row.appendChild(cell);
    workflowTableBody.appendChild(row);
  };

  const renderWorkflowRows = (candidates, selectedId) => {
    if (!workflowTableBody) {
      return;
    }
    workflowTableBody.innerHTML = "";
    if (!candidates.length) {
      setWorkflowTableMessage("No workflow candidates returned.");
      return;
    }

    candidates.forEach((candidate) => {
      const row = document.createElement("tr");
      if (selectedId && String(candidate.id) === String(selectedId)) {
        row.classList.add("workflow-row-active");
      }

      const dateCell = document.createElement("td");
      dateCell.textContent = candidate.eventDate || "TBD";
      row.appendChild(dateCell);

      const labelCell = document.createElement("td");
      labelCell.textContent = candidate.label;
      row.appendChild(labelCell);

      const stateCell = document.createElement("td");
      stateCell.textContent = candidate.state;
      row.appendChild(stateCell);

      const actionCell = document.createElement("td");
      const button = document.createElement("button");
      button.type = "button";
      button.className = "button-secondary";
      button.textContent = "Open workflow";
      button.addEventListener("click", () => {
        activeBookingState.selection = candidate;
        updateActiveBookingMeta();
        updateWorkflowSelection(candidate);
        renderWorkflowPanel();
        renderWorkflowRows(candidates, candidate.id);
      });
      actionCell.appendChild(button);
      row.appendChild(actionCell);

      workflowTableBody.appendChild(row);
    });
  };

  const resolveWorkflowPagination = (data, fallbackPage) => {
    let pageParam = fallbackPage;
    let pageLabel = fallbackPage;
    let totalPages = null;
    let hasNext = false;
    let hasPrevious = false;

    if (data && typeof data === "object") {
      if (Number.isFinite(data.page)) {
        pageParam = data.page;
        pageLabel = data.page;
      }
      if (Number.isFinite(data.pageNumber)) {
        pageParam = data.pageNumber;
        pageLabel = data.pageNumber;
      }
      if (Number.isFinite(data.number)) {
        pageParam = data.number;
        pageLabel = data.number + 1;
      }
      if (Number.isFinite(data.totalPages)) {
        totalPages = data.totalPages;
      }
      if (typeof data.hasNext === "boolean") {
        hasNext = data.hasNext;
      } else if (typeof data.last === "boolean") {
        hasNext = !data.last;
      } else if (totalPages !== null) {
        hasNext = pageLabel < totalPages;
      }
      if (typeof data.hasPrevious === "boolean") {
        hasPrevious = data.hasPrevious;
      } else if (typeof data.first === "boolean") {
        hasPrevious = !data.first;
      } else if (totalPages !== null) {
        hasPrevious = pageLabel > 1;
      }
    }

    return { pageParam, pageLabel, totalPages, hasNext, hasPrevious };
  };

  const resolveWorkflowItems = (data) => {
    if (Array.isArray(data)) {
      return data;
    }
    if (!data || typeof data !== "object") {
      return [];
    }
    if (Array.isArray(data.items)) {
      return data.items;
    }
    if (Array.isArray(data.content)) {
      return data.content;
    }
    if (Array.isArray(data.results)) {
      return data.results;
    }
    return [];
  };

  const workflowState = {
    pageParam: 1,
    pageLabel: 1,
    totalPages: null,
    hasNext: false,
    hasPrevious: false,
    pending: false,
  };

  const updateWorkflowPagination = () => {
    if (!workflowPrev || !workflowNext || !workflowPageMeta) {
      return;
    }
    workflowPrev.disabled = !workflowState.hasPrevious || workflowState.pending;
    workflowNext.disabled = !workflowState.hasNext || workflowState.pending;
    const totalLabel =
      workflowState.totalPages && Number.isFinite(workflowState.totalPages)
        ? ` of ${workflowState.totalPages}`
        : "";
    workflowPageMeta.textContent = `Page ${workflowState.pageLabel}${totalLabel}`;
  };

  const fetchWorkflowPage = async (page) => {
    if (!workflowTableBody || !baseUrlInput || !tenantIdInput) {
      return;
    }

    const baseUrl = getTrimmedValue(baseUrlInput).replace(/\/$/, "");
    const tenantId = getTrimmedValue(tenantIdInput);

    if (!baseUrl || !tenantId) {
      setWorkflowTableMessage("Enter a base URL and tenant ID to load bookings.");
      return;
    }

    workflowState.pending = true;
    updateWorkflowPagination();
    setWorkflowTableMessage("Loading workflow candidates...");

    try {
      const url = new URL(`${baseUrl}/api/bookings/workflow-candidates`);
      url.searchParams.set("page", page);
      url.searchParams.set("size", WORKFLOW_PAGE_SIZE);

      const response = await fetch(url.toString(), {
        method: "GET",
        headers: {
          "X-Tenant-Id": tenantId,
        },
      });

      if (!response.ok) {
        setWorkflowTableMessage("Unable to load workflow candidates.");
        return;
      }

      const data = await response.json();
      const items = resolveWorkflowItems(data);
      const pagination = resolveWorkflowPagination(data, page);

      workflowState.pageParam = pagination.pageParam;
      workflowState.pageLabel = pagination.pageLabel;
      workflowState.totalPages = pagination.totalPages;
      workflowState.hasNext = pagination.hasNext;
      workflowState.hasPrevious = pagination.hasPrevious;

      renderWorkflowRows(items, activeBookingState.selection ? activeBookingState.selection.id : "");
      updateWorkflowPagination();
    } catch (error) {
      setWorkflowTableMessage("Unable to load workflow candidates.");
    } finally {
      workflowState.pending = false;
      updateWorkflowPagination();
    }
  };

  const selectWorkflowPage = (nextPage) => {
    if (workflowState.pending) {
      return;
    }
    workflowState.pageParam = nextPage;
    workflowState.pageLabel = nextPage;
    fetchWorkflowPage(nextPage);
  };

  let workflowRootInstance = null;

  const renderWorkflowPanel = async () => {
    if (!workflowRoot) {
      return;
    }
    if (!activeBookingState.selection || !activeBookingState.selection.id) {
      if (workflowRootInstance) {
        workflowRootInstance.unmount();
        workflowRootInstance = null;
      }
      workflowRoot.textContent = "";
      return;
    }

    const bookingId = String(activeBookingState.selection.id);
    const baseUrl = baseUrlInput ? getTrimmedValue(baseUrlInput).replace(/\/$/, "") : "";
    const tenantId = tenantIdInput ? getTrimmedValue(tenantIdInput) : "";

    try {
      const { React, createRoot, BookingWorkflowPanel } = await loadWorkflowModules();
      if (!workflowRootInstance) {
        workflowRootInstance = createRoot(workflowRoot);
      }
      workflowRootInstance.render(
        React.createElement(BookingWorkflowPanel, { bookingId, baseUrl, tenantId })
      );
    } catch (error) {
      if (workflowRootInstance) {
        workflowRootInstance.unmount();
        workflowRootInstance = null;
      }
      workflowRoot.textContent = "Workflow panel unavailable.";
    }
  };

  if (workflowPrev) {
    workflowPrev.addEventListener("click", () => {
      if (workflowState.hasPrevious) {
        selectWorkflowPage(workflowState.pageParam - 1);
      }
    });
  }

  if (workflowNext) {
    workflowNext.addEventListener("click", () => {
      if (workflowState.hasNext) {
        selectWorkflowPage(workflowState.pageParam + 1);
      }
    });
  }

  if (workflowTableBody) {
    fetchWorkflowPage(workflowState.pageParam);
  }

  updateActiveBookingMeta();
  resetResponse();
});
