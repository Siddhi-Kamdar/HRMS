import React, { useEffect, useState } from "react";
import {Container, Row, Col, Card} from 'react-bootstrap';

import {
    getJobs,
    type Job
} from "../services/jobService";

const JobDisplay: React.FC = () => {

    const [jobs, setJobs] = useState<Job[]>([]);
    useEffect(() => {
        loadJobs();
    }, []);

    const loadJobs = async () => {
        const data = await getJobs();
        setJobs(data);
    }

    return (
        <Container fluid style={{ marginTop: "20px"}}>
            <Row className="gy-3">
            {
                jobs.map(job  => (
                    <Col key= {job.jobId} sm={12} md={6} lg={4}>
                         <Card style={{ width: '22rem' }}>
                        <Card.Body>
                            <Card.Header>{job.jobId}</Card.Header>
                             <Card.Text>Job Title : {job.jobTitle}</Card.Text>
                             <Card.Text>Job Summary: {job.jobSummary}</Card.Text>
                             <Card.Text>Job Status: {job.jobStatus}</Card.Text>
                            <a href="#" className="btn btn-success"><i className="bi bi-share"/>Share</a>
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