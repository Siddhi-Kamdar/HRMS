import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  getEmployees,
  applyForSlot,
  type Employee
} from "../services/gameService";

const BookingForm: React.FC = () => {

  const { slotId } = useParams();
  const navigate = useNavigate();

  const employeeId = Number(localStorage.getItem("employeeId"));

  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selectedMembers, setSelectedMembers] = useState<number[]>([]);

  useEffect(() => {
    loadEmployees();
  }, []);

  const loadEmployees = async () => {
    const data = await getEmployees();
    setEmployees(data);
  };

  const handleMemberToggle = (id: number) => {
    setSelectedMembers(prev =>
      prev.includes(id)
        ? prev.filter(m => m !== id)
        : [...prev, id]
    );
  };

  const handleSubmit = async () => {

    const members = [...new Set([employeeId, ...selectedMembers])];

    try {
      await applyForSlot({
        slotId: Number(slotId),
        leaderEmpId: employeeId,
        members
      });

      alert("Applied successfully");
      navigate("/games");

    } catch (err: any) {
      alert(err.response?.data?.message || "Failed to apply");
    }
  };

  return (
    <div style={{ padding: "40px" }}>
      <h2>Book Slot</h2>

      <p>Leader: You</p>

      <h5>Select Team Members</h5>

      <div style={{ maxHeight: "300px", overflowY: "auto" }}>
        {employees.map(emp => (
          <div key={emp.employeeId}>
            <input
              type="checkbox"
              checked={selectedMembers.includes(emp.employeeId)}
              onChange={() => handleMemberToggle(emp.employeeId)}
            />
            {" "}
            {emp.fullName}
          </div>
        ))}
      </div>

      <button
        className="btn btn-primary mt-3"
        onClick={handleSubmit}
      >
        Submit
      </button>
    </div>
  );
};

export default BookingForm;