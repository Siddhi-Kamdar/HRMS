package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments_likes")
@Getter
@Setter
public class CommentLike {

    @EmbeddedId
    private CommentLikeId id;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @MapsId("likedById")
    @JoinColumn(name = "liked_by_id")
    private Employee likedBy;

    private LocalDateTime likedAt;

    @PrePersist
    public void onLike() {
        this.likedAt = LocalDateTime.now();
    }
}
