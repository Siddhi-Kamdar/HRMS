package com.example.backend.repository;

import com.example.backend.entity.Employee;
import com.example.backend.entity.Post;
import com.example.backend.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndLikedBy(Post post, Employee employee);
    long countByPost(Post post);
    void deleteByPostAndLikedBy(Post post, Employee employee);
    List<PostLike> findByPost(Post post);
}
