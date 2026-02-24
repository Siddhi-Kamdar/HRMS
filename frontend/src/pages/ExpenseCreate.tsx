
import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { createExpense, uploadExpenseProof } from "../services/expenseService";


const ExpenseCreate: React.FC = () => {

    const { travelId } = useParams();
    const navigate = useNavigate();

    const [amount, setAmount] = useState("");
    const [comment, setComment] = useState("");
    const [proofUrl, setProofUrl] = useState("");
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [uploading, setUploading] = useState(false);



    const submitExpense = async (e:any) => {
        e.preventDefault();
        try{
        if (!selectedFile) {
            alert("Upload receipt");
            return;
        }
        console.log("1");
        await createExpense(
            Number(travelId),
            Number(amount),
            comment,
            selectedFile
        );
        console.log("2");

        alert("Expense submitted");
        setTimeout(()=>{
            console.log("3");
            navigate("/app/travel");},10);
    } catch(error){
        console.error("expense failed:", error);
        alert(error)
    }
    };

    return (
        <div className="card p-4 shadow-sm">

            <h4>Add Expense</h4>

            <input
                className="form-control mb-2"
                placeholder="Amount"
                type="number"
                onChange={(e) => setAmount(e.target.value)}
            />

            <textarea
                className="form-control mb-2"
                placeholder="Comment"
                onChange={(e) => setComment(e.target.value)}
            />


            <input
                type="file"
                className="form-control mb-2"
                onChange={(e) =>
                    setSelectedFile(e.target.files?.[0] || null)
                }
            />

            <button
                className="btn btn-success"
                onClick={submitExpense}
                disabled={uploading}
            >
                {uploading ? "Submitting..." : "Submit Expense"}
            </button>
        </div>
    );
};

export default ExpenseCreate;
