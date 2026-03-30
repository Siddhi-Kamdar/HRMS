import axiosInstance from "./axiosInstance";

export interface Employee {
  employeeId: number;
  fullName: string;
  dateOfBirth: string;
}

export const getEmployees = async (): Promise<Employee[]> => {
  const response = await axiosInstance.get("/employees");
  return response.data;
};