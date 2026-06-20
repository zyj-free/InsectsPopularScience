package com.example.insectspopularscience.service;

import com.example.insectspopularscience.entity.Note;
import com.example.insectspopularscience.repository.NoteRepository;
import com.example.insectspopularscience.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Transactional
    public Note createNote(Long insectId, String content) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Note note = new Note();
        note.setUserId(userId);
        note.setInsectId(insectId);
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Transactional
    public Note updateNote(Long noteId, String content) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("笔记不存在"));

        if (!note.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此笔记");
        }

        note.setContent(content);
        return noteRepository.save(note);
    }

    @Transactional
    public void deleteNote(Long noteId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("笔记不存在"));

        if (!note.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此笔记");
        }

        noteRepository.delete(note);
    }

    public Page<Note> getUserNotes(Pageable pageable) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return noteRepository.findByUserId(userId, pageable);
    }

    public Page<Note> getNotesByInsectId(Long insectId, Pageable pageable) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return noteRepository.findByUserIdAndInsectId(userId, insectId, pageable);
    }

    public Optional<Note> getNoteById(Long noteId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Optional<Note> note = noteRepository.findById(noteId);
        if (note.isPresent() && !note.get().getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此笔记");
        }
        return note;
    }
}

