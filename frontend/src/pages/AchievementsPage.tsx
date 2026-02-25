import { useEffect, useState } from "react";
import { CreatePost } from "./CreatePost";
import { AchievementsFeed } from "./AchievementsFeed";
import {
  getAchievementsFeed,
  type PostResponse,
} from "../services/achievementsService";

export const AchievementsPage: React.FC = () => {
  const [posts, setPosts] = useState<PostResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);

  const fetchPosts = async (pageNumber = 0) => {
    setLoading(true);
    try {
      const data = await getAchievementsFeed(pageNumber, 10); 
      setPosts(data); 
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts(); 
  }, []);

  const handlePostCreated = (newPost: PostResponse) => {
    setPosts((prev) => [newPost, ...prev]);
  };

  return (
    <div className="space-y-6">
      {/* <h1 className="text-2xl font-bold">Achievements</h1> */}

      <CreatePost onPostCreated={handlePostCreated} />

      {loading ? (
        <p>Loading posts...</p>
      ) : (
        <AchievementsFeed posts={posts} onPostsChange={setPosts} />
      )}
    </div>
  );
};