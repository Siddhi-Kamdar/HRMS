import axiosInstance from "./axiosInstance";

export interface Travel {
  travelId: number;
  fullName: string;
  destination: string;
  departDate: string;
  returnDate: string;
}

export const getTravels = async (): Promise<Travel[]> => {
  const response = await axiosInstance.get("/api/travel");
  return response.data;
};