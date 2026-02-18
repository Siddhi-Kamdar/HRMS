import React, { useEffect, useState } from "react";
import {Container, Row, Col, Card} from 'react-bootstrap';

import {
    getTravels,
    type Travel
} from "../services/travelService";

const TravelDisplay: React.FC = () => {

    const [travels, setTravels] = useState<Travel[]>([]);
    useEffect(() => {
        loadTravels();
    }, []);

    const loadTravels = async () => {
        const data = await getTravels();
        setTravels(data);
    }

    return (
        <Container fluid style={{ marginTop: "20px", backgroundColor: "lightblue" }}>
            <Row className="gy-4">
            {
                travels.map(travel => (
                    <Col sm={12} md={6} lg={4}>
                         <Card style={{ width: '18rem' }}>
                        <Card.Body>
                            <Card.Title>{travel.travelId}</Card.Title>
                             <Card.Text>Employee Name: {travel.fullName}</Card.Text>
                             <Card.Text>Travel Destination: {travel.destination}</Card.Text>
                             <Card.Text>Departure Date: {travel.departDate}</Card.Text>
                             <Card.Text>Return Date: {travel.returnDate}</Card.Text>
                            <a href="#" className="btn btn-primary">Detail</a>
                        </Card.Body>
                        </Card>
                    </Col>
                ))
            }
            </Row>
        </Container> 
    );
}
export default TravelDisplay;