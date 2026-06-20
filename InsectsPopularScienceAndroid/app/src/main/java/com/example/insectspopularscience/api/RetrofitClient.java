package com.example.insectspopularscience.api;

import android.content.Context;

import com.example.insectspopularscience.util.StorageUtil;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080";
    private static RetrofitClient instance;
    private Retrofit retrofit;
    private Context context;

    private RetrofitClient(Context context) {
        this.context = context.getApplicationContext();
        initRetrofit();
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    private void initRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        
        // 添加日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        // 添加认证拦截器
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            String token = StorageUtil.getInstance(context).getToken();
            Request.Builder requestBuilder = original.newBuilder();
            if (token != null && !token.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + token);
            }
            requestBuilder.header("Content-Type", "application/json");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}

