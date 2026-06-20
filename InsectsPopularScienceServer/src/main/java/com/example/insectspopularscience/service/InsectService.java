package com.example.insectspopularscience.service;

import com.example.insectspopularscience.entity.Insect;
import com.example.insectspopularscience.repository.InsectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsectService {

    @Autowired
    private InsectRepository insectRepository;

    public Page<Insect> getAllInsects(Pageable pageable) {
        return insectRepository.findAll(pageable);
    }

    public Page<Insect> getInsectsByCategory(Long categoryId, Pageable pageable) {
        return insectRepository.findByCategoryId(categoryId, pageable);
    }

    public Optional<Insect> getInsectById(Long id) {
        Optional<Insect> insect = insectRepository.findById(id);
        if (insect.isPresent()) {
            Insect i = insect.get();
            i.setViewCount(i.getViewCount() + 1);
            insectRepository.save(i);
        }
        return insect;
    }

    public Page<Insect> searchInsects(String keyword, Pageable pageable) {
        return insectRepository.searchInsects(keyword, pageable);
    }

    public List<Insect> getPopularInsects() {
        return insectRepository.findTop10ByOrderByViewCountDesc();
    }
}

