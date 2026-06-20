package com.example.insectspopularscience.service;

import com.example.insectspopularscience.entity.Comment;
import com.example.insectspopularscience.entity.Share;
import com.example.insectspopularscience.entity.User;
import com.example.insectspopularscience.repository.CommentRepository;
import com.example.insectspopularscience.repository.ShareRepository;
import com.example.insectspopularscience.repository.UserRepository;
import com.example.insectspopularscience.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Comment createComment(Long shareId, String content, Long parentId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        if (!shareRepository.existsById(shareId)) {
            throw new RuntimeException("分享不存在");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment comment = new Comment();
        comment.setShareId(shareId);
        comment.setUserId(userId);
        comment.setUser(user);
        comment.setContent(content);
        comment.setParentId(parentId);

        Comment savedComment = commentRepository.save(comment);

        // 更新分享的评论数
        Share share = shareRepository.findById(shareId).orElse(null);
        if (share != null) {
            share.setCommentCount((int) commentRepository.countByShareId(shareId));
            shareRepository.save(share);
        }

        return savedComment;
    }

    public Page<Comment> getCommentsByShareId(Long shareId, Pageable pageable) {
        return commentRepository.findByShareId(shareId, pageable);
    }

    public List<Comment> getCommentTree(Long shareId) {
        List<Comment> rootComments = commentRepository.findByShareIdAndParentIdIsNull(shareId);
        // 这里可以设置子评论，如果需要的话
        return rootComments;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此评论");
        }

        Long shareId = comment.getShareId();
        commentRepository.delete(comment);

        // 更新分享的评论数
        Share share = shareRepository.findById(shareId).orElse(null);
        if (share != null) {
            share.setCommentCount((int) commentRepository.countByShareId(shareId));
            shareRepository.save(share);
        }
    }

    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }
}

