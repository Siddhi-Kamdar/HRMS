import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
  getSlotDetail,
  applyForSlot,
  cancelBooking,
  type SlotDetail
} from "../services/gameService";

const SlotDetailPage: React.FC = () => {

  const { slotId } = useParams();
  const navigate = useNavigate();

  const [slot, setSlot] =
    useState<SlotDetail | null>(null);

  const user =
    JSON.parse(localStorage.getItem("user") || "{}");

  const loadSlot = async () => {

    const data = await getSlotDetail(
      Number(slotId),
      user.employeeId
    );

    setSlot(data);
  };

  useEffect(() => {
    loadSlot();
  }, []);


  const handleApply = async () => {

    try {

      await applyForSlot({
        slotId: Number(slotId),
        leaderEmpId: user.employeeId,
        members: [user.employeeId]
      });

      alert("Applied successfully");

      loadSlot();

    } catch (err: any) {
      alert(
        err.response?.data ||
        "Application failed"
      );
    }
  };

  const handleCancel = async () => {

    const confirm =
      window.confirm(
        "Cancel your booking?"
      );

    if (!confirm) return;

    try {

      await cancelBooking({
        slotId: Number(slotId),
        cancelledByEmpId:
          user.employeeId
      });

      alert("Slot cancelled");

      navigate("/app/games");

    } catch {
      alert("Cancel failed");
    }
  };


  if (!slot)
    return <div>Loading...</div>;



  return (

    <div className="container mt-4">

      <h3>{slot.gameName}</h3>

      <div className="card p-4">

        <h5>Date</h5>
        <p>{slot.slotDate}</p>

        <h5>Time</h5>
        <p>
          {slot.startTime}
          {" - "}
          {slot.endTime}
        </p>

        <h5>Slot Status</h5>
        <span className="badge bg-info">
          {slot.status}
        </span>

        <hr />

        <h5>Your Status</h5>

        {slot.myStatus === "CONFIRMED" &&
          <span className="badge bg-success">
            Confirmed
          </span>}

        {slot.myStatus === "WAITING" &&
          <span className="badge bg-warning">
            In Queue
          </span>}

        {slot.myStatus === "NONE" &&
          <span className="badge bg-secondary">
            Not Joined
          </span>}

        <hr />

        {slot.myStatus === "NONE"
          && slot.status !== "COMPLETED" && (

          <button
            className="btn btn-primary"
            onClick={() =>
                navigate(`/app/games/slot/${slotId}/book`)
            }
          >
            Join Slot / Queue
          </button>
        )}

        {slot.myStatus === "CONFIRMED" && (

          <button
            className="btn btn-danger"
            onClick={handleCancel}
          >
            Cancel Booking
          </button>
        )}

      </div>

    </div>
  );
};

export default SlotDetailPage;