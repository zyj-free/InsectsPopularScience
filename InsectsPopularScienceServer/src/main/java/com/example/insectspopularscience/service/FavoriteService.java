package com.example.insectspopularscience.service;

import com.example.insectspopularscience.entity.Favorite;
import com.example.insectspopularscience.entity.Insect;
import com.example.insectspopularscience.repository.FavoriteRepository;
import com.example.insectspopularscience.repository.InsectRepository;
import com.example.insectspopularscience.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private InsectRepository insectRepository;

    @Transactional
    public Map<String, Object> toggleFavorite(Long insectId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        boolean exists = favoriteRepository.existsByUserIdAndInsectId(userId, insectId);
        Map<String, Object> result = new HashMap<>();

        if (exists) {
            favoriteRepository.deleteByUserIdAndInsectId(userId, insectId);
            result.put("isFavorite", false);
            result.put("message", "取消收藏成功");
        } else {
            if (!insectRepository.existsById(insectId)) {
                throw new RuntimeException("昆虫不存在");
            }
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setInsectId(insectId);
            favoriteRepository.save(favorite);
            result.put("isFavorite", true);
            result.put("message", "收藏成功");
        }

        return result;
    }

    public Page<Favorite> getUserFavorites(Pageable pageable) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return favoriteRepository.findByUserId(userId, pageable);
    }

    public Page<Insect> getUserFavoriteInsects(Pageable pageable) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        Page<Favorite> favoritePage = favoriteRepository.findByUserId(userId, pageable);
        List<Insect> insects = favoritePage.getContent().stream()
                .map(favorite -> insectRepository.findById(favorite.getInsectId()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
        return new PageImpl<>(insects, pageable, favoritePage.getTotalElements());
    }

    public boolean isFavorite(Long insectId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            return false;
        }
        return favoriteRepository.existsByUserIdAndInsectId(userId, insectId);
    }
}

