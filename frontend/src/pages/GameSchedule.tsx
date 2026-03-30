/* eslint-disable react-hooks/set-state-in-effect */
/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import { Container, Card, Row, Col, Form } from "react-bootstrap";

import {
  getGames,
  getSlots,
  completeSlot,
  type Game,
  type Slot
} from "../services/gameService";

import { useNavigate } from "react-router-dom";

const GameSchedule: React.FC = () => {

  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [games, setGames] = useState<Game[]>([]);
  const [selectedGame, setSelectedGame] = useState<number | null>(null);
  const [slots, setSlots] = useState<Slot[]>([]);

  
  const loadGames = async () => {
    const data = await getGames();
    setGames(data);

    if (data.length > 0) {
      const firstGameId = data[0].gameId;
      setSelectedGame(firstGameId);

      const slotsData = await getSlots(firstGameId, user.employeeId);
      setSlots(slotsData);
    }
  };
  
  useEffect(() => {
    loadGames();
  }, []);


  const handleGameSelect = async (gameId: number) => {
    setSelectedGame(gameId);
    const data = await getSlots(gameId, user.employeeId);
    setSlots(data);
  };

  const calendarEvents = slots.map((slot) => {

    const start = `${slot.slotDate}T${slot.startTime}`;
    const end = `${slot.slotDate}T${slot.endTime}`;

    const slotStartTime = new Date(start);
    const slotEndTime = new Date(end);

    const isExpired =
      slot.status === "COMPLETED" || slot.status === "BOOKED"
        ? false
        : slotStartTime.getTime() < new Date().getTime();

    if (slot.status === "BOOKED" && slotEndTime.getTime() < new Date().getTime()) {
      completeSlot(slot.slotId);
    }

    const getColor = () => {

      if (isExpired) return "#6c757d";

      if (slot.isMySlot) return "#b6d722";

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

    let title;

    if (isExpired) {
      title = "Expired";
    } else if (slot.status === "OPEN") {
      title = "Available";
    } else if (slot.status === "CANCLED") {
      title = "Cancelled";
    } else if (slot.status === "COMPLETED") {
      title = "Completed Slot";
    } else if (slot.isMySlot) {
      title = "Your Slot";
    } else {
      title = `Booked By: ${slot.bookedBy ?? "Employee"} (Join Queue)`;
    }


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

  const handleEventClick = (info: any) => {

    const { isExpired } = info.event.extendedProps;
    const { status } = info.event.extendedProps;
    const slotId = Number(info.event.id);

    if (
      status === "COMPLETED" ||
      status === "CANCLED" ||
      isExpired
    ) return;

    navigate(`/app/games/${slotId}`);
  };

  return (

    <Container className="mt-4">

      <Card className="shadow-sm edge border-0 mb-4">
        <Card.Body>

          <Row className="align-items-center">

            <Col md={8}>
              <h4 className="mb-0">Game Schedule</h4>
              {/* <small className="text-muted">
                Book and manage game slots
              </small> */}
            </Col>

            <Col md={4}>
              <Form.Select
                value={selectedGame ?? ""}
                className="edge"
                onChange={(e) =>
                  handleGameSelect(Number(e.target.value))
                }
              >
                {games.map(game => (
                  <option key={game.gameId} value={game.gameId}>
                    {game.gameName}
                  </option>
                ))}
              </Form.Select>
            </Col>

          </Row>

        </Card.Body>
      </Card>

      {selectedGame && (
        <Card className="shadow-sm border-0">
          <Card.Body>

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
              slotMinTime="10:00:00"
              slotMaxTime="22:00:00"
              height="85vh"
              slotLabelInterval="01:00"
              events={calendarEvents}
              eventClick={handleEventClick}
              expandRows={true}
              eventDidMount={(info) => {
                const bookedBy = info.event.extendedProps.bookedBy;
                if (bookedBy) {
                  info.el.title = `Booked by: ${bookedBy}`;
                }
              }}
            />

          </Card.Body>
        </Card>
      )}

    </Container>
  );
};

export default GameSchedule;