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

export const updateReferralStatus = async (
  referralId: number,
  statusId: number
) => {
  return axiosInstance.put(
    `/api/referrals/${referralId}/status?statusId=${statusId}`
  );
};

export const getAllReferrals = async () => {
  const response = await axiosInstance.get("/api/referrals");
  return response.data;
};