import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
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
    <Container className="mt-4">

      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h3 className="mb-0">Travel Plans</h3>
          <small className="text-muted">
            Manage and view company travel schedules
          </small>
        </div>

        <div className="d-flex gap-2">
          <Button
            variant="outline-success"
            onClick={() => navigate("/app/expenses/personal")}
          >
            My Expenses & Status
          </Button>

          {user.role === "HR" && (
            <Button
              variant="success"
              onClick={() => navigate("/app/travel/create")}
            >
              + Create Travel
            </Button>
          )}
        </div>
      </div>

      <Row className="g-4">
        {travels.map((travel) => (
          <Col key={travel.travelId} xs={12} md={6} lg={4}>
            <Card className="h-100 shadow-sm border-0">

              <Card.Body className="d-flex flex-column">

                <div className="text-muted small mb-2">
                  Travel #{travel.travelId}
                </div>

                <h5 className="mb-3">{travel.destination}</h5>

                <div className="mb-2">
                  <span className="fw-semibold">Employees:</span>
                  <div className="text-muted small">
                    {travel.employeeNames?.join(", ")}
                  </div>
                </div>

                <div className="d-flex justify-content-between mb-3 mt-2">
                  <div>
                    <small className="text-muted">Departure</small>
                    <div>
                      {new Date(travel.departDate).toLocaleDateString()}
                    </div>
                  </div>

                  <div>
                    <small className="text-muted">Return</small>
                    <div>
                      {new Date(travel.returnDate).toLocaleDateString()}
                    </div>
                  </div>
                </div>

                <div className="mt-auto">
                  <NavLink
                    to={`/app/travel/${travel.travelId}`}
                    className="btn btn-outline-success w-100"
                  >
                    View Details
                  </NavLink>
                </div>

              </Card.Body>

            </Card>
          </Col>
        ))}
      </Row>

    </Container>
  );
};

export default TravelDisplay;