package com.example.insectspopularscience.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Article;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleDetailActivity extends AppCompatActivity {
    private Long articleId;
    private ApiService apiService;
    private TextView tvTitle, tvAuthor, tvViewCount, tvContent;
    private ImageView ivCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        articleId = getIntent().getLongExtra("article_id", 0);
        if (articleId == 0) {
            finish();
            return;
        }

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        loadArticleDetail();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("文章详情");
        }

        tvTitle = findViewById(R.id.tv_title);
        tvAuthor = findViewById(R.id.tv_author);
        tvViewCount = findViewById(R.id.tv_view_count);
        tvContent = findViewById(R.id.tv_content);
        ivCover = findViewById(R.id.iv_cover);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadArticleDetail() {
        apiService.getArticleById(articleId).enqueue(new Callback<ApiResponse<Article>>() {
            @Override
            public void onResponse(Call<ApiResponse<Article>> call, Response<ApiResponse<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Article> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Article article = apiResponse.getData();
                        displayArticle(article);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Article>> call, Throwable t) {
                // Handle error
            }
        });
    }

    private void displayArticle(Article article) {
        tvTitle.setText(article.getTitle());
        tvAuthor.setText("作者: " + (article.getAuthor() != null ? article.getAuthor() : "未知"));
        tvViewCount.setText("浏览 " + (article.getViewCount() != null ? article.getViewCount() : 0));
        tvContent.setText(article.getContent() != null ? article.getContent() : "");

        if (article.getCoverImage() != null && !article.getCoverImage().isEmpty()) {
            Glide.with(this)
                    .load(article.getCoverImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivCover);
            ivCover.setVisibility(android.view.View.VISIBLE);
        } else {
            ivCover.setVisibility(android.view.View.GONE);
        }
    }
}

