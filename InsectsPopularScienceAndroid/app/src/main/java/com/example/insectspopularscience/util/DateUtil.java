package com.example.insectspopularscience.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            if (date == null) return "";
            
            long diff = System.currentTimeMillis() - date.getTime();
            long minutes = diff / 60000;
            long hours = diff / 3600000;
            long days = diff / 86400000;
            
            if (minutes < 1) return "刚刚";
            if (minutes < 60) return minutes + "分钟前";
            if (hours < 24) return hours + "小时前";
            if (days < 7) return days + "天前";
            
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
}

