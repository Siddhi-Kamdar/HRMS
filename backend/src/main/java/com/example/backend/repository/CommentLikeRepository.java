package com.example.backend.repository;

import com.example.backend.entity.Comment;
import com.example.backend.entity.CommentLike;
import com.example.backend.entity.CommentLikeId;
import com.example.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository
        extends JpaRepository<CommentLike, CommentLikeId> {

    boolean existsByCommentAndLikedBy(Comment comment, Employee employee);

    long countByComment(Comment comment);

    void deleteByCommentAndLikedBy(Comment comment, Employee employee);

    List<CommentLike> findByComment(Comment comment);
}