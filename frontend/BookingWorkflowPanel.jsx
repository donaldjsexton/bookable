import React, { useEffect, useState } from "react";

const EMPTY_WORKFLOW = {
  state: "",
  allowedActions: [],
  blockedActions: [],
  timeline: [],
};

const BookingWorkflowPanel = ({ bookingId }) => {
  const [workflow, setWorkflow] = useState(EMPTY_WORKFLOW);

  const fetchWorkflow = () => {
    const url = `/api/bookings/${encodeURIComponent(bookingId)}/workflow`;

    return fetch(url, { method: "GET" })
      .then((response) => response.json())
      .then((data) => setWorkflow(data))
      .catch(() => setWorkflow(...EMPTY_WORKFLOW));
  };

  useEffect(() => {
    fetchWorkflow();
  }, [bookingId]);

  const triggerAction = (action) => {
    const url = `/api/bookings/${encodeURIComponent(bookingId)}/actions`;

    return fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ action: action.action }),
    })
      .then(() => fetchWorkflow())
      .catch(() => null);
  };

  return (
    <section className="booking-workflow-panel" aria-label="Booking workflow state">
      <section>
        <h2>Current State</h2>
        <div>{workflow.state}</div>
      </section>
      <section>
        <h2>Allowed Actions</h2>
        <ul>
          {workflow.allowedActions.map((action) => (
            <li key={action.action}>
              <div>{action.label}</div>
              <div>{action.description}</div>
              <button type="button" onClick={() => triggerAction(action)}>
                {action.action}
              </button>
            </li>
          ))}
        </ul>
      </section>
      <section>
        <h2>Blocked Actions</h2>
        <ul>
          {workflow.blockedActions.map((action) => (
            <li key={action.action}>
              <div>{action.action}</div>
              <div>{action.reason}</div>
            </li>
          ))}
        </ul>
      </section>
      <section>
        <h2>Timeline</h2>
        <ul>
          {workflow.timeline.map((entry) => (
            <li key={`${entry.state}-${entry.at}`}>
              <div>{entry.state}</div>
              <div>{entry.at}</div>
            </li>
          ))}
        </ul>
      </section>
    </section>
  );
};

export default BookingWorkflowPanel;
