import React from "react";

const AdminBookingState = ({ rawResponse }) => {
  return (
    <section className="admin-booking-state" aria-label="Booking state response">
      <pre>{rawResponse ?? ""}</pre>
    </section>
  );
};

export default AdminBookingState;
