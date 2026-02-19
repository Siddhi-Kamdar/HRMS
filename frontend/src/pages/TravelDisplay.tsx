
import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import { NavLink, useNavigate } from "react-router-dom";
import { getTravels, type Travel } from "../services/travelService";

const TravelDisplay: React.FC = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [travels, setTravels] = useState<Travel[]>([]);

  useEffect(() => {
    loadTravels();
  }, []);

  const loadTravels = async () => {
    const data = await getTravels();
    setTravels(data);
  };

  return (
    <Container fluid style={{ marginTop: "20px" }}>

      {user.role === "HR" && (
        <div className="d-flex justify-content-end mb-3">
          <button
            className="btn btn-success"
            onClick={() => navigate("/app/travel/create")}
          >
            + Create Travel
          </button>
        </div>
      )}

      <Row className="gy-4">
        {travels.map((travel) => (
          <Col key={travel.travelId} sm={12} md={6} lg={4}>
            <Card className="shadow-sm h-100">

              <Card.Body>
                <Card.Title className="mb-3">
                  Travel #{travel.travelId}
                </Card.Title>

                <Card.Text>
                  <strong>Employees:</strong>{" "}
                  {travel.employeeNames?.join(", ")}
                </Card.Text>

                <Card.Text>
                  <strong>Destination:</strong> {travel.destination}
                </Card.Text>

                <Card.Text>
                  <strong>Departure:</strong>{" "}
                  {new Date(travel.departDate).toLocaleDateString()}
                </Card.Text>

                <Card.Text>
                  <strong>Return:</strong>{" "}
                  {new Date(travel.returnDate).toLocaleDateString()}
                </Card.Text>
              </Card.Body>

              <Card.Footer className="bg-white text-center">
                <NavLink
                  to={`/app/travel/${travel.travelId}`}
                  className="text-success fw-semibold text-decoration-none"
                >
                  View Details →
                </NavLink>
              </Card.Footer>

            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default TravelDisplay;