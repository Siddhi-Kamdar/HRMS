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
  likeCount: number;              
  likedByCurrentUser: boolean; 
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
  await axiosInstance.delete(`/api/achievements/${postId}/like`);
};

export const deletePost = async (postId: number) => {
  await axiosInstance.delete(`/api/achievements/${postId}`);
};


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

export const deleteComment = async (commentId: number): Promise<void> => {
  await axiosInstance.delete(`/api/achievements/comments/${commentId}`);
};

export const likeComment = async (commentId: number) => {
  await axiosInstance.post(`/api/achievements/comments/${commentId}/like`);
};

export const unlikeComment = async (commentId: number) => {
  await axiosInstance.delete(`/api/achievements/comments/${commentId}/like`);
};

export const getPostLikes = async (postId: number): Promise<LikeUser[]> => {
  const response = await axiosInstance.get(`/api/achievements/${postId}/likes`);
  return response.data;
};

export const editPost = async (
  postId: number,
  title: string,
  description: string,
  image?: File | null,
  removeImage: boolean = false
) => {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("description", description);
  formData.append("removeImage", removeImage ? "true" : "false");

  if (image) formData.append("image", image);

  const response = await axiosInstance.put(
    `/api/achievements/${postId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return response.data;
};

export const getCommentLikes = async (commentId: number) => {
  const response = await axiosInstance.get(
    `/api/achievements/comments/${commentId}/likes`
  );
  return response.data;
};
export const searchPosts = async (
  keyword?: string,
  authorId?: number,
  from?: string,
  to?: string
) => {
  const params = new URLSearchParams();

  if (keyword) params.append("keyword", keyword);
  if (authorId) params.append("authorId", authorId.toString());
  if (from) params.append("from", from);
  if (to) params.append("to", to);

  const response = await axiosInstance.get(
    `/api/achievements/search?${params.toString()}`
  );

  return response.data;
};