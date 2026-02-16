import React from "react";
import { useNavigate } from "react-router-dom";

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  const modules = [
    {
      title: "Travel & Expenses",
      description: "View assigned travels and manage expense claims.",
      route: "/travel"
    },
    {
      title: "Achievements",
      description: "Post and view achievements and celebrations.",
      route: "/achievements"
    },
    {
      title: "Games Scheduling",
      description: "Book and manage game slots.",
      route: "/games"
    },
    {
      title: "Jobs & Referrals",
      description: "Browse job openings and refer candidates.",
      route: "/jobs"
    }
  ];

  return (
    <div>

      <nav className="navbar navbar-light bg-white shadow-sm px-4">
        <span className="navbar-brand mb-0 h5">HRMS Dashboard</span>

        <div className="d-flex align-items-center">
          <div
            className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center me-3"
            style={{ width: "35px", height: "35px" }}
          >
            U
          </div>

          <button
            className="btn btn-outline-danger btn-sm"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </nav>


      <div className="container mt-4">
        <div className="row">
          {modules.map((module, index) => (
            <div className="col-md-6 mb-4" key={index}>
              <div className="card h-100 shadow-sm">
                <div className="card-body d-flex flex-column">
                  <h5 className="card-title">{module.title}</h5>
                  <p className="card-text">{module.description}</p>
                  <button
                    className="btn btn-primary mt-auto"
                    onClick={() => navigate(module.route)}
                  >
                    Open
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

    </div>
  );
};

export default Dashboard;