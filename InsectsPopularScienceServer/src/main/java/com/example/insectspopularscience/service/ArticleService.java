package com.example.insectspopularscience.service;

import com.example.insectspopularscience.entity.Article;
import com.example.insectspopularscience.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Page<Article> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Optional<Article> getArticleById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            Article a = article.get();
            a.setViewCount(a.getViewCount() + 1);
            articleRepository.save(a);
        }
        return article;
    }

    public List<Article> getRecommendArticles() {
        return articleRepository.findByIsRecommendTrueOrderByCreatedAtDesc();
    }

    public Page<Article> searchArticles(String keyword, Pageable pageable) {
        return articleRepository.searchArticles(keyword, pageable);
    }
}

