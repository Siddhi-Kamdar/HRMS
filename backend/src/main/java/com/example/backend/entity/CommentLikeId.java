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
public class CommentLikeId implements Serializable {

    private Integer commentId;
    private Integer likedById;

    public CommentLikeId() {}

    public CommentLikeId(Integer commentId, Integer likedById) {
        this.commentId = commentId;
        this.likedById = likedById;
    }
}