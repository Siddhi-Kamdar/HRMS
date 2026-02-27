import React, { useState } from "react";
import { Container, Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { createJob } from "../services/jobService";

const CreateJob = () => {
  const navigate = useNavigate();

  const [jobTitle, setJobTitle] = useState("");
  const [jobSummary, setJobSummary] = useState("");
  const [jobStatus, setJobStatus] = useState("OPEN");
  const [file, setFile] = useState<File | null>(null);

  const handleSubmit = async () => {
  if (!jobTitle || !jobSummary || !file) {
    alert("Fill all required fields");
    return;
  }

  const formData = new FormData();
  formData.append("jobTitle", jobTitle);
  formData.append("jobSummary", jobSummary);
  formData.append("jobStatus", jobStatus);
  formData.append("jobDescriptionFile", file);

  try {
    await createJob(formData);
    alert("Job created!");
    navigate("/app/jobs");
  } catch (error) {
    alert("Failed to create job");
  }
};

  return (
    <Container className="mt-4">
      <h3>Create Job</h3>

      <Form>
        <Form.Group className="mb-3">
          <Form.Label>Job Title *</Form.Label>
          <Form.Control
            value={jobTitle}
            onChange={(e) => setJobTitle(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Job Summary *</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            value={jobSummary}
            onChange={(e) => setJobSummary(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Status</Form.Label>
          <Form.Select
            value={jobStatus}
            onChange={(e) => setJobStatus(e.target.value)}
          >
            <option value="OPEN">OPEN</option>
            <option value="CLOSED">CLOSED</option>
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Upload Job Description *</Form.Label>
          <Form.Control
            type="file"
            accept=".pdf,.doc,.docx"
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
              if (e.target.files && e.target.files.length > 0) {
                setFile(e.target.files[0]);
              }
            }}
          />
        </Form.Group>

        <Button onClick={handleSubmit}>Create Job</Button>
      </Form>
    </Container>
  );
};

export default CreateJob;