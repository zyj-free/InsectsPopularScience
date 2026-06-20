package com.example.insectspopularscience.service;

import com.example.insectspopularscience.entity.Share;
import com.example.insectspopularscience.entity.User;
import com.example.insectspopularscience.repository.ShareRepository;
import com.example.insectspopularscience.repository.UserRepository;
import com.example.insectspopularscience.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Share createShare(Long insectId, String title, String imageUrl, String description) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Share share = new Share();
        share.setUserId(userId);
        share.setUser(user);
        share.setInsectId(insectId);
        share.setTitle(title);
        share.setImageUrl(imageUrl);
        share.setDescription(description);
        return shareRepository.save(share);
    }

    public Page<Share> getAllShares(Pageable pageable) {
        return shareRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Share> getUserShares(Pageable pageable) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return shareRepository.findByUserId(userId, pageable);
    }

    public Optional<Share> getShareById(Long id) {
        return shareRepository.findById(id);
    }

    @Transactional
    public Share updateShare(Long shareId, Long insectId, String title, String imageUrl, String description) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Share share = shareRepository.findById(shareId)
                .orElseThrow(() -> new RuntimeException("分享不存在"));

        if (!share.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此分享");
        }

        if (insectId != null) {
            share.setInsectId(insectId);
        }
        if (title != null) {
            share.setTitle(title);
        }
        if (imageUrl != null) {
            share.setImageUrl(imageUrl);
        }
        if (description != null) {
            share.setDescription(description);
        }

        return shareRepository.save(share);
    }

    @Transactional
    public void deleteShare(Long shareId) {
        Long userId = UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Share share = shareRepository.findById(shareId)
                .orElseThrow(() -> new RuntimeException("分享不存在"));

        if (!share.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此分享");
        }

        shareRepository.delete(share);
    }
}

