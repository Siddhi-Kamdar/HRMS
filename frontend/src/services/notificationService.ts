import axiosInstance from "./axiosInstance";

export interface Notification{
    notificationId: number,
    message: String,
    title: String
}

export const getNotifications = async (): Promise<Notification[]> => {
  const response = await axiosInstance.get("/api/notifications/my");
  return response.data;
};