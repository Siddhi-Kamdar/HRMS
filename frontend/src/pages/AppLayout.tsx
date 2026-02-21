import React from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";

const AppLayout: React.FC = () => {
  const navigate = useNavigate();

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
    ...(user.role === "HR" ? [{label: "Expenses", route: "expenses"}] : [])
  ];

  return (
    <div style={{ minHeight: "100vh", backgroundColor: "#f4f6f9" }}>

      <nav className="navbar navbar-expand bg-white shadow-sm px-4">
        <span className="navbar-brand fw-bold">HRMS</span>

        <div className="ms-auto d-flex align-items-center gap-3">
          <div
            className="rounded-circle bg-success text-white d-flex justify-content-center align-items-center"
            style={{ width: "36px", height: "36px" }}
          >
            {user?.fullName?.charAt(0) || "U"}
          </div>

          <span className="fw-semibold">
            {user?.fullName || "User"}
          </span>

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
                `text-decoration-none fw-semibold pb-2 ${
                  isActive
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