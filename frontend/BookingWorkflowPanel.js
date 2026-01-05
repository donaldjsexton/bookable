import React, { useEffect, useState } from "https://esm.sh/react";

const EMPTY_WORKFLOW = {
  state: "",
  allowedActions: [],
  blockedActions: [],
  timeline: [],
};

const resolveApiUrl = (baseUrl, path) => {
  if (!baseUrl) {
    return path;
  }
  return `${baseUrl.replace(/\/$/, "")}${path}`;
};

const buildTenantHeaders = (tenantId) => {
  if (!tenantId) {
    return {};
  }
  return {
    "X-Tenant-Id": tenantId,
  };
};

const BookingWorkflowPanel = ({ bookingId, baseUrl, tenantId }) => {
  const [workflow, setWorkflow] = useState(EMPTY_WORKFLOW);

  const fetchWorkflow = () => {
    const url = resolveApiUrl(
      baseUrl,
      `/api/bookings/${encodeURIComponent(bookingId)}/workflow`
    );

    return fetch(url, {
      method: "GET",
      headers: buildTenantHeaders(tenantId),
    })
      .then((response) => response.json())
      .then((data) => setWorkflow(data))
      .catch(() => setWorkflow(EMPTY_WORKFLOW));
  };

  useEffect(() => {
    fetchWorkflow();
  }, [bookingId]);

  const triggerAction = (action) => {
    const url = resolveApiUrl(
      baseUrl,
      `/api/bookings/${encodeURIComponent(bookingId)}/actions`
    );
    const headers = buildTenantHeaders(tenantId);
    headers["Content-Type"] = "application/json";

    return fetch(url, {
      method: "POST",
      headers,
      body: JSON.stringify({ action: action.action }),
    })
      .then(() => fetchWorkflow())
      .catch(() => null);
  };

  return React.createElement(
    "section",
    { className: "booking-workflow-panel", "aria-label": "Booking workflow state" },
    React.createElement(
      "header",
      { className: "booking-workflow-panel__header" },
      React.createElement(
        "div",
        { className: "booking-workflow-panel__summary" },
        React.createElement(
          "h2",
          { className: "booking-workflow-panel__title" },
          "Booking Workflow"
        ),
        React.createElement(
          "div",
          { className: "booking-workflow-panel__state" },
          React.createElement(
            "span",
            { className: "booking-workflow-panel__label" },
            "Current state"
          ),
          React.createElement(
            "span",
            { className: "booking-workflow-panel__value" },
            workflow.state
          )
        )
      )
    ),
    React.createElement(
      "section",
      { className: "booking-workflow-panel__actions", "aria-label": "Workflow actions" },
      React.createElement(
        "div",
        { className: "booking-workflow-panel__actions-group" },
        React.createElement(
          "h3",
          { className: "booking-workflow-panel__section-title" },
          "Allowed Actions"
        ),
        React.createElement(
          "ul",
          { className: "booking-workflow-panel__list" },
          workflow.allowedActions.map((action) =>
            React.createElement(
              "li",
              { className: "booking-workflow-panel__item", key: action.action },
              React.createElement(
                "div",
                { className: "booking-workflow-panel__item-header" },
                React.createElement(
                  "div",
                  { className: "booking-workflow-panel__item-title" },
                  action.label
                ),
                React.createElement(
                  "button",
                  { type: "button", onClick: () => triggerAction(action) },
                  action.label
                )
              ),
              React.createElement(
                "div",
                { className: "booking-workflow-panel__item-detail" },
                action.description
              )
            )
          )
        )
      ),
      React.createElement(
        "div",
        { className: "booking-workflow-panel__actions-group" },
        React.createElement(
          "h3",
          { className: "booking-workflow-panel__section-title" },
          "Blocked Actions"
        ),
        React.createElement(
          "ul",
          { className: "booking-workflow-panel__list" },
          workflow.blockedActions.map((action) =>
            React.createElement(
              "li",
              { className: "booking-workflow-panel__item", key: action.action },
              React.createElement(
                "div",
                { className: "booking-workflow-panel__item-title" },
                action.action
              ),
              React.createElement(
                "div",
                { className: "booking-workflow-panel__item-detail" },
                action.reason
              )
            )
          )
        )
      )
    ),
    React.createElement(
      "section",
      { className: "booking-workflow-panel__timeline", "aria-label": "Workflow timeline" },
      React.createElement(
        "h3",
        { className: "booking-workflow-panel__section-title" },
        "Timeline"
      ),
      React.createElement(
        "ul",
        { className: "booking-workflow-panel__list" },
        workflow.timeline.map((entry) =>
          React.createElement(
            "li",
            { className: "booking-workflow-panel__item", key: `${entry.state}-${entry.at}` },
            React.createElement(
              "div",
              { className: "booking-workflow-panel__item-title" },
              entry.state
            ),
            React.createElement(
              "div",
              { className: "booking-workflow-panel__item-detail" },
              entry.at
            )
          )
        )
      )
    )
  );
};

export default BookingWorkflowPanel;
