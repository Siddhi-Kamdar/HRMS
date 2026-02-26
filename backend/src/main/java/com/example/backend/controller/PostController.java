package com.example.backend.controller;

import com.example.backend.dto.request.CommentRequestDTO;
import com.example.backend.dto.response.CommentResponseDTO;
import com.example.backend.dto.response.LikeUserDTO;
import com.example.backend.dto.response.PostResponseDTO;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Employee;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.service.PostService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final EmployeeRepository employeeRepository;

    private final String secretKey = "my-super-secret-key-my-super-secret-key";

    // Create Post
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponseDTO createPost(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) // raw bytes
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        if (email == null) {
            throw new RuntimeException("JWT token missing subject (email)");
        }

        Employee user = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return postService.createPost(title, description, image, user);
    }

    // Get Achievements Feed
    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        Employee user = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return ResponseEntity.ok(postService.getFeed(page, size, user));
    }

    // Like a post
    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        Employee user = getEmployeeFromToken(authHeader);
        postService.likePost(id, user);
        return ResponseEntity.ok().build();
    }

    // Unlike a post
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlike(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        Employee user = getEmployeeFromToken(authHeader);
        postService.unlikePost(id, user);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{postId}/comments")
    public CommentResponseDTO addComment(
            @PathVariable Integer postId,
            @RequestBody CommentRequestDTO request,
            Principal principal
    ) {
        Employee currentUser = employeeRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = postService.addComment(postId, request.getCommentDescription(), currentUser);

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
            Principal principal
    ) {
        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = principal.getName();
        Employee currentUser = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        postService.deletePost(postId, currentUser);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              Principal principal) {
        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }
        String email = principal.getName();
        Employee currentUser = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image,
            Principal principal) {

        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = principal.getName();
        Employee currentUser = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return postService.updatePost(postId, title, description, image, currentUser);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment(
            @PathVariable Integer commentId,
            Principal principal) {

        Employee currentUser = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        postService.likeComment(commentId, currentUser);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<?> unlikeComment(
            @PathVariable Integer commentId,
            Principal principal) {

        Employee currentUser = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        postService.unlikeComment(commentId, currentUser);

        return ResponseEntity.ok().build();
    }

    private Employee getEmployeeFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = claims.getSubject();
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}