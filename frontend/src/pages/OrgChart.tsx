import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getHierarchy } from "../services/hierarchyService";

const OrgChart: React.FC = () => {

  const { empId } = useParams();

  const [managerData, setManagerData] = useState<any[]>([]);
  const [directReports, setDirectReports] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (empId) loadHierarchy(Number(empId));
  }, [empId]);

  const loadHierarchy = async (id: number) => {
    try {
      const res = await getHierarchy(id);

      setManagerData(res.managerChain || res);
      setDirectReports(res.directReports || []);

    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;


  const managers: any[] = [];

  const lastRow =
    managerData[managerData.length - 1];

  if (lastRow?.supervisiorName) {
    managers.push({
      name: lastRow.supervisiorName,
      position: lastRow.supervisiorPosition,
      picture: lastRow.supervisiorProfilePicture
    });
  }

  managerData.forEach((row: any) => {
    managers.push({
      name: row.employeeName,
      position: row.employeePosition,
      picture: row.employeeProfilePicture
    });
  });

  managers.reverse();

  const selectedEmployee =
    managers[managers.length - 1];

  const managerChain =
    managers.slice(0, managers.length - 1);


  return (
    <div className="d-flex flex-column align-items-center">

      <h4 className="mb-5">Organization Chart</h4>

      {managerChain.map((emp, index) => (
        <React.Fragment key={index}>
          <EmployeeCard emp={emp} />
          <Connector />
        </React.Fragment>
      ))}

      {selectedEmployee && (
        <>
          <EmployeeCard emp={selectedEmployee} highlight />
          <Connector />
        </>
      )}

      {directReports.length > 0 && (
        <div className="d-flex gap-4 flex-wrap justify-content-center">
          {directReports.map((emp, i) => (
            <EmployeeCard
              key={i}
              emp={{
                name: emp.name,
                position: emp.designation,
                picture: emp.profilePicture
              }}
            />
          ))}
        </div>
      )}

    </div>
  );
};


const EmployeeCard = ({ emp, highlight = false }: any) => (
  <div
    className={`card text-center shadow-sm p-3 ${
      highlight ? "border-success border-2" : ""
    }`}
    style={{ width: "220px" }}
  >
    <img
      src={emp.picture || "/avatar.png"}
      className="rounded-circle mx-auto"
      width={70}
      height={70}
    />
    <div className="fw-semibold mt-2">{emp.name}</div>
    <small>{emp.position}</small>
  </div>
);

const Connector = () => (
  <div
    style={{
      width: "2px",
      height: "40px",
      backgroundColor: "#ccc"
    }}
  />
);

export default OrgChart;