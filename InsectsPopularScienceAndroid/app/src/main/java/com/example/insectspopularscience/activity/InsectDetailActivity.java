package com.example.insectspopularscience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.adapter.BannerAdapter;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Insect;
import com.example.insectspopularscience.util.ImageUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsectDetailActivity extends AppCompatActivity {
    private Long insectId;
    private ApiService apiService;
    private TextView tvName, tvScientificName, tvDescription, tvMorphologicalFeatures, 
                     tvLivingHabits, tvHabitat, tvDistribution;
    private ViewPager2 viewPager2;
    private FloatingActionButton fabFavorite;
    private boolean isFavorite = false;
    private Insect currentInsect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insect_detail);

        insectId = getIntent().getLongExtra("insect_id", 0);
        if (insectId == 0) {
            finish();
            return;
        }

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        loadInsectDetail();
        checkFavorite();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("昆虫详情");
        }

        tvName = findViewById(R.id.tv_name);
        tvScientificName = findViewById(R.id.tv_scientific_name);
        tvDescription = findViewById(R.id.tv_description);
        tvMorphologicalFeatures = findViewById(R.id.tv_morphological_features);
        tvLivingHabits = findViewById(R.id.tv_living_habits);
        tvHabitat = findViewById(R.id.tv_habitat);
        tvDistribution = findViewById(R.id.tv_distribution);
        viewPager2 = findViewById(R.id.view_pager_images);
        fabFavorite = findViewById(R.id.fab_favorite);

        fabFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void loadInsectDetail() {
        apiService.getInsectById(insectId).enqueue(new Callback<ApiResponse<Insect>>() {
            @Override
            public void onResponse(Call<ApiResponse<Insect>> call, Response<ApiResponse<Insect>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Insect> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Insect insect = apiResponse.getData();
                        updateUI(insect);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Insect>> call, Throwable t) {
                Toast.makeText(InsectDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Insect insect) {
        this.currentInsect = insect;
        
        tvName.setText(insect.getName());
        tvScientificName.setText(insect.getScientificName());
        tvDescription.setText(insect.getDescription());
        tvMorphologicalFeatures.setText(insect.getMorphologicalFeatures());
        tvLivingHabits.setText(insect.getLivingHabits());
        tvHabitat.setText(insect.getHabitat());
        tvDistribution.setText(insect.getDistribution());

        List<String> imageUrls = ImageUtil.parseImageUrls(insect.getImageUrls());
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String[] urls = imageUrls.toArray(new String[0]);
            BannerAdapter adapter = new BannerAdapter(urls);
            viewPager2.setAdapter(adapter);
            viewPager2.setOffscreenPageLimit(3);
        }
    }

    private void checkFavorite() {
        apiService.checkFavorite(insectId).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        isFavorite = apiResponse.getData();
                        updateFavoriteButton();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
            }
        });
    }

    private void toggleFavorite() {
        apiService.toggleFavorite(insectId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        isFavorite = !isFavorite;
                        updateFavoriteButton();
                        Toast.makeText(InsectDetailActivity.this, 
                                isFavorite ? "收藏成功" : "取消收藏成功", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(InsectDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteButton() {
        fabFavorite.setImageResource(isFavorite ? 
                android.R.drawable.star_big_on : android.R.drawable.star_big_off);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insect_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            shareInsect();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareInsect() {
        if (currentInsect == null) {
            Toast.makeText(this, "内容加载中，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder shareText = new StringBuilder();
        shareText.append("【").append(currentInsect.getName()).append("】\n\n");
        
        if (currentInsect.getScientificName() != null && !currentInsect.getScientificName().isEmpty()) {
            shareText.append("学名：").append(currentInsect.getScientificName()).append("\n\n");
        }
        
        if (currentInsect.getDescription() != null && !currentInsect.getDescription().isEmpty()) {
            shareText.append("简介：").append(currentInsect.getDescription()).append("\n\n");
        }
        
        if (currentInsect.getMorphologicalFeatures() != null && !currentInsect.getMorphologicalFeatures().isEmpty()) {
            shareText.append("形态特征：").append(currentInsect.getMorphologicalFeatures()).append("\n\n");
        }
        
        if (currentInsect.getLivingHabits() != null && !currentInsect.getLivingHabits().isEmpty()) {
            shareText.append("生活习性：").append(currentInsect.getLivingHabits()).append("\n\n");
        }
        
        if (currentInsect.getHabitat() != null && !currentInsect.getHabitat().isEmpty()) {
            shareText.append("栖息地：").append(currentInsect.getHabitat()).append("\n\n");
        }
        
        if (currentInsect.getDistribution() != null && !currentInsect.getDistribution().isEmpty()) {
            shareText.append("分布范围：").append(currentInsect.getDistribution()).append("\n\n");
        }
        
        shareText.append("—— 来自昆虫科普APP");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentInsect.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        
        try {
            startActivity(Intent.createChooser(shareIntent, "分享到"));
        } catch (Exception e) {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }
    }
}

