/* eslint-disable react-hooks/set-state-in-effect */
import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Modal, Button, Form } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";
import validator from "validator";

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
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [search, setSearch] = useState("");

    const user = JSON.parse(localStorage.getItem("user") || "{}");

    const loadJobs = async () => {
        const data = await getJobs();
        setJobs(data);
    }

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
        if (isSubmitting) return;
        setIsSubmitting(true);
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
            console.log(error);
            alert("Referral failed");
        }
    };

    const handleShare = async (jobId: number) => {
        const email = prompt("Enter email address");
        if (!email) return;
        if (validator.isEmail(email)) {
            try {
                await shareJob(jobId, [email]);
                alert("Job shared successfully!");
            } catch (error) {
                console.log(error);
                alert("Failed to share job");
            }
        }
        else {
            alert("Enter valid Email")
        }
    };

    const filteredJobs = jobs.filter((job) => {
        const term = search.toLowerCase();

        return (
            job.jobTitle.toLowerCase().includes(term) ||
            job.jobSummary.toLowerCase().includes(term) ||
            job.jobStatus.toLowerCase().includes(term)
        );
    });
    return (
        <Container fluid style={{ marginTop: "20px" }}>
            <div className="d-flex justify-content-between align-items-center mb-3">

                {user.role === "HR" && (
                    <div className="d-flex gap-2">
                        <button
                            className="btn btn-outline-success edge"
                            onClick={() => navigate("/app/jobs/create")}
                        >
                            + Create New Job
                        </button>

                        <button
                            className="btn btn-outline-dark edge"
                            onClick={() => navigate("/app/hr/referrals")}
                        >
                            View Referrals
                        </button>
                    </div>
                )}

                <input
                    type="text"
                    placeholder="Search jobs..."
                    className="form-control edge"
                    style={{ maxWidth: "300px" }}
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

            </div>
            <Row className="g-4">
                {
                    filteredJobs.map(job => (
                        <Col key={job.jobId} sm={12} md={6} lg={4}>
                            <Card className="h-100 edge shadow-sm">
                                <Card.Body className="d-flex flex-column">

                                    <div className="mb-2 text-muted small">
                                        Job ID: {job.jobId}
                                    </div>

                                    <h5 className="mb-2">{job.jobTitle}</h5>

                                    <p className="text-muted small mb-3">
                                        {job.jobSummary}
                                    </p>

                                    <div className="mb-3">
                                        <span className="me-2">Status:</span>
                                        <span
                                            className={`badge edge ${job.jobStatus === "OPEN" ? "bg-success" : "bg-secondary"
                                                }`}
                                        >
                                            {job.jobStatus}
                                        </span>
                                    </div>

                                    <a
                                        href={`http://localhost:8080/${job.jobDescriptionUrl}`}
                                        target="_blank"
                                        className="mb-3 text-decoration-none"
                                    >
                                        View Job Description
                                    </a>

                                    <div className="mt-auto d-flex justify-content-between align-items-center">

                                        <div className="d-flex gap-2">
                                            {
                                                job.jobStatus === "OPEN" && (
                                                    <button
                                                        className="btn btn-sm btn-outline-primary edge"
                                                        onClick={() => handleShare(job.jobId)}
                                                    >
                                                        Share
                                                    </button>
                                                )
                                            }

                                            {
                                                job.jobStatus === "OPEN" && (
                                                    <button
                                                        className="btn btn-sm btn-outline-warning edge"
                                                        onClick={() => handleOpenModal(job.jobId)}
                                                    >
                                                        Refer
                                                    </button>
                                                )
                                            }
                                        </div>

                                        {user.role === "HR" && (
                                            <button
                                                className="btn btn-sm btn-outline-secondary edge"
                                                onClick={() => navigate(`${job.jobId}/edit`)}
                                            >
                                                Edit
                                            </button>
                                        )}
                                    </div>

                                </Card.Body>
                            </Card>
                        </Col>
                    ))
                }
            </Row>
            <Modal show={showModal} onHide={handleCloseModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Refer Someone</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Candidate Name *</Form.Label>
                            <Form.Control
                                type="text"
                                value={candidateName}
                                onChange={(e) => setCandidateName(e.target.value)}
                                className="edge"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Candidate Email *</Form.Label>
                            <Form.Control
                                type="email"
                                value={candidateEmail}
                                onChange={(e) => setCandidateEmail(e.target.value)}
                                className="edge"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Short Note</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                value={shortNote}
                                onChange={(e) => setShortNote(e.target.value)}
                                className="edge"
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
                                className="edge"
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" className="edge" onClick={handleCloseModal}>
                        Cancel
                    </Button>
                    <Button
                        variant="primary"
                        className="edge"
                        onClick={handleSubmitReferral}
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? "Submitting..." : "Submit Referral"}
                    </Button>
                </Modal.Footer>
            </Modal>

        </Container>
    );
}
export default JobDisplay;