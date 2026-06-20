package com.example.insectspopularscience.repository;

import com.example.insectspopularscience.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUserId(Long userId, Pageable pageable);
    Optional<Favorite> findByUserIdAndInsectId(Long userId, Long insectId);
    boolean existsByUserIdAndInsectId(Long userId, Long insectId);
    void deleteByUserIdAndInsectId(Long userId, Long insectId);
}

