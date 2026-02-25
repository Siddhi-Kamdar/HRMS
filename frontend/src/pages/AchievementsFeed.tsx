import { useState } from "react";
import {
    type PostResponse,
    toggleLikePost,
    addComment,
    type CommentResponse,
} from "../services/achievementsService";

interface Props {
    posts: PostResponse[];
    onPostsChange: (posts: PostResponse[]) => void;
}

export const AchievementsFeed: React.FC<Props> = ({ posts, onPostsChange }) => {
    const [commentText, setCommentText] = useState<{ [key: number]: string }>({});

    const handleLike = async (postId: number) => {
        await toggleLikePost(postId);
        onPostsChange(
            posts.map((p) =>
                p.postId === postId
                    ? {
                        ...p,
                        likedByCurrentUser: !p.likedByCurrentUser,
                        likeCount: p.likedByCurrentUser ? p.likeCount - 1 : p.likeCount + 1,
                    }
                    : p
            )
        );
    };

    const handleComment = async (postId: number) => {
        const text = commentText[postId];
        if (!text?.trim()) return;
        const newComment: CommentResponse = await addComment(postId, { commentDescription: text });
        onPostsChange(
            posts.map((p) =>
                p.postId === postId
                    ? { ...p, comments: [...p.comments, newComment], commentCount: p.commentCount + 1 }
                    : p
            )
        );
        setCommentText((prev) => ({ ...prev, [postId]: "" }));
    };

    return (
        <div className="row g-3">
            {posts.map((post) => (
                <div key={post.postId} className="col-12">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            {post.systemGenerated && (
                                <span className="badge bg-secondary mb-2">System</span>
                            )}
                            <small className="text-muted mb-2 d-block">
                                Posted on {new Date(post.createdDate).toLocaleString()}
                            </small>
                            <h5 className="card-title">{post.title}</h5>
                            <p className="card-text">{post.description}</p>
                            {post.postImageUrl && (
                                <img
                                    src={post.postImageUrl}
                                    alt="Post"
                                    className="img-fluid rounded mb-2"
                                    style={{ maxHeight: "400px", objectFit: "cover" }}
                                />
                            )}
                            

                            <div className="d-flex align-items-center mb-2 gap-3">
                                <button
                                    className="btn btn-sm btn-outline-primary"
                                    onClick={() => handleLike(post.postId)}
                                >
                                    Like ({post.likeCount}) {post.likedByCurrentUser ? "✔" : ""}
                                </button>
                                <span>Comments ({post.commentCount})</span>
                            </div>

                            <div className="mb-2">
                                {post.comments.map((c) => (
                                    <div key={c.commentId} className="mb-1">
                                        <strong>{c.authorName}:</strong> {c.commentDescription}{" "}
                                        <small className="text-muted">
                                            ({new Date(c.createdDate).toLocaleString()})
                                        </small>
                                    </div>
                                ))}

                                <div className="input-group mt-2">
                                    <input
                                        type="text"
                                        placeholder="Add a comment..."
                                        className="form-control"
                                        value={commentText[post.postId] || ""}
                                        onChange={(e) =>
                                            setCommentText((prev) => ({
                                                ...prev,
                                                [post.postId]: e.target.value,
                                            }))
                                        }
                                    />
                                    <button
                                        className="btn btn-outline-secondary"
                                        onClick={() => handleComment(post.postId)}
                                    >
                                        Post
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};