package com.example.backend.repository;

import com.example.backend.entity.Employee;
import com.example.backend.entity.Post;
import com.example.backend.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndLikedBy(Post post, Employee employee);
    long countByPost(Post post);
    void deleteByPostAndLikedBy(Post post, Employee employee);
}
