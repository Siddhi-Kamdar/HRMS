package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostResponseDTO {

    private Integer postId;
    private String title;
    private String description;
    private String postImageUrl;

    private Long authorId;
    private String authorName;

    private boolean systemGenerated;

    private LocalDateTime createdDate;

    private long likeCount;
    private long commentCount;

    private boolean likedByCurrentUser;

    private List<CommentResponseDTO> comments;
}