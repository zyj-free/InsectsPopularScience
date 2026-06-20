package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.entity.Comment;
import com.example.insectspopularscience.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ApiResponse<Comment> createComment(@RequestBody Map<String, Object> request) {
        try {
            Long shareId = Long.valueOf(request.get("shareId").toString());
            String content = request.get("content").toString();
            Long parentId = request.get("parentId") != null ? 
                    Long.valueOf(request.get("parentId").toString()) : null;
            Comment comment = commentService.createComment(shareId, content, parentId);
            return ApiResponse.success("评论成功", comment);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/share/{shareId}")
    public ApiResponse<Page<Comment>> getCommentsByShareId(
            @PathVariable Long shareId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Comment> comments = commentService.getCommentsByShareId(shareId, pageable);
        return ApiResponse.success(comments);
    }

    @GetMapping("/tree/{shareId}")
    public ApiResponse<List<Comment>> getCommentTree(@PathVariable Long shareId) {
        List<Comment> comments = commentService.getCommentTree(shareId);
        return ApiResponse.success(comments);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{commentId}")
    public ApiResponse<Comment> getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("评论不存在"));
    }
}

