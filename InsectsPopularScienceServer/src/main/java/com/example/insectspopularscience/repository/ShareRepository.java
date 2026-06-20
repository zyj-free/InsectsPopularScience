package com.example.insectspopularscience.repository;

import com.example.insectspopularscience.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    Page<Share> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Share> findByUserId(Long userId, Pageable pageable);
}

