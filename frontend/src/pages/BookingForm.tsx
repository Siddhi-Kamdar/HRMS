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

  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const employeeId = user?.employeeId;

  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selectedMembers, setSelectedMembers] = useState<number[]>([]);
  const [loading, setLoading] = useState(false);

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
      setLoading(true);

      await applyForSlot({
        slotId: Number(slotId),
        leaderEmpId: employeeId,
        members
      });

      navigate("/app/games");

    } catch (err: any) {
      const message = err.response?.data?.message|| err.response?.data || "Failed to apply"
      alert(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-lg-6 col-md-8">

        <div className="card shadow-sm">

          <div className="card-header bg-white">
            <h5 className="mb-0">Book Slot #{slotId}</h5>
          </div>

          <div className="card-body">

            <div className="mb-3">
              <label className="form-label fw-semibold">Leader</label>
              <input
                type="text"
                className="form-control"
                value={user?.fullName || "You"}
                disabled
              />
            </div>

            <div className="mb-3">
              <label className="form-label fw-semibold">
                Select Team Members
              </label>

              <div
                className="border rounded p-3"
                style={{ maxHeight: "250px", overflowY: "auto" }}
              >
                {employees.map(emp => (
                  <div className="form-check" key={emp.employeeId}>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      id={`emp-${emp.employeeId}`}
                      checked={selectedMembers.includes(emp.employeeId)}
                      onChange={() => handleMemberToggle(emp.employeeId)}
                      disabled={emp.employeeId === employeeId}
                    />
                    <label
                      className="form-check-label"
                      htmlFor={`emp-${emp.employeeId}`}
                    >
                      {emp.fullName}
                    </label>
                  </div>
                ))}
              </div>
            </div>

            <div className="d-flex justify-content-end gap-2">
              <button
                className="btn btn-outline-secondary"
                onClick={() => navigate("/app/games")}
              >
                Cancel
              </button>

              <button
                className="btn btn-success"
                onClick={handleSubmit}
                disabled={loading}
              >
                {loading ? "Applying..." : "Apply"}
              </button>
            </div>

          </div>
        </div>

      </div>
    </div>
  );
};

export default BookingForm;