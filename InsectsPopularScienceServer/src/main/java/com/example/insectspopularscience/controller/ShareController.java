package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.entity.Share;
import com.example.insectspopularscience.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shares")
@CrossOrigin(origins = "*")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @PostMapping("/create")
    public ApiResponse<Share> createShare(@RequestBody Map<String, Object> request) {
        try {
            Long insectId = request.get("insectId") != null ? 
                    Long.valueOf(request.get("insectId").toString()) : null;
            String title = request.get("title") != null ? 
                    request.get("title").toString() : null;
            String imageUrl = request.get("imageUrl") != null ? 
                    request.get("imageUrl").toString() : null;
            String description = request.get("description") != null ? 
                    request.get("description").toString() : null;
            Share share = shareService.createShare(insectId, title, imageUrl, description);
            return ApiResponse.success("分享成功", share);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse<Page<Share>> getAllShares(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Share> shares = shareService.getAllShares(pageable);
        return ApiResponse.success(shares);
    }

    @GetMapping("/my")
    public ApiResponse<Page<Share>> getUserShares(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Share> shares = shareService.getUserShares(pageable);
            return ApiResponse.success(shares);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Share> getShareById(@PathVariable Long id) {
        return shareService.getShareById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("分享不存在"));
    }

    @PutMapping("/{id}")
    public ApiResponse<Share> updateShare(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Long insectId = request.get("insectId") != null ?
                    Long.valueOf(request.get("insectId").toString()) : null;
            String title = request.get("title") != null ?
                    request.get("title").toString() : null;
            String imageUrl = request.get("imageUrl") != null ?
                    request.get("imageUrl").toString() : null;
            String description = request.get("description") != null ?
                    request.get("description").toString() : null;
            Share share = shareService.updateShare(id, insectId, title, imageUrl, description);
            return ApiResponse.success("修改成功", share);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteShare(@PathVariable Long id) {
        try {
            shareService.deleteShare(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

