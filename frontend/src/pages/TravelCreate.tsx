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

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    await createTravel({
      schedulerId: user.employeeId,
      employeeIds: selectedEmployees,
      destination,
      departDate,
      returnDate
    });

    navigate("/app/travel");
  };

  return (
    <div className="card p-4 shadow-sm" style={{ maxWidth: "600px" }}>
      <h4 className="mb-4">Create Travel</h4>

      <form onSubmit={handleSubmit}>

        <div className="mb-3">
          <label className="form-label">Destination</label>
          <input
            type="text"
            className="form-control"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Departure Date</label>
          <input
            type="date"
            className="form-control"
            value={departDate}
            onChange={(e) => setDepartDate(e.target.value)}
            min={new Date().toJSON().slice(0, 10)}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Return Date</label>
          <input
            type="date"
            className="form-control"
            value={returnDate}
            min={new Date().toJSON().slice(0, 10)}
            onChange={(e) => setReturnDate(e.target.value)}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Select Employees</label>
          <div className="border rounded p-2" style={{ maxHeight: "150px", overflowY: "auto" }}>
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
        </div>

        <button type="submit" className="btn btn-success w-100">
          Create Travel
        </button>
      </form>
    </div>
  );
};

export default TravelCreate;