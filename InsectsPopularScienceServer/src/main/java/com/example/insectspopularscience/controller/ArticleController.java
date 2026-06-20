package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.entity.Article;
import com.example.insectspopularscience.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public ApiResponse<Page<Article>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Article> articles = articleService.getAllArticles(pageable);
        return ApiResponse.success(articles);
    }

    @GetMapping("/{id}")
    public ApiResponse<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("文章不存在"));
    }

    @GetMapping("/recommend")
    public ApiResponse<List<Article>> getRecommendArticles() {
        List<Article> articles = articleService.getRecommendArticles();
        return ApiResponse.success(articles);
    }

    @GetMapping("/search")
    public ApiResponse<Page<Article>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles = articleService.searchArticles(keyword, pageable);
        return ApiResponse.success(articles);
    }
}

