import React, { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Card, Row, Col, Form, Button } from "react-bootstrap";

import { createTravel } from "../services/travelService";
import { getEmployees, type Employee } from "../services/employeeService";

const TravelCreate: React.FC = () => {

  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selectedEmployees, setSelectedEmployees] = useState<number[]>([]);
  const [destination, setDestination] = useState("");
  const [departDate, setDepartDate] = useState("");
  const [returnDate, setReturnDate] = useState("");

  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    loadEmployees();
  }, []);

  const loadEmployees = async () => {
    const data = await getEmployees();
    setEmployees(data);
  };

  const handleEmployeeChange = (id: number) => {
    setSelectedEmployees((prev) =>
      prev.includes(id)
        ? prev.filter((empId) => empId !== id)
        : [...prev, id]
    );
  };

  const validateForm = () => {

    const newErrors: Record<string, string> = {};

    if (!destination.trim()) {
      newErrors.destination = "Destination is required";
    } else if (destination.length < 3) {
      newErrors.destination = "Destination must be at least 3 characters";
    }

    if (!departDate) {
      newErrors.departDate = "Departure date is required";
    }

    if (!returnDate) {
      newErrors.returnDate = "Return date is required";
    }

    if (departDate && returnDate && returnDate < departDate) {
      newErrors.returnDate = "Return date cannot be before departure date";
    }

    if (selectedEmployees.length === 0) {
      newErrors.employees = "Please select at least one employee";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: FormEvent) => {

    e.preventDefault();

    if (!validateForm()) return;

    try {

      await createTravel({
        schedulerId: user.employeeId,
        employeeIds: selectedEmployees,
        destination,
        departDate,
        returnDate
      });

      navigate("/app/travel");
    } catch (error) {
      setErrors({
        api: "Failed to create travel. Please try again."
      });
    }
  };

  return (

    <Container className="mt-4">

      <Row className="justify-content-center">

        <Col md={8} lg={6}>

          <Card className="shadow-sm border-0 edge">

            <Card.Body>

              <div className="mb-4">
                <h4 className="mb-1">Create Travel</h4>
                <small className="text-muted">
                  Schedule a travel plan for employees
                </small>
              </div>

              {errors.api && (
                <div className="alert alert-danger">
                  {errors.api}
                </div>
              )}

              <Form onSubmit={handleSubmit} noValidate>

                <Form.Group className="mb-3">
                  <Form.Label>Destination</Form.Label>

                  <Form.Control
                    type="text"
                    value={destination}
                    isInvalid={!!errors.destination}
                    className="edge"
                    onChange={(e) =>
                      setDestination(e.target.value)
                    }
                  />

                  <Form.Control.Feedback className="edge" type="invalid">
                    {errors.destination}
                  </Form.Control.Feedback>
                </Form.Group>

                <Row>

                  <Col md={6}>
                    <Form.Group className="mb-3">

                      <Form.Label>Departure Date</Form.Label>

                      <Form.Control
                        type="date"
                        value={departDate}
                        min={new Date().toISOString().split("T")[0]}
                        isInvalid={!!errors.departDate}
                        onChange={(e) =>
                          setDepartDate(e.target.value)
                        }
                        className="edge"
                      />

                      <Form.Control.Feedback type="invalid">
                        {errors.departDate}
                      </Form.Control.Feedback>

                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3 edge">

                      <Form.Label>Return Date</Form.Label>

                      <Form.Control
                        type="date"
                        value={returnDate}
                        min={
                          departDate ||
                          new Date().toISOString().split("T")[0]
                        }
                        isInvalid={!!errors.returnDate}
                        onChange={(e) =>
                          setReturnDate(e.target.value)
                        }
                        className="edge"
                      />

                      <Form.Control.Feedback type="invalid">
                        {errors.returnDate}
                      </Form.Control.Feedback>

                    </Form.Group>
                  </Col>

                </Row>

                <Form.Group className="mb-3 edge">

                  <Form.Label>Select Employees</Form.Label>

                  <div
                    className={`border rounded p-3 edge ${
                      errors.employees ? "border-danger" : ""
                    }`}
                    style={{
                      maxHeight: "160px",
                      overflowY: "auto"
                    }}
                  >

                    {employees.map((emp) => (

                      <Form.Check
                        key={emp.employeeId}
                        type="checkbox"
                        label={emp.fullName}
                        checked={selectedEmployees.includes(emp.employeeId)}
                        onChange={() =>
                          handleEmployeeChange(emp.employeeId)
                        }
                      />

                    ))}

                  </div>

                  {errors.employees && (
                    <div className="text-danger mt-1">
                      {errors.employees}
                    </div>
                  )}

                </Form.Group>

                <Button
                  type="submit"
                  variant="success"
                  className="w-100 edge"
                >
                  Create Travel
                </Button>

              </Form>

            </Card.Body>

          </Card>

        </Col>

      </Row>

    </Container>
  );
};

export default TravelCreate;