import { useState } from "react";
import { createPost, type PostResponse } from "../services/achievementsService";

interface Props {
  onPostCreated: (post: PostResponse) => void;
}

export const CreatePost: React.FC<Props> = ({ onPostCreated }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [image, setImage] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const post = await createPost({ title, description, image });
      onPostCreated(post);
      setTitle("");
      setDescription("");
      setImage(null);
    } catch (err) {
      console.error(err);
      alert("Failed to create post");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <h5 className="card-title">Create Achievement</h5>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <input
              type="text"
              placeholder="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
              className="form-control"
            />
          </div>
          <div className="mb-3">
            <textarea
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              className="form-control"
              rows={3}
            />
          </div>
          <div className="mb-3">
            <input
              type="file"
              accept="image/png, image/jpeg"
              onChange={(e) => setImage(e.target.files?.[0] || null)}
              className="form-control"
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary"
          >
            {loading ? "Posting..." : "Post Achievement"}
          </button>
        </form>
      </div>
    </div>
  );
};