
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  getTravelById,
  type Travel
} from "../services/travelService";
import {
  getDocumentsByTravel,
  uploadDocument,
  type TravelDocument
} from "../services/documentService";

const TravelDetail: React.FC = () => {

  const { travelId } = useParams();
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [travel, setTravel] = useState<Travel | null>(null);
  const [documents, setDocuments] = useState<TravelDocument[]>([]);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  useEffect(() => {
    if (travelId) {
      loadTravel();
      loadDocuments();
    }
  }, []);

  const loadTravel = async () => {
    const data = await getTravelById(travelId!);
    setTravel(data);
  };

  const loadDocuments = async () => {
    const data = await getDocumentsByTravel(travelId!);
    setDocuments(data);
  };

  const handleUpload = async () => {
    console.log("upload clicked");
    if (!selectedFile){
        console.log("no file selected");
        return;
    } 

    await uploadDocument(
      travelId!,
      selectedFile,
      user.employeeId
    );
    console.log("upload finished");

    setSelectedFile(null);
    loadDocuments();
  };

  if (!travel) return <div>Loading...</div>;

  return (
    <div className="card p-4 shadow-sm">

      <h4>Travel Detail</h4>

      <p><strong>Destination:</strong> {travel.destination}</p>
      <p>
        <strong>Employees:</strong> {travel.employeeNames?.join(", ")}
      </p>
      <p>
        <strong>Departure:</strong>{" "}
        {new Date(travel.departDate).toLocaleDateString()}
      </p>
      <p>
        <strong>Return:</strong>{" "}
        {new Date(travel.returnDate).toLocaleDateString()}
      </p>

      <hr />

      <h5>Documents</h5>

      {documents.length === 0 && <p>No documents uploaded yet.</p>}

      <ul>
        {documents.map((doc) => (
          <li key={doc.id}>
            {doc.documentUrl.split("\\").pop()}
          </li>
        ))}
      </ul>

      <hr />

      <h5>Upload Document</h5>

      <input
        type="file"
        onChange={(e) =>
          setSelectedFile(e.target.files?.[0] || null)
        }
      />

      <button
        className="btn btn-primary mt-2"
        onClick={handleUpload}
      >
        Upload
      </button>

    </div>
  );
};

export default TravelDetail;
