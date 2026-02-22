import axiosInstance from "./axiosInstance";

export interface Game {
  gameId: number;
  gameName: string;
}

export type Slot = {
  slotId: number;
  slotDate: string;
  startTime: string;
  endTime: string;
  status: string;
  bookedBy?: string;
  mySlot?: boolean;
};
export interface Employee {
  employeeId: number;
  fullName: string;
}

export const getEmployees = async (): Promise<Employee[]> => {
  const response = await axiosInstance.get("/employees");
  return response.data;
};
export const getGames = async (): Promise<Game[]> => {
  const response = await axiosInstance.get("/api/games");
  return response.data;
};

export const getSlots = async (gameId: number, empId: number): Promise<Slot[]> => {
  const response = await axiosInstance.get(`/api/games/${gameId}/slots`, {params:{empId}});
  return response.data;
};

export const applyForSlot = async (data: {
  slotId: number;
  leaderEmpId: number;
  members: number[];
}) => {
  return axiosInstance.post("/api/games/apply", data);
};

export const cancelBooking = async (data: {
  slotId: number;
  cancelledByEmpId: number;
}) => {
  return axiosInstance.post("/api/games/cancel", data);
};

export const completeSlot = async (slotId: number) => {
  return axiosInstance.post(`/api/games/${slotId}/complete`);
};