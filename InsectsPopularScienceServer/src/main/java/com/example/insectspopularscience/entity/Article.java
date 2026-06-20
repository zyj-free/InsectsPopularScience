package com.example.insectspopularscience.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String author;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @Column(name = "view_count", columnDefinition = "int default 0")
    private Integer viewCount = 0;

    @Column(name = "like_count", columnDefinition = "int default 0")
    private Integer likeCount = 0;

    @Column(name = "is_recommend", columnDefinition = "boolean default false")
    private Boolean isRecommend = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

