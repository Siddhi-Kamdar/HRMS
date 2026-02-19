import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card } from 'react-bootstrap';
import { NavLink, useNavigate } from "react-router-dom";

import {
    getJobs,
    type Job
} from "../services/jobService";

const JobDisplay: React.FC = () => {
    const navigate = useNavigate();
    const [jobs, setJobs] = useState<Job[]>([]);
    const user = JSON.parse(localStorage.getItem("user") || "{}");
    useEffect(() => {
        loadJobs();
    }, []);

    const loadJobs = async () => {
        const data = await getJobs();
        setJobs(data);
    }

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
                                        Share 
                                    </Card.Footer>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))
                }
            </Row>
        </Container>
    );
}
export default JobDisplay;