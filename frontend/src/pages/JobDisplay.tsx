import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card } from 'react-bootstrap';
import { NavLink, useNavigate } from "react-router-dom";
import { Modal, Button, Form } from "react-bootstrap";

import {
    getJobs,
    type Job,
    shareJob
} from "../services/jobService";
import { referFriend } from "../services/referralService";

const JobDisplay: React.FC = () => {
    const navigate = useNavigate();
    const [jobs, setJobs] = useState<Job[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [selectedJobId, setSelectedJobId] = useState<number | null>(null);

    const [candidateName, setCandidateName] = useState("");
    const [candidateEmail, setCandidateEmail] = useState("");
    const [shortNote, setShortNote] = useState("");
    const [cvFile, setCvFile] = useState<File | null>(null);

    const user = JSON.parse(localStorage.getItem("user") || "{}");
    useEffect(() => {
        loadJobs();
    }, []);
    const handleOpenModal = (jobId: number) => {
        setSelectedJobId(jobId);
        setShowModal(true);
    };
    const handleCloseModal = () => {
        setShowModal(false);
        setCandidateName("");
        setCandidateEmail("");
        setShortNote("");
        setCvFile(null);
    };
    const handleSubmitReferral = async () => {
        if (!candidateName || !candidateEmail || !cvFile || !selectedJobId) {
            alert("Please fill all required fields");
            return;
        }

        const formData = new FormData();
        formData.append("candidateName", candidateName);
        formData.append("candidateEmail", candidateEmail);
        formData.append("shortNote", shortNote);
        formData.append("cvFile", cvFile);

        try {
            await referFriend(selectedJobId, formData);
            alert("Referral submitted successfully!");
            handleCloseModal();
        } catch (error) {
            alert("Referral failed");
        }
    };

    const loadJobs = async () => {
        const data = await getJobs();
        setJobs(data);
    }

    const handleShare = async (jobId: number) => {
        const email = prompt("Enter email address");
        if (!email) return;

        try {
            await shareJob(jobId, [email]);
            alert("Job shared successfully!");
        } catch (error) {
            alert("Failed to share job");
        }
    };

    const handleRefer = async (jobId: number) => {
        const candidateName = prompt("Candidate name");
        const candidateEmail = prompt("Candidate email");

        if (!candidateName || !candidateEmail) {
            alert("Name and Email required");
            return;
        }

        const fileInput = document.createElement("input");
        fileInput.type = "file";

        fileInput.onchange = async () => {
            const file = fileInput.files?.[0];
            if (!file) return;

            const formData = new FormData();
            formData.append("candidateName", candidateName);
            formData.append("candidateEmail", candidateEmail);
            formData.append("shortNote", "");
            formData.append("cvFile", file);

            try {
                await referFriend(jobId, formData);
                alert("Referral submitted!");
            } catch (error) {
                alert("Referral failed");
            }
        };

        fileInput.click();
    };
    return (
        <Container fluid style={{ marginTop: "20px" }}>
            {user.role === "HR" && (
                <div className="d-flex justify-content-end mb-3">
                    <button
                        className="btn btn-success"
                        onClick={() => navigate("")}
                    >
                        + Create New Job
                    </button>
                </div>
            )}
            <Row className="gy-3">
                {
                    jobs.map(job => (
                        <Col key={job.jobId} sm={12} md={6} lg={4}>
                            <Card style={{ width: '22rem' }}>
                                <Card.Body>
                                    <Card.Header>{job.jobId}</Card.Header>
                                    <Card.Text>Job Title : {job.jobTitle}</Card.Text>
                                    <Card.Text>Job Summary: {job.jobSummary}</Card.Text>
                                    <Card.Text>Job Status: {job.jobStatus}</Card.Text>
                                    <Card.Footer className="bg-white text-center">
                                        <Card.Footer className="bg-white text-center">
                                            <button
                                                className="btn btn-primary me-2"
                                                onClick={() => handleShare(job.jobId)}
                                            >
                                                Share Job
                                            </button>

                                            <button
                                                className="btn btn-warning"
                                                onClick={() => handleOpenModal(job.jobId)}
                                            >
                                                Refer Friend
                                            </button>
                                        </Card.Footer>
                                    </Card.Footer>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))
                }
            </Row>
            <Modal show={showModal} onHide={handleCloseModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Refer a Friend</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Candidate Name *</Form.Label>
                            <Form.Control
                                type="text"
                                value={candidateName}
                                onChange={(e) => setCandidateName(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Candidate Email *</Form.Label>
                            <Form.Control
                                type="email"
                                value={candidateEmail}
                                onChange={(e) => setCandidateEmail(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Short Note</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                value={shortNote}
                                onChange={(e) => setShortNote(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Upload CV *</Form.Label>
                            <Form.Control
                                type="file"
                                onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                                    if (e.target.files && e.target.files.length > 0) {
                                        setCvFile(e.target.files[0]);
                                    }
                                }}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleSubmitReferral}>
                        Submit Referral
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}
export default JobDisplay;