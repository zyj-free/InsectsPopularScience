package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.entity.Insect;
import com.example.insectspopularscience.service.InsectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insects")
@CrossOrigin(origins = "*")
public class InsectController {

    @Autowired
    private InsectService insectService;

    @GetMapping("/list")
    public ApiResponse<Page<Insect>> getInsects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Insect> insects;
        if (categoryId != null) {
            insects = insectService.getInsectsByCategory(categoryId, pageable);
        } else {
            insects = insectService.getAllInsects(pageable);
        }
        return ApiResponse.success(insects);
    }

    @GetMapping("/{id}")
    public ApiResponse<Insect> getInsectById(@PathVariable Long id) {
        return insectService.getInsectById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("昆虫不存在"));
    }

    @GetMapping("/search")
    public ApiResponse<Page<Insect>> searchInsects(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Insect> insects = insectService.searchInsects(keyword, pageable);
        return ApiResponse.success(insects);
    }

    @GetMapping("/popular")
    public ApiResponse<List<Insect>> getPopularInsects() {
        List<Insect> insects = insectService.getPopularInsects();
        return ApiResponse.success(insects);
    }
}

