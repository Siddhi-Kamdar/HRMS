
import React, { useEffect, useState } from "react";
import {
  getMyExpenses,
  getAllExpenses,
  getTeamExpenses,
  approveExpense,
  rejectExpense
} from "../services/expenseService";

const ExpenseDashboard:React.FC = () => {

  const user =
    JSON.parse(localStorage.getItem("user")||"{}");

  const [expenses,setExpenses] = useState<any[]>([]);

  useEffect(()=>{
    loadExpenses();
  },[]);

  const loadExpenses = async () => {

    let data;

    if(user.role==="HR")
      data = await getAllExpenses();
    else if(user.role==="MANAGER")
      data = await getTeamExpenses();
    else
      data = await getMyExpenses();

    setExpenses(data);
  };

  const approve = async(id:number)=>{
    await approveExpense(id, user.employeeId);
    loadExpenses();
  };

  const reject = async(id:number)=>{
    const remark =
      prompt("Reject reason") || "";

    await rejectExpense(id,user.employeeId,remark);
    loadExpenses();
  };

  return (

    <div className="card p-4">

      <h4>Expense Dashboard</h4>

      <table className="table">

        <thead>
          <tr>
            <th>Employee</th>
            <th>Travel</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Proof</th>
            {user.role==="HR" && <th>Action</th>}
          </tr>
        </thead>

        <tbody>

        {expenses.map(exp=>(
          <tr key={exp.expenseId}>

            <td>{exp.employeeName}</td>
            <td>{exp.destination}</td>
            <td>₹{exp.amount}</td>

            <td>
              <span className={
                exp.status==="APPROVED"
                ?"badge bg-success":
                exp.status==="REJECTED"
                ?"badge bg-danger":
                "badge bg-warning"
              }>
                {exp.status}
              </span>
            </td>

            <td>
              <a
                href={`http://localhost:8080/${exp.proofUrl}`}
                target="_blank"
              >
                View
              </a>
            </td>

            {user.role==="HR" && (
              <td>
                <button
                  className="btn btn-success btn-sm me-2"
                  onClick={()=>approve(exp.expenseId)}
                >
                  Approve
                </button>

                <button
                  className="btn btn-danger btn-sm"
                  onClick={()=>reject(exp.expenseId)}
                >
                  Reject
                </button>
              </td>
            )}

          </tr>
        ))}

        </tbody>
      </table>

    </div>
  );
};

export default ExpenseDashboard;