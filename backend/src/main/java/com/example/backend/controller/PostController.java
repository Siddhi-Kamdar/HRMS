package com.example.backend.controller;

import com.example.backend.dto.request.CommentRequestDTO;
import com.example.backend.dto.response.CommentResponseDTO;
import com.example.backend.dto.response.LikeUserDTO;
import com.example.backend.dto.response.PostResponseDTO;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Employee;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final EmployeeRepository employeeRepository;

    private Employee getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }

        return employeeRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponseDTO createPost(
            @RequestParam(required = false)  String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image,
            Principal principal) {

        Employee user = getCurrentUser(principal);
        return postService.createPost(title, description, image, user);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        Employee user = getCurrentUser(principal);
        return ResponseEntity.ok(postService.getFeed(page, size, user));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(
            @PathVariable Integer id,
            Principal principal) {

        Employee user = getCurrentUser(principal);
        postService.likePost(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlike(
            @PathVariable Integer id,
            Principal principal) {

        Employee user = getCurrentUser(principal);
        postService.unlikePost(id, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public CommentResponseDTO addComment(
            @PathVariable Integer postId,
            @RequestBody CommentRequestDTO request,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);

        Comment comment = postService.addComment(
                postId,
                request.getCommentDescription(),
                currentUser
        );

        return CommentResponseDTO.builder()
                .commentId(Long.valueOf(comment.getCommentId()))
                .commentDescription(comment.getCommentDescription())
                .authorId((long) comment.getCommentor().getEmployeeId())
                .authorName(comment.getCommentor().getFullName())
                .createdDate(comment.getDateTime())
                .build();
    }

    @DeleteMapping("/{postId}")
    public void deletePost(
            @PathVariable Integer postId,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);
        postService.deletePost(postId, currentUser);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);
        postService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}/likes")
    public List<LikeUserDTO> getPostLikes(@PathVariable Integer postId) {
        return postService.getPostLikes(postId);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponseDTO editPost(
            @PathVariable Integer postId,
            @RequestParam(required = false)  String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);
        return postService.updatePost(postId, title, description, image, currentUser);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment(
            @PathVariable Integer commentId,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);
        postService.likeComment(commentId, currentUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<?> unlikeComment(
            @PathVariable Integer commentId,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);
        postService.unlikeComment(commentId, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments/{commentId}/likes")
    public ResponseEntity<List<LikeUserDTO>> getCommentLikes(
            @PathVariable Integer commentId) {

        return ResponseEntity.ok(postService.getCommentLikes(commentId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            Principal principal) {

        Employee currentUser = getCurrentUser(principal);

        LocalDateTime fromDate = from != null ? LocalDateTime.parse(from) : null;
        LocalDateTime toDate = to != null ? LocalDateTime.parse(to) : null;

        return ResponseEntity.ok(
                postService.searchPosts(keyword, authorId, fromDate, toDate, currentUser)
        );
    }
}