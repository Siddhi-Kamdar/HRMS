import React, { useEffect, useState } from "react";
import { NavLink, useNavigate, useParams } from "react-router-dom";
import { Container, Row, Col, Card, Button, Form } from "react-bootstrap";

import {
  getTravelById,
  type Travel,
  type DropdownOption
} from "../services/travelService";

import {
  getDocumentsByTravel,
  uploadDocument,
  type TravelDocument
} from "../services/documentService";

import {
  getMyExpenses,
  type Expense
} from "../services/expenseService";

const TravelDetail: React.FC = () => {

  const { travelId } = useParams();
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [travel, setTravel] = useState<Travel | null>(null);
  const [documents, setDocuments] = useState<TravelDocument[]>([]);
  const [file, setFile] = useState<File | null>(null);

  const [activeTab, setActiveTab] =
    useState<"documents" | "expenses">("documents");

  const [canSubmitExpense, setCanSubmitExpense] = useState(false);
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [dropDownOptions, setDropDownOptions] = useState<DropdownOption[]>([]);
  const [selectedEmployeeId, setSelectedEmployeeId] = useState<string>("");

  useEffect(() => {
    loadExpenses();
  }, []);

  const loadExpenses = async () => {
    const data = await getMyExpenses();
    setExpenses(data);
  };

  useEffect(() => {
    if (travelId) {
      loadTravel();
      loadDocuments();
    }
  }, []);

  const loadTravel = async () => {
    const data = await getTravelById(travelId!);

    setTravel(data);

    const combined: DropdownOption[] = data.employeeIds.map((id, index) => ({
      id,
      name: data.employeeNames[index]
    }));

    setDropDownOptions(combined);

    const loggedUserId = Number(user.employeeId);

    const assigned = data.employeeIds?.some(
      (id: number) => Number(id) === loggedUserId
    );

    if (assigned || user.role === "HR") {
      setCanSubmitExpense(true);
    } else {
      setCanSubmitExpense(false);
    }
  };

  const loadDocuments = async () => {
    const data = await getDocumentsByTravel(travelId!);
    setDocuments(data);
  };

  const handleUpload = async () => {
    if (!file) return;

    await uploadDocument(
      travelId!,
      file,
      user.employeeId,
      selectedEmployeeId
    );

    setFile(null);
    loadDocuments();
  };

  if (!travel) return <div className="p-4">Loading...</div>;

  return (
    <Container className="mt-4">

      <Card className="shadow-sm border-0 mb-4">
        <Card.Body>

          <div className="d-flex justify-content-between align-items-center mb-3">
            <div>
              <h4 className="mb-0">{travel.destination}</h4>
              <small className="text-muted">
                Travel #{travel.travelId}
              </small>
            </div>

            {canSubmitExpense && (
              <Button
                variant="success"
                onClick={() =>
                  navigate(`/app/expenses/create/${travel.travelId}`)
                }
              >
                + Add Expense
              </Button>
            )}
          </div>

          <Row className="g-3 mt-2">

            <Col md={4}>
              <div className="text-muted small">Employees</div>
              <div>{travel.employeeNames?.join(", ")}</div>
            </Col>

            <Col md={4}>
              <div className="text-muted small">Departure</div>
              <div>
                {new Date(travel.departDate).toLocaleDateString()}
              </div>
            </Col>

            <Col md={4}>
              <div className="text-muted small">Return</div>
              <div>
                {new Date(travel.returnDate).toLocaleDateString()}
              </div>
            </Col>

          </Row>

        </Card.Body>
      </Card>

      <div className="d-flex gap-2 mb-4">
        <Button
          variant={activeTab === "documents" ? "success" : "outline-success"}
          onClick={() => setActiveTab("documents")}
        >
          Documents
        </Button>

        <Button
          variant={activeTab === "expenses" ? "success" : "outline-success"}
          onClick={() => setActiveTab("expenses")}
        >
          Expenses
        </Button>
      </div>

      {activeTab === "documents" && (
        <Card className="shadow-sm border-0 mb-4">
          <Card.Body>

            <Row className="g-3 mb-3">

              <Col md={4}>
                <Form.Control
                  type="file"
                  onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                    setFile(e.target.files?.[0] || null)
                  }
                />
              </Col>

              <Col md={4}>
                <Form.Select
                  value={selectedEmployeeId}
                  onChange={(e) =>
                    setSelectedEmployeeId(e.target.value)
                  }
                >
                  <option value="">Select Employee</option>
                  {dropDownOptions.map(opt => (
                    <option key={opt.id} value={opt.id}>
                      {opt.name}
                    </option>
                  ))}
                </Form.Select>
              </Col>

              <Col md={4}>
                <Button
                  variant="success"
                  onClick={handleUpload}
                >
                  Upload Document
                </Button>
              </Col>

            </Row>

            <ul className="list-unstyled">
              {documents.map(doc => (
                <li key={doc.id} className="mb-2">
                  <a
                    href={`http://localhost:8080/${doc.documentUrl}`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-decoration-none"
                  >
                    {doc.documentUrl.split(/[/\\]/).pop()}
                  </a>
                </li>
              ))}
            </ul>

          </Card.Body>
        </Card>
      )}

      {activeTab === "expenses" && (
        <Row className="g-4">

          {expenses
            .filter(exp => exp.travelId.toString() === travelId)
            .map(exp => (
              <Col key={exp.expenseId} md={6} lg={4}>
                <Card className="shadow-sm border-0 h-100">

                  <Card.Body>

                    <Card.Title className="mb-3">
                      Expense #{exp.expenseId}
                    </Card.Title>

                    <Card.Text>
                      <strong>Amount:</strong> {exp.amount}
                    </Card.Text>

                    <Card.Text>
                      <strong>Status:</strong> <span
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
                    </Card.Text>

                    {exp.remark && (
                      <Card.Text>
                        <strong>Remark:</strong> {exp.remark}
                      </Card.Text>
                    )}

                    <NavLink
                      to={`http://localhost:8080/${exp.proofUrl}`}
                      target="_blank"
                      className="btn btn-outline-success btn-sm"
                    >
                      View Proof
                    </NavLink>

                  </Card.Body>

                </Card>
              </Col>
            ))}

        </Row>
      )}

    </Container>
  );
};

export default TravelDetail;