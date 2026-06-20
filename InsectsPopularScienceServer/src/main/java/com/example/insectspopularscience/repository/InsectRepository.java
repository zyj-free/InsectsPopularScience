package com.example.insectspopularscience.repository;

import com.example.insectspopularscience.entity.Insect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsectRepository extends JpaRepository<Insect, Long> {
    Page<Insect> findByCategoryId(Long categoryId, Pageable pageable);
    
    @Query("SELECT i FROM Insect i WHERE i.name LIKE %:keyword% OR i.scientificName LIKE %:keyword% OR i.description LIKE %:keyword%")
    Page<Insect> searchInsects(@Param("keyword") String keyword, Pageable pageable);
    
    List<Insect> findTop10ByOrderByViewCountDesc();
}

