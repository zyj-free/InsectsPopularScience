package com.example.insectspopularscience.repository;

import com.example.insectspopularscience.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByUserId(Long userId, Pageable pageable);
    Page<Note> findByUserIdAndInsectId(Long userId, Long insectId, Pageable pageable);
}

