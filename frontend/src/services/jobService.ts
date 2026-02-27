import axiosInstance from "./axiosInstance";

export interface Job {
    jobId: number,
    jobTitle: string,
    jobSummary: string,
    jobDescriptionUrl: string,
    jobStatus: string,
    postedDate: string
}
export const createJob = async (formData: FormData) => {
  const response = await axiosInstance.post(
    "/api/jobs",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return response.data;
};
export const getJobs = async (): Promise<Job[]> => {
  const response = await axiosInstance.get("/api/jobs");
  return response.data;
};
export const shareJob = async (
  jobId: number,
  emails: string[]
) => {
  return axiosInstance.post(
    `/api/jobs/${jobId}/share`,
    emails
  );
};

export const updateJob = async (
  jobId: number,
  formData: FormData
) => {
  const response = await axiosInstance.put(
    `/api/jobs/${jobId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return response.data;
};
