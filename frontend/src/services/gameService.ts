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
  isMySlot?: boolean;
};
export interface Employee {
  employeeId: number;
  fullName: string;
}

export interface SlotDetail {
  gameName: string;
  slotDate: string;
  startTime: string;
  endTime: string;
  status: string;
  myStatus: string;
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

export const getSlotDetail = async (
  slotId: number,
  empId: number
): Promise<SlotDetail> => {
  const res = await axiosInstance.get(
    `/api/games/slot/${slotId}`,
    {params: {empId}}
  );
  return res.data;
}