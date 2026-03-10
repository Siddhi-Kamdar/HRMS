import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import { getGames, getSlots, enableSlot, disableSlot } from "../services/gameService";

const HrSlotManager: React.FC = () => {

  const [games, setGames] = useState<any[]>([]);
  const [selectedGame, setSelectedGame] = useState<number | null>(null);
  const [slots, setSlots] = useState<any[]>([]);

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  useEffect(() => {
    loadGames();
  }, []);

  const loadGames = async () => {
    const data = await getGames();
    setGames(data);

    if (data.length > 0) {
      setSelectedGame(data[0].gameId);
      loadSlots(data[0].gameId);
    }
  };

  const loadSlots = async (gameId: number) => {
    const data = await getSlots(gameId, user.employeeId);
    setSlots(data);
  };

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
        status: slot.status
      }
    };
  });

  const handleEventClick = async (info: any) => {

    const slotId = Number(info.event.id);
    const status = info.event.extendedProps.status;

    if (status === "BOOKED" || status === "COMPLETED") return;

    if (status === "OPEN") {
      await disableSlot(slotId);
    } else {
      await enableSlot(slotId);
    }

    loadSlots(selectedGame!);
  };

  return (
    <div className="container mt-4">

      <h4>HR Slot Manager</h4>

      <select
        className="form-select mb-3"
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
          left: "prev,next today",
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

    </div>
  );
};

export default HrSlotManager;