package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts_likes")
@Getter
@Setter
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @MapsId("likedById")
    @JoinColumn(name = "liked_by_id", nullable = false)
    private Employee likedBy;

    private LocalDateTime likedAt;

    @PrePersist
    public void onLike() {
        this.likedAt = LocalDateTime.now();
    }
}