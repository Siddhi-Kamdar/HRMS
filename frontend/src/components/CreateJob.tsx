import React, { useState, useEffect } from "react";
import { Container, Form, Button } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import { createJob, updateJob, getJobById } from "../services/jobService";

const CreateJob = () => {
  const navigate = useNavigate();

  const { jobId } = useParams();
  const isEditMode = !!jobId;
  const [jobTitle, setJobTitle] = useState("");
  const [jobSummary, setJobSummary] = useState("");
  const [jobStatus, setJobStatus] = useState("OPEN");
  const [file, setFile] = useState<File | null>(null);

  useEffect(() => {
    if (isEditMode) {
      loadJob();
    }
  }, []);

  const loadJob = async () => {
    try {
      const job = await getJobById(Number(jobId));

      setJobTitle(job.jobTitle);
      setJobSummary(job.jobSummary);
      setJobStatus(job.jobStatus);
    } catch {
      alert("Failed to load job");
    }
  };
  const handleSubmit = async () => {

    if (!jobTitle || !jobSummary) {
      alert("Fill all required fields");
      return;
    }

    const formData = new FormData();
    formData.append("jobTitle", jobTitle);
    formData.append("jobSummary", jobSummary);
    formData.append("jobStatus", jobStatus);

    if (file) {
      formData.append("jobDescriptionFile", file);
    }

    try {

      if (isEditMode) {
        await updateJob(Number(jobId), formData);
        alert("Job updated!");
      } else {
        if (!file) {
          alert("Upload job description");
          return;
        }

        await createJob(formData);
        alert("Job created!");
      }

      navigate("/app/jobs");

    } catch (error) {
      alert("Operation failed");
    }
  };

  return (
    <Container className="mt-4">
      <h3>{isEditMode ? "Edit Job" : "Create Job"}</h3>

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

        <Button onClick={handleSubmit}>
          {isEditMode ? "Update Job" : "Create Job"}
        </Button>
      </Form>
    </Container>
  );
};

export default CreateJob;