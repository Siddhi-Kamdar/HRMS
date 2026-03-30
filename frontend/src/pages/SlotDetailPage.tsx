import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
  getSlotDetail,
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
    <div className="container">

      <div className="card edge shadow-sm border-0">
        <div className="card-body">

          <div className="d-flex justify-content-between align-items-center mb-3">
            <h4 className="mb-0">{slot.gameName}</h4>

            <span className="badge bg-info fs-6 edge">
              {slot.status}
            </span>
          </div>

          <hr />

          <div className="row mb-3">
            <div className="col-md-6">
              <h6 className="text-muted mb-1">Date</h6>
              <p className="mb-0">{slot.slotDate}</p>
            </div>

            <div className="col-md-6">
              <h6 className="text-muted mb-1">Time</h6>
              <p className="mb-0">
                {slot.startTime.slice(0, 5)} - {slot.endTime.slice(0, 5)}
              </p>
            </div>
          </div>

          <hr />

          <h6 className="text-muted mb-2">Your Status</h6>

          {slot.myStatus === "CONFIRMED" && (
            <span className="badge bg-success fs-6 edge">
              Confirmed
            </span>
          )}

          {slot.myStatus === "WAITING" && (
            <span className="badge bg-warning text-dark fs-6 edge">
              In Queue
            </span>
          )}

          {slot.myStatus === "PREEMPTED" && (
            <span className="badge bg-danger fs-6 edge">
              Higher priority player joined, You are in Queue now.
            </span>
          )}

          {slot.myStatus === "NONE" && (
            <span className="badge bg-secondary fs-6 edge">
              Not Joined
            </span>
          )}

          <hr />

          <div className="d-flex gap-2">

            {slot.myStatus === "NONE" && slot.status !== "COMPLETED" && (
              <button
                className="btn btn-success edge"
                onClick={() =>
                  navigate(`/app/games/${slotId}/book`)
                }
              >
                Join Slot / Queue
              </button>
            )}

            {slot.myStatus === "CONFIRMED" && (
              <button
                className="btn btn-outline-danger edge"
                onClick={handleCancel}
              >
                Cancel Booking
              </button>
            )}

            <button
              className="btn btn-outline-secondary edge"
              onClick={() => navigate("/app/games")}
            >
              Back
            </button>

          </div>

        </div>
      </div>

    </div>
  );
};

export default SlotDetailPage;