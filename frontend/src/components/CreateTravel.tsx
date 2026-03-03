import React, { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { createTravel } from "../services/travelService";
import { getEmployees, type Employee } from "../services/employeeService";

const TravelCreate: React.FC = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selectedEmployees, setSelectedEmployees] = useState<number[]>([]);
  const [destination, setDestination] = useState("");
  const [departDate, setDepartDate] = useState("");
  const [returnDate, setReturnDate] = useState("");

  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    loadEmployees();
  }, []);

  const loadEmployees = async () => {
    const data = await getEmployees();
    setEmployees(data);
  };

  const handleEmployeeChange = (id: number) => {
    setSelectedEmployees((prev) =>
      prev.includes(id)
        ? prev.filter((empId) => empId !== id)
        : [...prev, id]
    );
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!destination.trim()) {
      newErrors.destination = "Destination is required";
    } else if (destination.length < 3) {
      newErrors.destination = "Destination must be at least 3 characters";
    }

    if (!departDate) {
      newErrors.departDate = "Departure date is required";
    }

    if (!returnDate) {
      newErrors.returnDate = "Return date is required";
    }

    if (departDate && returnDate && returnDate < departDate) {
      newErrors.returnDate = "Return date cannot be before departure date";
    }

    if (selectedEmployees.length === 0) {
      newErrors.employees = "Please select at least one employee";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    if (!validateForm()) return;

    try {
      await createTravel({
        schedulerId: user.employeeId,
        employeeIds: selectedEmployees,
        destination,
        departDate,
        returnDate
      });

      navigate("/app/travel");
    } catch (error) {
      setErrors({ api: "Failed to create travel. Please try again." });
    }
  };

  return (
    <div className="card p-4 shadow-sm" style={{ maxWidth: "600px" }}>
      <h4 className="mb-4">Create Travel</h4>

      {errors.api && (
        <div className="alert alert-danger">{errors.api}</div>
      )}

      <form onSubmit={handleSubmit} noValidate>

        <div className="mb-3">
          <label className="form-label">Destination</label>
          <input
            type="text"
            className={`form-control ${errors.destination ? "is-invalid" : ""}`}
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
          />
          {errors.destination && (
            <div className="invalid-feedback">{errors.destination}</div>
          )}
        </div>

        <div className="mb-3">
          <label className="form-label">Departure Date</label>
          <input
            type="date"
            className={`form-control ${errors.departDate ? "is-invalid" : ""}`}
            value={departDate}
            min={new Date().toISOString().split("T")[0]}
            onChange={(e) => setDepartDate(e.target.value)}
          />
          {errors.departDate && (
            <div className="invalid-feedback">{errors.departDate}</div>
          )}
        </div>

        <div className="mb-3">
          <label className="form-label">Return Date</label>
          <input
            type="date"
            className={`form-control ${errors.returnDate ? "is-invalid" : ""}`}
            value={returnDate}
            min={departDate || new Date().toISOString().split("T")[0]}
            onChange={(e) => setReturnDate(e.target.value)}
          />
          {errors.returnDate && (
            <div className="invalid-feedback">{errors.returnDate}</div>
          )}
        </div>

        <div className="mb-3">
          <label className="form-label">Select Employees</label>
          <div
            className={`border rounded p-2 ${
              errors.employees ? "border-danger" : ""
            }`}
            style={{ maxHeight: "150px", overflowY: "auto" }}
          >
            {employees.map((emp) => (
              <div key={emp.employeeId} className="form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  checked={selectedEmployees.includes(emp.employeeId)}
                  onChange={() => handleEmployeeChange(emp.employeeId)}
                />
                <label className="form-check-label">
                  {emp.fullName}
                </label>
              </div>
            ))}
          </div>
          {errors.employees && (
            <div className="text-danger mt-1">{errors.employees}</div>
          )}
        </div>

        <button type="submit" className="btn btn-success w-100">
          Create Travel
        </button>
      </form>
    </div>
  );
};

export default TravelCreate;