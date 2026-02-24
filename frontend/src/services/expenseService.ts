import axiosInstance from "./axiosInstance";

export interface ExpenseRequest {
  travelId: number;
  expenseTypeId: number;
  amount: number;
  comment?: string;
  proofUrl: string;
}
export interface Expense{
  expenseId: number,
  employeeName: string,
  destination: string,
  amount: number,
  status: string,
  remark: string | null,
  expenseDate: string,
  proofUrl: string,
  travelId:number
}

export const createExpense = async (
  travelId: number,
  amount: number,
  comment: string,
  file: File
) => {

  const formData = new FormData();

  formData.append("travelId", travelId.toString());
  formData.append("expenseTypeId", "1");
  formData.append("amount", amount.toString());
  formData.append("comment", comment);
  formData.append("file", file);

  await axiosInstance.post(
    "/api/expenses",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }
  );
};

export const getMyExpenses = async () =>
  (await axiosInstance.get("/api/expenses/my")).data;

export const getAllExpenses = async () =>
  (await axiosInstance.get("/api/expenses")).data;

export const getTeamExpenses = async () =>
  (await axiosInstance.get("/api/expenses/team")).data;

export const uploadExpenseProof = async (
  file: File
): Promise<string> => {

  const formData = new FormData();
  formData.append("file", file);

  const response = await axiosInstance.post(
    "/api/expenses/upload-proof",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }
  );

  return response.data;
};

export const approveExpense = async (id:number, hrId: number) =>
  axiosInstance.put(`/api/expenses/${id}/approve`, null, {params:{hrId}});

export const rejectExpense = async (
  id:number,
  hrId: number,
  remark:string
) =>
  axiosInstance.put(
    `/api/expenses/${id}/reject`,
    null,
    { params:{ hrId, remark } }
  );
