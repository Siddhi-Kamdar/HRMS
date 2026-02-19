import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import {
  getGames,
  getSlots,
  type Game,
  type Slot
} from "../services/gameService";
import { useNavigate } from "react-router-dom";

const GameSchedule: React.FC = () => {

  const [games, setGames] = useState<Game[]>([]);
  const [selectedGame, setSelectedGame] = useState<number | null>(null);
  const [slots, setSlots] = useState<Slot[]>([]);
  const navigate = useNavigate();


  useEffect(() => {
    loadGames();
  }, []);

  const loadGames = async () => {
    const data = await getGames();
    setGames(data);
  };

  const handleGameSelect = async (gameId: number) => {
    setSelectedGame(gameId);
    const data = await getSlots(gameId);
    setSlots(data);
  };

  // const refreshSlots = async () => {
  //   if (selectedGame) {
  //     const data = await getSlots(selectedGame);
  //     setSlots(data);
  //   }
  // };

  const calendarEvents = slots.map((slot) => {

    const start = `${slot.slotDate}T${slot.startTime}`;
    const end = `${slot.slotDate}T${slot.endTime}`;

    const getColor = () => {
      switch (slot.status) {
        case "OPEN":
          return "#63ec83";
        case "BOOKED":
          return "#c05c66";
        case "COMPLETED":
          return "#6c757d";
        case "CANCLED":
          return "#ec9e5f";
        default:
          return "#0d6efd";
      }
    };

    return {
      id: slot.slotId.toString(),
      title: `${slot.startTime} - ${slot.endTime}`,
      start,
      end,
      backgroundColor: getColor(),
      borderColor: getColor(),
      extendedProps: {
        status: slot.status
      }
    };
  });

  const handleEventClick = (info: any) => {
  const slotId = Number(info.event.id);
  navigate(`/app/games/slot/${slotId}/book`);
};

  return (
    <div className="container mt-4">

      {/* <h3 className="mb-4">Game Scheduling</h3> */}

      <div className="mb-4">
        <label className="form-label">Select Game</label>
        <select
          className="form-select"
          onChange={(e) => handleGameSelect(Number(e.target.value))}
        >
          <option>Select Game</option>
          {games.map(game => (
            <option key={game.gameId} value={game.gameId}>
              {game.gameName}
            </option>
          ))}
        </select>
      </div>

      {selectedGame && (
        <FullCalendar
          plugins={[timeGridPlugin, interactionPlugin]}
          initialView="timeGridWeek"
          headerToolbar={{
            left: "prev,next today",
            center: "title",
            right: ""
          }}
          allDaySlot={false}
          slotMinTime="00:00:00"
          slotMaxTime="24:00:00"
          events={calendarEvents}
          eventClick={handleEventClick}
          height="auto"
        />
      )}
    </div>
  );
};

export default GameSchedule;