import React, { useEffect, useState } from "react";
import { Container, Card, Table, Button } from "react-bootstrap";

import {
  getMyExpenses,
  getAllExpenses,
  getTeamExpenses,
  approveExpense,
  rejectExpense
} from "../services/expenseService";

const ExpenseDashboard: React.FC = () => {

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [expenses, setExpenses] = useState<any[]>([]);

  useEffect(() => {
    loadExpenses();
  }, []);

  const loadExpenses = async () => {

    let data;

    if (user.role === "HR")
      data = await getAllExpenses();
    else if (user.role === "MANAGER")
      data = await getTeamExpenses();
    else
      data = await getMyExpenses();

    setExpenses(data);
  };

  const approve = async (id: number) => {
    await approveExpense(id, user.employeeId);
    loadExpenses();
  };

  const reject = async (id: number) => {
    const remark = prompt("Reject reason") || "";
    await rejectExpense(id, user.employeeId, remark);
    loadExpenses();
  };

  return (

    <Container className="mt-4">

      <Card className="shadow-sm border-0">
        <Card.Body>

          <div className="d-flex justify-content-between align-items-center mb-3">
            <div>
              <h4 className="mb-0">Expense Dashboard</h4>
              <small className="text-muted">
                View and manage travel expenses
              </small>
            </div>
          </div>

          <Table hover responsive className="align-middle">

            <thead className="table-light">
              <tr>
                <th>Employee</th>
                <th>Travel</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Proof</th>
                {user.role === "HR" && <th>Action</th>}
              </tr>
            </thead>

            <tbody>

              {expenses.map(exp => (
                <tr key={exp.expenseId}>

                  <td>{exp.employeeName}</td>

                  <td>{exp.destination}</td>

                  <td>₹{exp.amount}</td>

                  <td>
                    <span
                      className={
                        exp.status === "APPROVED"
                          ? "badge bg-success"
                          : exp.status === "REJECTED"
                          ? "badge bg-danger"
                          : "badge bg-warning text-dark"
                      }
                    >
                      {exp.status}
                    </span>
                  </td>

                  <td>
                    <a
                      href={`http://localhost:8080/${exp.proofUrl}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="btn btn-sm btn-outline-secondary"
                    >
                      View
                    </a>
                  </td>

                  {user.role === "HR" && (
                    <td>

                      <div className="d-flex gap-2">

                        <Button
                          size="sm"
                          variant="outline-success"
                          onClick={() => approve(exp.expenseId)}
                        >
                          Approve
                        </Button>

                        <Button
                          size="sm"
                          variant="outline-danger"
                          onClick={() => reject(exp.expenseId)}
                        >
                          Reject
                        </Button>

                      </div>

                    </td>
                  )}

                </tr>
              ))}

            </tbody>

          </Table>

        </Card.Body>
      </Card>

    </Container>
  );
};

export default ExpenseDashboard;