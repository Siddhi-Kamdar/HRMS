import axiosInstance from "./axiosInstance";

export interface Job {
    jobId: number,
    jobTitle: string,
    jobSummary: string,
    jobDescriptionUrl: string,
    jobStatus: string,
    postedDate: string
}

export const getJobs = async (): Promise<Job[]> => {
  const response = await axiosInstance.get("/api/jobs");
  return response.data;
};
