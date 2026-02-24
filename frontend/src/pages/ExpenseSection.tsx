import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import { NavLink } from "react-router-dom";


import {
  getMyExpenses,
  type Expense
} from "../services/expenseService";

const ExpenseSection: React.FC = () => {

  const [expenses, setExpenses] = useState<Expense[]>([]);

  useEffect(() => {
    loadExpenses();
  }, []);

  const loadExpenses = async () => {
    const data = await getMyExpenses();
    setExpenses(data);
    console.log(expenses);
  };

  return (

    <Container fluid style={{ marginTop: "20px" }}>
      <Row className="gy-4">

        <h5>My Expenses</h5>

        {expenses.map(exp => (
          <Col
            key={exp.expenseId}
            sm={12} md={6} lg={4}
          >
            <Card className="shadow-sm h-100">
              <Card.Body>
                <Card.Title className="mb-3">
                  Expense #{exp.expenseId}
                </Card.Title>
                <Card.Text>
                  <strong>Amount:</strong> {exp.amount}
                </Card.Text>
                <Card.Text>
                  <strong>Destination: </strong> {exp.destination}
                </Card.Text>
                <Card.Text>
                  <strong>Travel Id: </strong> {exp.travelId}
                </Card.Text>
                <Card.Text>
                  <strong>Status:</strong> {exp.status}
                </Card.Text>

                {exp.remark && (
                  <Card.Text>
                    <strong>Remark:</strong> {exp.remark}
                  </Card.Text>
                )}
                  <NavLink
                    to={`http://localhost:8080/${exp.proofUrl}`}
                    target="_blank" className="text-success fw-semibold text-decoration-none"
                  >
                    View Proof
                  </NavLink>
              </Card.Body>
            </Card>
          </Col>
        ))}

      </Row>
    </Container>
  );
};

export default ExpenseSection;