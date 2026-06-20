package com.example.insectspopularscience.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Share;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishShareActivity extends AppCompatActivity {
    private EditText etTitle, etImageUrl, etDescription;
    private ImageView ivPreview;
    private MaterialButton btnPublish, btnRemoveImage;
    private TextView tvCharCount;
    private FrameLayout flImagePreview;
    private MaterialCardView cardImagePlaceholder;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_share);

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("发布分享");
        }

        etTitle = findViewById(R.id.et_title);
        etImageUrl = findViewById(R.id.et_image_url);
        etDescription = findViewById(R.id.et_description);
        ivPreview = findViewById(R.id.iv_preview);
        btnPublish = findViewById(R.id.btn_publish);
        btnRemoveImage = findViewById(R.id.btn_remove_image);
        tvCharCount = findViewById(R.id.tv_char_count);
        flImagePreview = findViewById(R.id.fl_image_preview);
        cardImagePlaceholder = findViewById(R.id.card_image_placeholder);

        // 图片URL输入监听，实时预览
        etImageUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String url = s.toString().trim();
                if (!TextUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
                    showImagePreview(url);
                } else {
                    hideImagePreview();
                }
            }
        });

        // 删除图片按钮
        btnRemoveImage.setOnClickListener(v -> {
            etImageUrl.setText("");
            hideImagePreview();
        });

        // 描述字符计数
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tvCharCount.setText(length + "/1000");
                if (length > 900) {
                    tvCharCount.setTextColor(getResources().getColor(R.color.red_error, getTheme()));
                } else {
                    tvCharCount.setTextColor(getResources().getColor(R.color.text_secondary, getTheme()));
                }
                updatePublishButtonState();
            }
        });

        // 标题输入监听，更新发布按钮状态
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePublishButtonState();
            }
        });

        btnPublish.setOnClickListener(v -> publishShare());
        
        // 初始化字符计数
        tvCharCount.setText("0/1000");
        updatePublishButtonState();
    }

    private void showImagePreview(String url) {
        flImagePreview.setVisibility(View.VISIBLE);
        cardImagePlaceholder.setVisibility(View.GONE);
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.head_icon)
                .error(R.drawable.head_icon)
                .centerCrop()
                .into(ivPreview);
    }

    private void hideImagePreview() {
        flImagePreview.setVisibility(View.GONE);
        cardImagePlaceholder.setVisibility(View.VISIBLE);
        ivPreview.setImageDrawable(null);
    }

    private void updatePublishButtonState() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        boolean canPublish = !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description);
        btnPublish.setEnabled(canPublish);
        btnPublish.setAlpha(canPublish ? 1.0f : 0.5f);
    }

    private void publishShare() {
        String title = etTitle.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            etTitle.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "请输入描述内容", Toast.LENGTH_SHORT).show();
            etDescription.requestFocus();
            return;
        }

        Map<String, Object> request = new HashMap<>();
        request.put("title", title);
        if (!TextUtils.isEmpty(imageUrl)) {
            request.put("imageUrl", imageUrl);
        }
        request.put("description", description);

        btnPublish.setEnabled(false);
        apiService.createShare(request).enqueue(new Callback<ApiResponse<Share>>() {
            @Override
            public void onResponse(Call<ApiResponse<Share>> call, Response<ApiResponse<Share>> response) {
                btnPublish.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Share> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(PublishShareActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PublishShareActivity.this, 
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "发布失败", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Share>> call, Throwable t) {
                btnPublish.setEnabled(true);
                Toast.makeText(PublishShareActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

