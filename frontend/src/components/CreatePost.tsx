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
  const [visibility, setVisibility] = useState<"ALL" | "DEPARTMENT">("ALL");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const post = await createPost({ title, description, image, visibility });
      onPostCreated(post);
      setTitle("");
      setDescription("");
      setImage(null);
      setVisibility("ALL");
    } catch (err) {
      console.error(err);
      alert("Failed to create post");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card edge mb-4 shadow-sm">
      <div className="card-body">
        <h5 className="card-title">Create Post</h5>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <input
              type="text"
              placeholder="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="form-control edge"
            />
          </div>
          <div className="mb-3">
            <textarea
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              className="form-control edge"
              rows={3}
            />
          </div>
          <div className="mb-3">
            <input
              type="file"
              accept="image/png, image/jpeg"
              onChange={(e) => setImage(e.target.files?.[0] || null)}
              className="form-control edge"
            />
          </div>
          <select
            className="form-select mt-2 mb-3 edge"
            value={visibility}
            onChange={(e) => setVisibility(e.target.value as "ALL" | "DEPARTMENT")}
          >
            <option value="ALL">All Employees</option>
            <option value="DEPARTMENT">My Department</option>
          </select>
          <button
            type="submit"
            disabled={loading}
            className="btn btn-outline-success edge"
          >
            {loading ? "Posting..." : "Post Achievement"}
          </button>
        </form>
      </div>
    </div>
  );
};