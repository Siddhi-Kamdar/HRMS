package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String postImageUrl;

    private List<Long> tagIds;
}