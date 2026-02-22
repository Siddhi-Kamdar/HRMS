import axios from "../services/axiosInstance";

export const getHierarchy = async (empId: number) => {
  const response = await axios.get(
    `/api/hierarchy/${empId}/hierarchy`
  );

  return response.data;
};