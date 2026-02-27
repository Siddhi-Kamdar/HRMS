import { useEffect, useState } from "react";
import { Container, Table, Button, Form } from "react-bootstrap";
import { getAllReferrals,updateReferralStatus } from "../services/referralService";

const ReferralDashboard = () => {
  const [referrals, setReferrals] = useState<any[]>([]);
  const [statusMap, setStatusMap] = useState<{ [key: number]: number }>({});

  useEffect(() => {
    loadReferrals();
  }, []);

  const loadReferrals = async () => {
    const data = await getAllReferrals();
    setReferrals(data);
    console.log(referrals);
  };

  const handleStatusChange = (referralId: number, statusId: number) => {
    setStatusMap({ ...statusMap, [referralId]: statusId });
  };

  const handleUpdate = async (referralId: number) => {
    const statusId = statusMap[referralId];
    if (!statusId) return;

    await updateReferralStatus(referralId, statusId);
    alert("Status updated!");
    loadReferrals();
  };

  return (
    <Container className="mt-4">
      <h3>Referral Dashboard</h3>

      <Table bordered hover>
        <thead>
          <tr>
            <th>Candidate</th>
            <th>Job</th>
            <th>Referred By</th>
            <th>Current Status</th>
            <th>Change Status</th>
          </tr>
        </thead>
        <tbody>
          {referrals.map((ref) => (
            <tr key={ref.referralId}>
              <td>{ref.candidateName}</td>
              <td>{ref.jobTitle}</td>
              <td>{ref.referredBy}</td>
              <td>{ref.currentStatus}</td>
              <td>
                <Form.Select
                  onChange={(e) =>
                    handleStatusChange(
                      ref.referralId,
                      Number(e.target.value)
                    )
                  }
                >
                  <option value="">Select</option>
                  <option value="1002">In Review</option>
                  <option value="1003">Shortlisted</option>
                  <option value="2">Rejected</option>
                  <option value="1004">Selected</option>
                </Form.Select>
                <Button
                  className="mt-2"
                  onClick={() => handleUpdate(ref.referralId)}
                >
                  Update
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default ReferralDashboard;