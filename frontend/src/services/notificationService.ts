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

export const readNotification = async (id: number) => {
  await axiosInstance.put(`/api/notifications/read/${id}`);
};

export const readAllNotifications = async() =>{
  await axiosInstance.put("/api/notifications/read-all");
}

export const getNotificationCount = async() =>{
  const response = await axiosInstance.get("/api/notifications/unread-count");
  return response.data;
}