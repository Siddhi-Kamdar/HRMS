/* eslint-disable @typescript-eslint/no-explicit-any */
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
  const [errors, setErrors] = useState<string | null>(null);

  useEffect(() => {
    loadEmployees();
  }, []);

  const loadEmployees = async () => {
    try {
      const data = await getEmployees();
      setEmployees(data);
    } catch {
      setErrors("Failed to load employees. Please refresh the page.");
    }
  };

  const handleMemberToggle = (id: number) => {
    setSelectedMembers(prev =>
      prev.includes(id)
        ? prev.filter(m => m !== id)
        : [...prev, id]
    );
  };

  const validateForm = () => {
    if (!employeeId) {
      setErrors("Invalid user session. Please login again.");
      return false;
    }

    const totalMembers = [...new Set([employeeId, ...selectedMembers])];

    if (totalMembers.length < 1) {
      setErrors("At least one team member is required.");
      return false;
    }

    return true;
  };

  const handleSubmit = async () => {
    setErrors(null);

    if (!validateForm()) return;

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
      const message =
        err.response?.data?.message ||
        err.response?.data ||
        "Failed to apply for slot. Please try again.";

      setErrors(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-lg-6 col-md-8">
        <div className="card edge shadow-sm">

          <div className="card-header bg-white">
            <h5 className="mb-0">Book Slot #{slotId}</h5>
          </div>

          <div className="card-body">

            {errors && (
              <div className="alert alert-danger alert-dismissible fade show">
                <div className="fw-semibold">Error</div>
                <div>{errors}</div>
                <button
                  type="button"
                  className="btn-close edge"
                  onClick={() => setErrors(null)}
                ></button>
              </div>
            )}

            <div className="mb-3">
              <label className="form-label fw-semibold">Leader</label>
              <input
                type="text"
                className="form-control edge"
                value={user?.fullName || "You"}
                disabled
              />
            </div>

            <div className="mb-3">
              <label className="form-label fw-semibold">
                Select Team Members
              </label>

              <div
                className="border edge p-3"
                style={{ maxHeight: "250px", overflowY: "auto" }}
              >
                {employees.map(emp => (
                  <div className="form-check" key={emp.employeeId}>
                    <input
                      className="form-check-input edge"
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
                className="btn btn-outline-secondary edge"
                onClick={() => navigate("/app/games")}
                disabled={loading}
              >
                Cancel
              </button>

              <button
                className="btn btn-success edge"
                onClick={handleSubmit}
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span
                      className="spinner-border spinner-border-sm me-2"
                      role="status"
                    ></span>
                    Applying...
                  </>
                ) : (
                  "Apply"
                )}
              </button>
            </div>

          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingForm;