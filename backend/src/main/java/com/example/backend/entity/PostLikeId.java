package com.example.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class PostLikeId implements Serializable {

    private Long postId;
    private Long likedById;

    public PostLikeId() {}

    public PostLikeId(Long postId, Long likedById) {
        this.postId = postId;
        this.likedById = likedById;
    }
}