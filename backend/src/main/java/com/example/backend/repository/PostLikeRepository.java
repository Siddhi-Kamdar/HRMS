package com.example.backend.repository;

import com.example.backend.entity.Employee;
import com.example.backend.entity.Post;
import com.example.backend.entity.PostLike;
import com.example.backend.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    boolean existsByPostAndLikedBy(Post post, Employee employee);
    long countByPost(Post post);
    void deleteByPostAndLikedBy(Post post, Employee employee);
    List<PostLike> findByPost(Post post);
}
