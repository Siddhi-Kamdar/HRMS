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
                className="btn btn-sm btn-outline-primary"
                onClick={() => handleLike(post.postId)}
              >
                {post.likedByCurrentUser ? "✔" : ""} Like ({post.likeCount})
              </button>
              <span className="text-muted">Comments ({post.commentCount})</span>
            </div>

            <div className="mt-2">
              {post.comments.map((c) => (
                <div key={c.commentId} className="mb-1">
                  <strong>{c.authorName}</strong>: {c.commentDescription}{" "}
                  <small className="text-muted">({timeAgo(c.createdDate)})</small>
                </div>
              ))}

              <div className="input-group mt-2">
                <input
                  type="text"
                  className="form-control"
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
                  className="btn btn-outline-secondary"
                  onClick={() => handleComment(post.postId)}
                >
                  Post
                </button>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};