import React, { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { NotificationDialog } from "./NotificationDialog";

const AppLayout: React.FC = () => {
  const navigate = useNavigate();
  const [showDialog, setShowDialog] = useState(false);

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const modules = [
    { label: "Travel", route: "travel" },
    { label: "Achievements", route: "achievements" },
    { label: "Games", route: "games" },
    { label: "Jobs", route: "jobs" },
    ...(user.role === "HR" ? [{ label: "Expenses", route: "expenses" }] : [])
  ];

  return (
    <div style={{ minHeight: "100vh", backgroundColor: "#f4f6f9" }}>

      <nav className="navbar navbar-expand bg-white shadow-sm px-4">
        <span className="navbar-brand fw-bold">HRMS</span>


        <div className="ms-auto d-flex align-items-center gap-3">
          <button
            onClick={() => navigate(`org-chart/${user.employeeId}`)}
            style={{ border: "none", background: "transparent" }}
            title="View Organization Chart"
          >
            <div
              className="rounded-circle bg-success text-white d-flex justify-content-center align-items-center"
              style={{ width: "36px", height: "36px" }}
            >
              {user?.fullName?.charAt(0) || "U"}
            </div>
          </button>

          <span className="fw-semibold">
            {user?.fullName || "User"}
          </span>

        <div>
        <button onClick={() => setShowDialog(true)} className="btn">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" className="bi bi-bell" viewBox="0 0 16 16">
            <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2M8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5 5 0 0 1 13 6c0 .88.32 4.2 1.22 6" />
          </svg>
          </button>
          {showDialog && (
        <NotificationDialog
          onClose={() => setShowDialog(false)}
        />
      )}
          </div>
          <button
            className="btn btn-outline-danger btn-sm"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </nav>

      <div className="bg-white border-bottom">
        <div className="container-fluid d-flex gap-4 px-5 py-3">
          {modules.map((module) => (
            <NavLink
              key={module.route}
              to={module.route}
              className={({ isActive }) =>
                `text-decoration-none fw-semibold pb-2 ${isActive
                  ? "text-success border-bottom border-2 border-success"
                  : "text-dark"
                }`
              }
            >
              {module.label}
            </NavLink>
          ))}
        </div>
      </div>

      <div className="container-fluid px-5 py-4">
        <Outlet />
      </div>

    </div>
    
  );
};

export default AppLayout;