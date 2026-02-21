import axiosInstance from "./axiosInstance";


export interface Travel {
  travelId: number;
  employeeNames: string[];
  destination: string;
  departDate: string;
  returnDate: string;
  employeeIds: number[];
}

export interface CreateTravelRequest {
  schedulerId: number;
  employeeIds: number[];
  destination: string;
  departDate: string;
  returnDate: string;
}

export const getTravels = async (): Promise<Travel[]> => {
  const response = await axiosInstance.get("/api/travel");
  return response.data;
};

export const createTravel = async (
  data: CreateTravelRequest
): Promise<void> => {
  await axiosInstance.post("/api/travel", data);
};

export const getTravelById = async (
  travelId: string
): Promise<Travel> => {
  const response = await axiosInstance.get(`/api/travel/${travelId}`);
  return response.data;
};