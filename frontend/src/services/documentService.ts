
import axiosInstance from "./axiosInstance";

export interface TravelDocument {
  id: number;
  documentUrl: string;
  uploadedDate: string;
}

export const getDocumentsByTravel = async (
  travelId: string
): Promise<TravelDocument[]> => {
  const response = await axiosInstance.get(
    `/api/travel-documents/${travelId}`
  );
  return response.data;
};

export const uploadDocument = async (
  travelId: string,
  file: File,
  uploadedById: number
): Promise<void> => {

  const formData = new FormData();
  formData.append("file", file);
  formData.append("travelId", travelId);
  formData.append("uploadedById", uploadedById.toString());
  formData.append("documentTypeId", "1");
  await axiosInstance.post(
    "/api/travel-documents/upload",
    formData,
    {
      headers: { "Content-Type": "multipart/form-data" }
    }
  );
};
