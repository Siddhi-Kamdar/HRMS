package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentResponseDTO {

    private Long commentId;
    private String commentDescription;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdDate;
}