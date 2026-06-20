package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle/{insectId}")
    public ApiResponse<Map<String, Object>> toggleFavorite(@PathVariable Long insectId) {
        try {
            Map<String, Object> result = favoriteService.toggleFavorite(insectId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<Page<com.example.insectspopularscience.entity.Insect>> getUserFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<com.example.insectspopularscience.entity.Insect> insects = favoriteService.getUserFavoriteInsects(pageable);
            return ApiResponse.success(insects);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/check/{insectId}")
    public ApiResponse<Boolean> isFavorite(@PathVariable Long insectId) {
        boolean isFavorite = favoriteService.isFavorite(insectId);
        return ApiResponse.success(isFavorite);
    }
}

