/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import { getGames, getSlots, enableSlot, disableSlot } from "../services/gameService";

const HrSlotManager: React.FC = () => {

  const [games, setGames] = useState<any[]>([]);
  const [selectedGame, setSelectedGame] = useState<number | null>(null);
  const [slots, setSlots] = useState<any[]>([]);

  const [selectedSlot, setSelectedSlot] = useState<any>(null);
  const [showModal, setShowModal] = useState(false);

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  
  const loadSlots = async (gameId: number) => {

    const data = await getSlots(gameId, user.employeeId);
    setSlots(data);
  };

  const loadGames = async () => {

    const data = await getGames();
    setGames(data);

    if (data.length > 0) {
      setSelectedGame(data[0].gameId);
      loadSlots(data[0].gameId);
    }
  };

  useEffect(() => {
    loadGames();
  }, []);

  const calendarEvents = slots.map((slot: any) => {

    const start = `${slot.slotDate}T${slot.startTime}`;
    const end = `${slot.slotDate}T${slot.endTime}`;

    const getColor = () => {
      switch (slot.status) {
        case "OPEN":
          return "#63ec83";
        case "BOOKED":
          return "#c05c66";
        case "COMPLETED":
          return "#3fb1d1";
        case "DISABLED":
          return "#6c757d";
        default:
          return "#0d6efd";
      }
    };

    return {
      id: slot.slotId.toString(),
      title: slot.status,
      start,
      end,
      backgroundColor: getColor(),
      borderColor: getColor(),
      extendedProps: {
        status: slot.status,
        slotDate: slot.slotDate,
        startTime: slot.startTime,
        endTime: slot.endTime
      }
    };
  });

  const handleEventClick = (info: any) => {

    const slotId = Number(info.event.id);
    const status = info.event.extendedProps.status;

    if (status === "BOOKED" || status === "COMPLETED") return;

    setSelectedSlot({
      slotId,
      status,
      slotDate: info.event.extendedProps.slotDate,
      startTime: info.event.extendedProps.startTime,
      endTime: info.event.extendedProps.endTime
    });

    setShowModal(true);
  };

  const handleDisable = async () => {

    await disableSlot(selectedSlot.slotId);

    setShowModal(false);
    loadSlots(selectedGame!);
  };

  const handleEnable = async () => {

    await enableSlot(selectedSlot.slotId);

    setShowModal(false);
    loadSlots(selectedGame!);
  };

  const formatTime = (time: string) => time.slice(0,5);

  return (

    <div className="container mt-4">

      <h4>HR Slot Manager</h4>

      <select
        className="form-select mb-3 edge"
        value={selectedGame ?? ""}
        onChange={(e) => {

          const id = Number(e.target.value);
          setSelectedGame(id);
          loadSlots(id);

        }}
      >

        {games.map((g) => (
          <option key={g.gameId} value={g.gameId}>
            {g.gameName}
          </option>
        ))}

      </select>

      <FullCalendar
        plugins={[timeGridPlugin, interactionPlugin]}
        initialView="timeGridWeek"
        headerToolbar={{
          left: "prev,next,today",
          center: "title",
          right: "timeGridWeek,timeGridDay"
        }}
        allDaySlot={false}
        slotDuration="00:10:00"
        slotMinTime="00:00:00"
        slotMaxTime="24:00:00"
        height="85vh"
        events={calendarEvents}
        eventClick={handleEventClick}
      />

      {showModal && (

        <div className="modal fade show d-block">

          <div className="modal-dialog">
            <div className="modal-content">

              <div className="modal-header">

                <h5 className="modal-title">
                  Slot Action
                </h5>

                <button
                  className="btn-close edge"
                  onClick={() => setShowModal(false)}
                />

              </div>

              <div className="modal-body edge">

                <p>
                  <b>Date:</b> {selectedSlot.slotDate}
                </p>

                <p>
                  <b>Time:</b> {formatTime(selectedSlot.startTime)} - {formatTime(selectedSlot.endTime)}
                </p>

                {selectedSlot.status === "OPEN" && (
                  <p>This slot is currently open. Disable it?</p>
                )}

                {selectedSlot.status === "CANCLED" && (
                  <p>This slot is disabled. Enable it?</p>
                )}

              </div>

              <div className="modal-footer">

                <button
                  className="btn btn-secondary edge"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>

                {selectedSlot.status === "OPEN" && (
                  <button
                    className="btn btn-danger edge"
                    onClick={handleDisable}
                  >
                    Disable Slot
                  </button>
                )}

                {selectedSlot.status === "CANCLED" && (
                  <button
                    className="btn btn-success edge"
                    onClick={handleEnable}
                  >
                    Enable Slot
                  </button>
                )}

              </div>

            </div>
          </div>

        </div>

      )}

    </div>

  );
};

export default HrSlotManager;