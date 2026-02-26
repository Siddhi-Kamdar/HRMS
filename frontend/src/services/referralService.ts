import axiosInstance from "./axiosInstance";

export const referFriend = async (
  jobId: number,
  formData: FormData
) => {
  return axiosInstance.post(
    `/api/referrals/${jobId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );
};