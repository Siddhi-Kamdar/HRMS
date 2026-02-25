import axiosInstance from "./axiosInstance";

export interface PostRequest {
  title: string;
  description: string;
  image?: File | null;
}

export interface CommentRequest {
  commentDescription: string;
}

export interface CommentResponse {
  commentId: number;
  commentDescription: string;
  authorId: number;
  authorName: string;
  createdDate: string;
}

export interface PostResponse {
  postId: number;
  title: string;
  description: string;
  postImageUrl?: string;
  authorId: number;
  authorName: string;
  systemGenerated: boolean;
  createdDate: string;
  likeCount: number;
  commentCount: number;
  likedByCurrentUser: boolean;
  comments: CommentResponse[];
}
export interface LikeUser {
  employeeId: number;
  fullName: string;
}


export const createPost = async (post: PostRequest): Promise<PostResponse> => {
  const formData = new FormData();
  formData.append("title", post.title);
  formData.append("description", post.description);
  if (post.image) formData.append("image", post.image);

  const response = await axiosInstance.post<PostResponse>(
    "/api/achievements",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return response.data;
};


export const getAchievementsFeed = async (
  page: number,
  size: number
): Promise<PostResponse[]> => {
  const response = await axiosInstance.get<{
    content: PostResponse[];
  }>(`/api/achievements?page=${page}&size=${size}`);

  return response.data.content;
};

export const likePost = async (postId: number) => {
  await axiosInstance.post(`/api/achievements/${postId}/like`);
};

export const unlikePost = async (postId: number) => {
  await axiosInstance.post(`/api/achievements/${postId}/unlike`);
};

export const deletePost = async (postId: number) => {
  await axiosInstance.delete(`/api/achievements/${postId}`);
};

// export const toggleLikePost = async (postId: number): Promise<void> => {
//   await axiosInstance.post(`/api/achievements/${postId}/like`);
// };

export const addComment = async (
  postId: number,
  comment: CommentRequest
): Promise<CommentResponse> => {
  const response = await axiosInstance.post<CommentResponse>(
    `/api/achievements/${postId}/comments`,
    comment
  );
  return response.data;
};

export const getPostLikes = async (postId: number): Promise<LikeUser[]> => {
  const response = await axiosInstance.get(`/api/achievements/${postId}/likes`);
  return response.data;
};