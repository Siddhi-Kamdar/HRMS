package com.example.backend.controller;

import com.example.backend.dto.response.PostResponseDTO;
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

        // Fetch logged-in user same way as above
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
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Employee user = getEmployeeFromToken(authHeader);
        postService.likePost(id, user);
        return ResponseEntity.ok().build();
    }

    // Unlike a post
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlike(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Employee user = getEmployeeFromToken(authHeader);
        postService.unlikePost(id, user);
        return ResponseEntity.ok().build();
    }

    //method to fetch Employee from JWT token
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