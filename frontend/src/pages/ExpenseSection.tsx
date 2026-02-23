
import React, { useEffect, useState } from "react";
import {
  getMyExpenses,
  type Expense
} from "../services/expenseService";

interface Props {
  travelId: string;
}

const ExpenseSection: React.FC<Props> = ({ travelId }) => {

  const [expenses,setExpenses]=useState<Expense[]>([]);

  useEffect(()=>{
    loadExpenses();
  },[]);

  const loadExpenses=async()=>{
    const data=await getMyExpenses();
    setExpenses(data);
    console.log(expenses);
  };

  return(

    <div>

      <h5>My Expenses</h5>

      {expenses.map(exp=>(
        <div
          key={exp.expenseId}
          className="border rounded p-2 mb-2"
        >

          <div>
            <strong>Amount:</strong> {exp.amount}
          </div>

          <div>
            <strong>Status:</strong> {exp.status}
          </div>

          {exp.remark && (
            <div>
              <strong>Remark:</strong> {exp.remark}
            </div>
          )}

          <a
            href={`http://localhost:8080/${exp.proofUrl}`}
            target="_blank"
          >
            View Proof
          </a>

        </div>
      ))}

    </div>
  );
};

export default ExpenseSection;