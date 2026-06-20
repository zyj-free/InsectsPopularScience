package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.entity.Category;
import com.example.insectspopularscience.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ApiResponse<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ApiResponse.success(categories);
    }

    @GetMapping("/parent/{parentId}")
    public ApiResponse<List<Category>> getCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        return ApiResponse.success(categories);
    }

    @GetMapping("/level/{level}")
    public ApiResponse<List<Category>> getCategoriesByLevel(@PathVariable Integer level) {
        List<Category> categories = categoryService.getCategoriesByLevel(level);
        return ApiResponse.success(categories);
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return ApiResponse.success(category);
        }
        return ApiResponse.error("分类不存在");
    }
}

