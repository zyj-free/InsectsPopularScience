package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.entity.Note;
import com.example.insectspopularscience.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/create")
    public ApiResponse<Note> createNote(@RequestBody Map<String, Object> request) {
        try {
            Long insectId = request.get("insectId") != null ? 
                    Long.valueOf(request.get("insectId").toString()) : null;
            String content = request.get("content").toString();
            Note note = noteService.createNote(insectId, content);
            return ApiResponse.success("创建成功", note);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{noteId}")
    public ApiResponse<Note> updateNote(
            @PathVariable Long noteId,
            @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            Note note = noteService.updateNote(noteId, content);
            return ApiResponse.success("更新成功", note);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{noteId}")
    public ApiResponse<Void> deleteNote(@PathVariable Long noteId) {
        try {
            noteService.deleteNote(noteId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<Page<Note>> getUserNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long insectId) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
            Page<Note> notes;
            if (insectId != null) {
                notes = noteService.getNotesByInsectId(insectId, pageable);
            } else {
                notes = noteService.getUserNotes(pageable);
            }
            return ApiResponse.success(notes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{noteId}")
    public ApiResponse<Note> getNoteById(@PathVariable Long noteId) {
        try {
            return noteService.getNoteById(noteId)
                    .map(ApiResponse::success)
                    .orElse(ApiResponse.error("笔记不存在"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

