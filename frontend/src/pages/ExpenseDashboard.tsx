/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable react-hooks/set-state-in-effect */
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
  const [search, setSearch] = useState("");

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

  useEffect(() => {
    loadExpenses();
  }, []);

  const approve = async (id: number) => {
    await approveExpense(id, user.employeeId);
    loadExpenses();
  };

  const reject = async (id: number) => {
    const remark = prompt("Reject reason") || "";
    await rejectExpense(id, user.employeeId, remark);
    loadExpenses();
  };

  const filteredExpenses = expenses.filter((exp) => {
    const term = search.toLowerCase();

    return (
      exp.employeeName.toLowerCase().includes(term) ||
      exp.destination.toLowerCase().includes(term) ||
      exp.status.toLowerCase().includes(term)
    );
  });

  return (

    <Container className="mt-4">

      <Card className="shadow-sm border-0 edge">
        <Card.Body>
          <div className="d-flex justify-content-between align-items-center mb-3">

            <h4 className="mb-0">Expense Dashboard</h4>
            {/* <small className="text-muted">
                View and manage travel expenses
              </small> */}
            <input
              type="text"
              placeholder="Search employee, destination..."
              className="form-control w-25 edge"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />

          </div>
          <div className="table-scroll">
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

                {filteredExpenses.map(exp => (
                  <tr key={exp.expenseId}>

                    <td>{exp.employeeName}</td>

                    <td>{exp.destination}</td>

                    <td>₹{exp.amount}</td>

                    <td>
                      <span
                        className={
                          exp.status === "APPROVED"
                            ? "badge bg-success edge"
                            : exp.status === "REJECTED"
                              ? "badge bg-danger edge"
                              : "badge bg-warning edge text-dark"
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
                        className="btn btn-sm btn-outline-secondary edge"
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
                            className="edge"
                          >
                            Approve
                          </Button>

                          <Button
                            size="sm"
                            variant="outline-danger"
                            onClick={() => reject(exp.expenseId)}
                            className="edge"
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
          </div>
        </Card.Body>
      </Card>

    </Container>
  );
};

export default ExpenseDashboard;