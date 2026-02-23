import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import {
  getGames,
  getSlots,
  cancelBooking,
  type Game,
  type Slot,
  getSlotDetail,
  completeSlot
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
  const user = JSON.parse(localStorage.getItem("user")|| "{}");
  const handleGameSelect = async (gameId: number) => {
    setSelectedGame(gameId);
    const data = await getSlots(gameId, user.employeeId);
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

    const slotStartTime = new Date(start);
    const slotEndTime = new Date(end);
    const isExpired = slot.status === "COMPLETED" || slot.status === "BOOKED" ? false : slotStartTime.getTime() < new Date().getTime();

    if(slot.status === "BOOKED" && slotEndTime.getTime() < new Date().getTime()){
      completeSlot(slot.slotId);
    }
    const getColor = () => {
      if(isExpired){
        return "#6c757d"
      }
      if (slot.isMySlot) {
        return "#b6d722";
      }

      switch (slot.status) {
        case "OPEN":
          return "#63ec83";

        case "BOOKED":
          return "#c05c66";

        case "COMPLETED":
          return "#3fb1d1";

        case "CANCLED":
          return "#ec9e5f";

        default:
          return "#0d6efd";
      }
    };

    var title;
    title =
    isExpired ? "Expired" :
      slot.status === "OPEN"
        ? "Available"
        : slot.isMySlot
          ? `Your Slot`
          : `Booked By: ${slot.bookedBy ?? "Employee"} (Join Queue)`;

      if(slot.status == "COMPLETED"){  
          title =  "Completed Slot";
      }
    

    console.log(slot);
    return {
      id: slot.slotId.toString(),
      title,
      start,
      end,
      backgroundColor: getColor(),
      borderColor: getColor(),

      extendedProps: {
        status: slot.status,
        bookedBy: slot.bookedBy,
        isMySlot: slot.isMySlot,
        isExpired
      }
    };
  });

//  const handleEventClick = (info: any) => {

//   const slotId = Number(info.event.id);

//   const {
//     status,
//     isMySlot
//   } = info.event.extendedProps;

//   if (status === "COMPLETED") return;

//   if (isMySlot && status === "BOOKED") {

//     const confirmCancel =
//       window.confirm(
//         "Cancel your booked slot?"
//       );

//     if (!confirmCancel) return;

//     cancelBooking({
//       slotId,
//       cancelledByEmpId: user.employeeId
//     })
//     .then(async () => {

//       alert("Slot cancelled");

//       if (selectedGame) {
//         const data =
//           await getSlots(
//             selectedGame,
//             user.employeeId
//           );
//         setSlots(data);
//       }

//     })
//     .catch(() =>
//       alert("Cancel failed")
//     );

//     return;
//   }

//   navigate(`/app/games/slot/${slotId}/book`);
// };

const handleEventClick = (info: any) => {

  const {isExpired} = info.event.extendedProps;
  const slotId = Number(info.event.id);
  const { status } = info.event.extendedProps;

  if (status === "COMPLETED" || isExpired ) return;

  navigate(`/app/games/slot/${slotId}`);
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
          slotDuration="00:10:00"
          slotMinTime="00:00:00"
          slotMaxTime="24:00:00"
          height="90vh"
          slotLabelInterval="01:00"
          events={calendarEvents}
          eventClick={handleEventClick}
          eventDidMount={(info) => {
            const bookedBy = info.event.extendedProps.bookedBy;

            if (bookedBy) {
              info.el.title = `Booked by: ${bookedBy}`;
            }
          }}
          expandRows={true}
        />
      )}
    </div>
  );
};

export default GameSchedule;