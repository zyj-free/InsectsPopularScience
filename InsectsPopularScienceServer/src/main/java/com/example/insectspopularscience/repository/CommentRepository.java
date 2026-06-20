package com.example.insectspopularscience.repository;

import com.example.insectspopularscience.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByShareId(Long shareId, Pageable pageable);
    List<Comment> findByShareIdAndParentIdIsNull(Long shareId);
    List<Comment> findByParentId(Long parentId);
    long countByShareId(Long shareId);
}

