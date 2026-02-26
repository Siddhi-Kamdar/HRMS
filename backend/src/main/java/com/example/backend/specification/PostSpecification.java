package com.example.backend.specification;

import com.example.backend.entity.Post;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PostSpecification {

    public static Specification<Post> containsKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    public static Specification<Post> hasAuthor(Integer authorId) {
        return (root, query, cb) -> {
            if (authorId == null) return null;
            return cb.equal(root.get("author").get("employeeId"), authorId);
        };
    }

    public static Specification<Post> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) return null;
            return cb.greaterThanOrEqualTo(root.get("createdDate"), from);
        };
    }

    public static Specification<Post> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) return null;
            return cb.lessThanOrEqualTo(root.get("createdDate"), to);
        };
    }
}