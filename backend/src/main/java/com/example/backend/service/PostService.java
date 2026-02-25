package com.example.backend.service;

import com.example.backend.dto.request.PostRequestDTO;
import com.example.backend.dto.response.CommentResponseDTO;
import com.example.backend.dto.response.PostResponseDTO;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Employee;
import com.example.backend.entity.Post;
import com.example.backend.entity.PostLike;
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
    private  EmailService emailService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AchievementImageStorageService imageStorageService;

    public PostResponseDTO createPost(String title,
                                      String description,
                                      MultipartFile image,
                                      Employee userPrincipal) {

        Employee author = employeeRepository.findById((long)userPrincipal.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setAuthor(author);
        post.setSystemGenerated(false);
        post.setDeleted(false);

        if (image != null && !image.isEmpty()) {
            String imageUrl = imageStorageService.uploadImage(image);
            post.setPostImageUrl(imageUrl);
        }

        Post saved = postRepository.save(post);
        return mapToResponse(saved, author);
    }

    public Page<PostResponseDTO> getFeed(int page, int size, Employee currentUser) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

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

        postLikeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long postId, Employee user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postLikeRepository.deleteByPostAndLikedBy(post, user);
    }

    public Comment addComment(Long postId, String text, Employee user) {

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

        boolean isAuthor = post.getAuthor().getEmployeeId()
                == (currentUser.getEmployeeId());

        boolean isHR = currentUser.getRole().getRoleName().equals("HR");

        if (!isAuthor && !isHR) {
            throw new RuntimeException("Not authorized");
        }

        post.setDeleted(true);
        post.setDeletedBy(currentUser);

        if (isHR && !isAuthor) {
            emailService.sendWarning(post.getAuthor());
        }
    }

    private PostResponseDTO mapToResponse(Post post, Employee currentUser) {

        long likeCount = postLikeRepository.countByPost(post);
        long commentCount = post.getComments()
                .stream()
                .filter(c -> !c.isDeleted())
                .count();

        boolean likedByUser =
                postLikeRepository.existsByPostAndLikedBy(post, currentUser);

        List<CommentResponseDTO> commentDTOS =
                post.getComments().stream()
                        .filter(c -> !c.isDeleted())
                        .map(comment -> CommentResponseDTO.builder()
                                .commentId(comment.getCommentId())
                                .commentDescription(comment.getCommentDescription())
                                .authorId((long)comment.getCommentor().getEmployeeId())
                                .authorName(comment.getCommentor().getFullName())
                                .createdDate(comment.getDateTime())
                                .build())
                        .toList();

        String imageUrl = null;
        if (post.getPostImageUrl() != null) {
            imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(post.getPostImageUrl())
                    .toUriString();
        }

        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .description(post.getDescription())
                .postImageUrl(imageUrl)
                .authorId((long)post.getAuthor().getEmployeeId())
                .authorName(post.getAuthor().getFullName())
                .systemGenerated(post.isSystemGenerated())
                .createdDate(post.getCreatedDate())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .likedByCurrentUser(likedByUser)
                .comments(commentDTOS)
                .build();
    }
    private void validateImage(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/png") &&
                        !contentType.equals("image/jpeg") &&
                        !contentType.equals("image/jpg"))) {

            throw new RuntimeException("Only PNG and JPG images are allowed");
        }

        long maxSize = 10 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size must be less than 10MB");
        }
    }
}
