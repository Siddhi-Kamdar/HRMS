import React, { useEffect, useState } from "react";
import { NavLink, useNavigate, useParams } from "react-router-dom";
import { Container, Row, Col, Card } from "react-bootstrap";
import {
  getTravelById,
  type Travel
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

  const user =
    JSON.parse(localStorage.getItem("user") || "{}");

  const [travel, setTravel] =
    useState<Travel | null>(null);

  const [documents, setDocuments] =
    useState<TravelDocument[]>([]);

  const [file, setFile] =
    useState<File | null>(null);

  const [activeTab, setActiveTab] =
    useState<"documents" | "expenses">
      ("documents");

  const [canSubmitExpense,
    setCanSubmitExpense] =
    useState(false);

  const [expenses, setExpenses] = useState<Expense[]>([]);

  useEffect(() => {
    loadExpenses();
  }, []);

  const loadExpenses = async () => {
    const data = await getMyExpenses();
    setExpenses(data);
    console.log(expenses);
  };


  useEffect(() => {
    if (travelId) {
      loadTravel();
      loadDocuments();
    }
  }, []);



  const loadTravel = async () => {

    const data =
      await getTravelById(travelId!);

    setTravel(data);

    const loggedUserId = Number(user.employeeId);
    const assigned = data.employeeIds?.some(
      (id: number) => Number(id) === loggedUserId
    )
    if (assigned || user.role === "HR") {
      setCanSubmitExpense(true);
    } else {
      setCanSubmitExpense(false);
    }
    console.log("Travel Employees:", data.employeeIds);
    console.log("Logged User:", user.employeeId);
    console.log("Role:", user.role);
  };



  const loadDocuments = async () => {

    const data =
      await getDocumentsByTravel(travelId!);

    setDocuments(data);
  };



  const handleUpload = async () => {

    if (!file) return;

    await uploadDocument(
      travelId!,
      file,
      user.employeeId,
      user.role === "EMPLOYEE"
        ? user.employeeId
        : undefined
    );

    setFile(null);

    loadDocuments();
  };



  if (!travel)
    return <div>Loading...</div>;



  return (

    <div className="card p-4 shadow-sm">

      <h4>Travel Detail</h4>

      <p>
        <strong>Destination:</strong>
        {travel.destination}
      </p>

      <p>
        <strong>Employees:</strong>
        {travel.employeeNames?.join(", ")}
      </p>

      <p>
        <strong>Departure:</strong>
        {new Date(
          travel.departDate
        ).toLocaleDateString()}
      </p>

      <p>
        <strong>Return:</strong>
        {new Date(
          travel.returnDate
        ).toLocaleDateString()}
      </p>

      <hr />

      <div className="mb-3">

        <button
          className={`btn me-2 ${activeTab === "documents"
            ? "btn-success"
            : "btn-outline-success"
            }`}
          onClick={() =>
            setActiveTab("documents")}
        >
          Documents
        </button>

        <button
          className={`btn me-2 ${activeTab === "expenses"
            ? "btn-success"
            : "btn-outline-success"
            }`}
          onClick={() =>
            setActiveTab("expenses")}
        >
          Expenses
        </button>

        {canSubmitExpense && (
          <button
            className="btn btn-primary mt-3"
            onClick={() =>
              navigate(`/app/expense/create/${travel.travelId}`)
            }
          >
            + Add Expense
          </button>
        )}


      </div>



      {activeTab === "documents" && (

        <div>

          <ul>
            {documents.map(doc => (
              <li key={doc.id}>
                <a
                  href={`http://localhost:8080/${doc.documentUrl}`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  {doc.documentUrl
                    .split(/[/\\]/)
                    .pop()}
                </a>
              </li>
            ))}
          </ul>

          <input
            type="file"
            onChange={e =>
              setFile(
                e.target.files?.[0] || null
              )}
          />

          <button
            className="btn btn-success mt-2"
            onClick={handleUpload}
          >
            Upload
          </button>

        </div>

      )}



      {activeTab === "expenses"
        && (
          <Container fluid style={{ marginTop: "20px" }}>
            <Row className="gy-4">
              {expenses.map(exp => {
                if (exp.travelId.toString() === travelId) {
                  return <Col
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
                      {/* <Card.Text>
                        <strong>Destination: </strong> {exp.destination}
                      </Card.Text>
                      <Card.Text>
                        <strong>Travel Id: </strong> {exp.travelId}
                      </Card.Text> */}
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
                }
              })}
            </Row>
          </Container>
        )}



    </div>
  );
};

export default TravelDetail;