package com.example.backend.service;

import com.example.backend.dto.response.CommentResponseDTO;
import com.example.backend.dto.response.PostResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.PostLikeRepository;
import com.example.backend.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AchievementImageStorageService imageStorageService;

    @Autowired
    private EmailService emailService;

    public PostResponseDTO createPost(String title, String description, MultipartFile image, Employee userPrincipal) {
        if (userPrincipal == null) throw new RuntimeException("User cannot be null");

        Employee author = employeeRepository.findById((long)userPrincipal.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setAuthor(author);
        post.setSystemGenerated(false);
        post.setDeleted(false);
        post.setComments(new ArrayList<>());
        post.setLikes(new ArrayList<>());

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String imageUrl = imageStorageService.uploadImage(image);
            post.setPostImageUrl(imageUrl);
        }

        Post saved = postRepository.save(post);
        return mapToResponse(saved, author);
    }

    public Page<PostResponseDTO> getFeed(int page, int size, Employee currentUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> posts = postRepository.findByIsDeletedFalse(pageable);
        return posts.map(post -> mapToResponse(post, currentUser));
    }

    @Transactional
    public void likePost(Long postId, Employee user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postLikeRepository.existsByPostAndLikedBy(post, user)) {
            throw new RuntimeException("Already liked");
        }

        PostLike like = new PostLike();
        like.setPost(post);
        like.setLikedBy(user);
        like.setId(new PostLikeId(post.getPostId(), user.getEmployeeId()));

        postLikeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long postId, Employee user) {
        if (user == null) throw new RuntimeException("User cannot be null");

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postLikeRepository.deleteByPostAndLikedBy(post, user);
    }

    public Comment addComment(Long postId, String text, Employee user) {
        if (user == null) throw new RuntimeException("User cannot be null");

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setCommentor(user);
        comment.setCommentDescription(text);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deletePost(Long postId, Employee currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean isAuthor = post.getAuthor() != null && post.getAuthor().getEmployeeId() == currentUser.getEmployeeId();
        boolean isHR = currentUser.getRole() != null && "HR".equals(currentUser.getRole().getRoleName());

        if (!isAuthor && !isHR) throw new RuntimeException("Not authorized");

        post.setDeleted(true);
        post.setDeletedBy(currentUser);

        if (isHR && !isAuthor) {
            emailService.sendWarning(post.getAuthor());
        }

        postRepository.save(post);
    }

    private PostResponseDTO mapToResponse(Post post, Employee currentUser) {
        List<Comment> comments = post.getComments() != null ? post.getComments() : new ArrayList<>();

        long likeCount = postLikeRepository.countByPost(post);
        boolean likedByUser = currentUser != null && postLikeRepository.existsByPostAndLikedBy(post, currentUser);

        List<CommentResponseDTO> commentDTOS = comments.stream()
                .filter(c -> !c.isDeleted())
                .map(c -> CommentResponseDTO.builder()
                        .commentId(c.getCommentId())
                        .commentDescription(c.getCommentDescription())
                        .authorId(c.getCommentor() != null ? c.getCommentor().getEmployeeId() : 0L) // default 0L
                        .authorName(c.getCommentor() != null ? c.getCommentor().getFullName() : "Unknown")
                        .createdDate(c.getDateTime())
                        .build())
                .toList();

        String imageUrl = post.getPostImageUrl() != null
                ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(post.getPostImageUrl())
                .toUriString()
                : null;

        long authorId = post.getAuthor() != null ? post.getAuthor().getEmployeeId() : 0L;
        String authorName = post.getAuthor() != null ? post.getAuthor().getFullName() : "Unknown";

        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .description(post.getDescription())
                .postImageUrl(imageUrl)
                .authorId(authorId)
                .authorName(authorName)
                .systemGenerated(post.isSystemGenerated())
                .createdDate(post.getCreatedDate())
                .likeCount(likeCount)
                .commentCount(commentDTOS.size())
                .likedByCurrentUser(likedByUser)
                .comments(commentDTOS)
                .build();
    }
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return;

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/png") || contentType.equals("image/jpeg") || contentType.equals("image/jpg"))) {
            throw new RuntimeException("Only PNG and JPG images are allowed");
        }

        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size must be less than 10MB");
        }
    }
}