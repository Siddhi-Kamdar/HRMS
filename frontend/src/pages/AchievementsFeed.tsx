import { useState } from "react";
import {
    type PostResponse,
    likePost,
    unlikePost,
    addComment,
    deletePost,
    getPostLikes,
    type LikeUser,
    type CommentResponse,
} from "../services/achievementsService";
import "../index.css";

interface Props {
    posts: PostResponse[];
    currentUserId: number;
    currentUserRole: string;
    onPostsChange: (posts: PostResponse[]) => void;
}

export const AchievementsFeed: React.FC<Props> = ({
    posts,
    currentUserId,
    currentUserRole,
    onPostsChange,
}) => {
    const [commentText, setCommentText] = useState<{ [key: number]: string }>({});
    const [expandedComments, setExpandedComments] = useState<{ [key: number]: boolean }>({});
    const [likesModalPostId, setLikesModalPostId] = useState<number | null>(null);
    const [likesUsers, setLikesUsers] = useState<LikeUser[]>([]);

    const toggleComments = (postId: number) => {
        setExpandedComments((prev) => ({
            ...prev,
            [postId]: !prev[postId],
        }));
    };

    const handleShowLikes = async (postId: number) => {
        try {
            const users = await getPostLikes(postId);
            setLikesUsers(users);
            setLikesModalPostId(postId);
        } catch (err) {
            console.error(err);
            alert("Failed to fetch likes.");
        }
    };
    const handleLike = async (post: PostResponse) => {
        try {
            if (post.likedByCurrentUser) {
                await unlikePost(post.postId);
            } else {
                await likePost(post.postId);
            }

            onPostsChange(
                posts.map((p) =>
                    p.postId === post.postId
                        ? {
                            ...p,
                            likedByCurrentUser: !p.likedByCurrentUser,
                            likeCount: p.likedByCurrentUser
                                ? p.likeCount - 1
                                : p.likeCount + 1,
                        }
                        : p
                )
            );
        } catch (err) {
            console.error(err);
            alert("Failed to toggle like. Are you logged in?");
        }
    };

    const handleComment = async (postId: number) => {
        const text = commentText[postId];
        if (!text?.trim()) return;
        try {
            const newComment: CommentResponse = await addComment(postId, {
                commentDescription: text,
            });
            onPostsChange(
                posts.map((p) =>
                    p.postId === postId
                        ? {
                            ...p,
                            comments: [...p.comments, newComment],
                            commentCount: p.commentCount + 1,
                        }
                        : p
                )
            );
            setCommentText((prev) => ({ ...prev, [postId]: "" }));
        } catch (err) {
            console.error(err);
            alert("Failed to add comment. Are you logged in?");
        }
    };

    const handleDelete = async (postId: number) => {
        if (!confirm("Are you sure you want to delete this post?")) return;
        try {
            await deletePost(postId);
            onPostsChange(posts.filter((p) => p.postId !== postId));
        } catch (err) {
            console.error(err);
            alert("Failed to delete post. Are you authorized?");
        }
    };

    const timeAgo = (dateString: string) => {
        const diff = Date.now() - new Date(dateString).getTime();
        const seconds = Math.floor(diff / 1000);
        if (seconds < 60) return `${seconds}s ago`;
        const minutes = Math.floor(seconds / 60);
        if (minutes < 60) return `${minutes}m ago`;
        const hours = Math.floor(minutes / 60);
        if (hours < 24) return `${hours}h ago`;
        const days = Math.floor(hours / 24);
        return `${days}d ago`;
    };

    return (
        <div className="d-flex flex-column gap-3">
            {posts.map((post) => (
                <div key={post.postId} className="card shadow-sm">
                    <div className="card-body">
                        <div className="d-flex align-items-center mb-2">
                            <div
                                className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center me-2"
                                style={{ width: 40, height: 40 }}
                            >
                                {post.authorName.charAt(0)}
                            </div>
                            <div>
                                <strong>{post.authorName}</strong>{" "}
                                {post.systemGenerated && (
                                    <span className="badge bg-secondary">System</span>
                                )}
                                <div className="text-muted" style={{ fontSize: "0.8rem" }}>
                                    {timeAgo(post.createdDate)}
                                </div>
                            </div>

                            {(post.authorId === currentUserId || currentUserRole === "HR") && (
                                <button
                                    className="btn btn-sm border-0 btn-outline-danger ms-auto"
                                    onClick={() => handleDelete(post.postId)}
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-trash" viewBox="0 0 16 16">
                                        <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z" />
                                        <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z" />
                                    </svg>
                                </button>
                            )}
                        </div>

                        <p>{post.description}</p>
                        {post.postImageUrl && (
                            <img
                                src={post.postImageUrl}
                                alt="Post"
                                className="img-fluid rounded mb-2"
                                style={{ maxHeight: 400, objectFit: "cover" }}
                            />
                        )}

                        <div className="d-flex gap-3 mb-2">
                            <button
                                className="btn btn-sm border-0 bg-transparent d-flex align-items-center gap-1"
                                onClick={() => handleLike(post)}
                            >
                                {post.likedByCurrentUser ? (
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        width="16"
                                        height="16"
                                        fill="red"
                                        viewBox="0 0 16 16"
                                    >
                                        <path
                                            fillRule="evenodd"
                                            d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
                                        />
                                    </svg>
                                ) : (
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        width="16"
                                        height="16"
                                        fill="currentColor"
                                        viewBox="0 0 16 16"
                                    >
                                        <path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15" />
                                    </svg>
                                )}

                                <span
                                    style={{ cursor: "pointer" }}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handleShowLikes(post.postId);
                                    }}
                                >
                                    {post.likeCount}
                                </span>
                            </button>
                            <button
                                className="btn btn-sm border-0 bg-transparent text-muted d-flex align-items-center gap-1"
                                onClick={() => toggleComments(post.postId)}
                            >
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    width="16"
                                    height="16"
                                    fill="currentColor"
                                    viewBox="0 0 16 16"
                                >
                                    <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894" />
                                </svg>

                                <span>{post.commentCount}</span>
                            </button>
                        </div>
                        <div className="input-group mt-2">
                            <input
                                type="text"
                                className="form-control form-control-sm"
                                placeholder="Add a comment..."
                                value={commentText[post.postId] || ""}
                                onChange={(e) =>
                                    setCommentText((prev) => ({
                                        ...prev,
                                        [post.postId]: e.target.value,
                                    }))
                                }
                            />
                            <button
                                className="btn btn-sm btn-outline-secondary"
                                onClick={() => handleComment(post.postId)}
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-send" viewBox="0 0 16 16">
                                    <path d="M15.854.146a.5.5 0 0 1 .11.54l-5.819 14.547a.75.75 0 0 1-1.329.124l-3.178-4.995L.643 7.184a.75.75 0 0 1 .124-1.33L15.314.037a.5.5 0 0 1 .54.11ZM6.636 10.07l2.761 4.338L14.13 2.576zm6.787-8.201L1.591 6.602l4.339 2.76z" />
                                </svg>
                            </button>
                        </div>
                        <div className="mt-2">
                            {expandedComments[post.postId] && (
                                <div className="mt-3 border-top pt-2">
                                    {post.comments.length === 0 && (
                                        <div className="text-muted small mb-2">
                                            No comments yet
                                        </div>
                                    )}



                                    {post.comments.map((c) => (
                                        <div key={c.commentId} className="mb-2 small">
                                            <strong>{c.authorName}</strong>{" "}
                                            <span className="text-muted">{timeAgo(c.createdDate)}</span>
                                            <div>{c.commentDescription}</div>
                                        </div>
                                    ))}


                                </div>
                            )}

                        </div>
                    </div>
                </div>
            ))}
            {likesModalPostId && (
                <div
                    className="modal fade show"
                    style={{ display: "block", backgroundColor: "rgba(0,0,0,0.5)" }}
                    onClick={() => setLikesModalPostId(null)}
                >
                    <div
                        className="modal-dialog modal-dialog-centered"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Liked by</h5>
                                <button
                                    className="btn-close"
                                    onClick={() => setLikesModalPostId(null)}
                                />
                            </div>
                            <div
                                className="modal-body"
                                style={{
                                    maxHeight: "400px",
                                    overflowY: "auto"
                                }}
                            >
                                {likesUsers.length === 0 ? (
                                    <div className="text-muted">No likes yet</div>
                                ) : (
                                    likesUsers.map((user) => (
                                        <div
                                            key={user.employeeId}
                                            className="d-flex align-items-center mb-2"
                                        >
                                            <div
                                                className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center me-2"
                                                style={{ width: 35, height: 35 }}
                                            >
                                                {user.fullName.charAt(0)}
                                            </div>
                                            <div>{user.fullName}</div>
                                        </div>
                                    ))
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};