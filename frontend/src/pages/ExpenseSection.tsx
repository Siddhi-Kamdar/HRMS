
import React, { useEffect, useState } from "react";
import {
  uploadExpenseProof,
  createExpense,
  getMyExpenses,
  type Expense
} from "../services/expenseService";

interface Props {
  travelId: string;
}

const ExpenseSection: React.FC<Props> = ({ travelId }) => {

  const [file,setFile]=useState<File|null>(null);
  const [amount,setAmount]=useState("");
  const [comment,setComment]=useState("");
  const [expenses,setExpenses]=useState<Expense[]>([]);
  const [loading,setLoading]=useState(false);

  useEffect(()=>{
    loadExpenses();
  },[]);

  const loadExpenses=async()=>{
    const data=await getMyExpenses();
    setExpenses(data);
  };

  const handleSubmit=async()=>{

    if(!file || !amount) return;

    setLoading(true);

    const proofUrl=
      await uploadExpenseProof(file);

    await createExpense({
      travelId:Number(travelId),
      expenseTypeId:1,
      amount:Number(amount),
      proofUrl,
      comment
    });

    setFile(null);
    setAmount("");
    setComment("");

    await loadExpenses();

    setLoading(false);
  };

  return(

    <div>

      <h5>Add Expense</h5>

      <input
        type="number"
        className="form-control mb-2"
        placeholder="Amount"
        value={amount}
        onChange={e=>setAmount(e.target.value)}
      />

      <textarea
        className="form-control mb-2"
        placeholder="Comment"
        value={comment}
        onChange={e=>setComment(e.target.value)}
      />

      <input
        type="file"
        className="form-control mb-2"
        onChange={e=>
          setFile(e.target.files?.[0]||null)}
      />

      <button
        className="btn btn-success"
        disabled={loading}
        onClick={handleSubmit}
      >
        Submit Expense
      </button>

      <hr/>

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