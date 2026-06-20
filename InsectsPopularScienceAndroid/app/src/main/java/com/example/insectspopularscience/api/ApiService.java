package com.example.insectspopularscience.api;

import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Article;
import com.example.insectspopularscience.model.AuthResponse;
import com.example.insectspopularscience.model.Comment;
import com.example.insectspopularscience.model.Insect;
import com.example.insectspopularscience.model.PageData;
import com.example.insectspopularscience.model.Share;
import com.example.insectspopularscience.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    // 认证相关
    @POST("/api/auth/register")
    Call<ApiResponse<AuthResponse>> register(@Body Map<String, String> request);

    @POST("/api/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body Map<String, String> request);

    @GET("/api/auth/me")
    Call<ApiResponse<User>> getCurrentUser();

    @PUT("/api/auth/profile")
    Call<ApiResponse<User>> updateProfile(@Body Map<String, String> request);

    @POST("/api/auth/change-password")
    Call<ApiResponse<Void>> changePassword(@Body Map<String, String> request);

    // 昆虫相关
    @GET("/api/insects/list")
    Call<ApiResponse<PageData<Insect>>> getInsects(@QueryMap Map<String, String> params);

    @GET("/api/insects/{id}")
    Call<ApiResponse<Insect>> getInsectById(@Path("id") Long id);

    @GET("/api/insects/search")
    Call<ApiResponse<PageData<Insect>>> searchInsects(@QueryMap Map<String, String> params);

    @GET("/api/insects/popular")
    Call<ApiResponse<java.util.List<Insect>>> getPopularInsects();

    // 文章相关
    @GET("/api/articles/list")
    Call<ApiResponse<PageData<Article>>> getArticles(@QueryMap Map<String, String> params);

    @GET("/api/articles/{id}")
    Call<ApiResponse<Article>> getArticleById(@Path("id") Long id);

    @GET("/api/articles/recommend")
    Call<ApiResponse<java.util.List<Article>>> getRecommendArticles();

    @GET("/api/articles/search")
    Call<ApiResponse<PageData<Article>>> searchArticles(@QueryMap Map<String, String> params);

    // 收藏相关
    @POST("/api/favorites/toggle/{insectId}")
    Call<ApiResponse<Object>> toggleFavorite(@Path("insectId") Long insectId);

    @GET("/api/favorites/list")
    Call<ApiResponse<PageData<Insect>>> getFavorites(@QueryMap Map<String, String> params);

    @GET("/api/favorites/check/{insectId}")
    Call<ApiResponse<Boolean>> checkFavorite(@Path("insectId") Long insectId);

    // 分享相关
    @POST("/api/shares/create")
    Call<ApiResponse<Share>> createShare(@Body Map<String, Object> request);

    @GET("/api/shares/list")
    Call<ApiResponse<PageData<Share>>> getShares(@QueryMap Map<String, String> params);

    @GET("/api/shares/my")
    Call<ApiResponse<PageData<Share>>> getMyShares(@QueryMap Map<String, String> params);

    @GET("/api/shares/{id}")
    Call<ApiResponse<Share>> getShareById(@Path("id") Long id);

    @PUT("/api/shares/{id}")
    Call<ApiResponse<Share>> updateShare(@Path("id") Long id, @Body Map<String, Object> request);

    @DELETE("/api/shares/{id}")
    Call<ApiResponse<Void>> deleteShare(@Path("id") Long id);

    // 评论相关
    @POST("/api/comments/create")
    Call<ApiResponse<Comment>> createComment(@Body Map<String, Object> request);

    @GET("/api/comments/share/{shareId}")
    Call<ApiResponse<PageData<Comment>>> getCommentsByShareId(@Path("shareId") Long shareId, @QueryMap Map<String, String> params);
}

