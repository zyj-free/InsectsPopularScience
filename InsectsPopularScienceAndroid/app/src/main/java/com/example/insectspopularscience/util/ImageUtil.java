package com.example.insectspopularscience.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
    private static final Gson gson = new Gson();

    public static List<String> parseImageUrls(String imageUrlsJson) {
        if (imageUrlsJson == null || imageUrlsJson.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            Type listType = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(imageUrlsJson, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static String getFirstImageUrl(String imageUrlsJson) {
        List<String> urls = parseImageUrls(imageUrlsJson);
        return urls.isEmpty() ? null : urls.get(0);
    }
}

