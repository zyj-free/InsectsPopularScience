package com.example.insectspopularscience.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "insects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "scientific_name", length = 200)
    private String scientificName;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(length = 2000)
    private String description;

    @Column(name = "morphological_features", length = 2000)
    private String morphologicalFeatures;

    @Column(name = "living_habits", length = 2000)
    private String livingHabits;

    @Column(length = 500)
    private String habitat;

    @Column(length = 500)
    private String distribution;

    @Column(name = "image_urls", length = 2000)
    private String imageUrls; // JSON格式存储多个图片URL

    @Column(name = "view_count", columnDefinition = "int default 0")
    private Integer viewCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

