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

public class EditShareActivity extends AppCompatActivity {
    private Long shareId;
    private EditText etTitle, etImageUrl, etDescription;
    private ImageView ivPreview;
    private MaterialButton btnUpdate, btnRemoveImage;
    private TextView tvCharCount;
    private FrameLayout flImagePreview;
    private MaterialCardView cardImagePlaceholder;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_share);

        shareId = getIntent().getLongExtra("share_id", 0);
        if (shareId == 0) {
            finish();
            return;
        }

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        loadShareDetail();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("编辑分享");
        }

        etTitle = findViewById(R.id.et_title);
        etImageUrl = findViewById(R.id.et_image_url);
        etDescription = findViewById(R.id.et_description);
        ivPreview = findViewById(R.id.iv_preview);
        btnUpdate = findViewById(R.id.btn_publish);
        btnRemoveImage = findViewById(R.id.btn_remove_image);
        tvCharCount = findViewById(R.id.tv_char_count);
        flImagePreview = findViewById(R.id.fl_image_preview);
        cardImagePlaceholder = findViewById(R.id.card_image_placeholder);

        btnUpdate.setText("更新");

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
                updateUpdateButtonState();
            }
        });

        // 标题输入监听，更新按钮状态
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateUpdateButtonState();
            }
        });

        btnUpdate.setOnClickListener(v -> updateShare());
        
        // 初始化字符计数
        tvCharCount.setText("0/1000");
        updateUpdateButtonState();
    }

    private void loadShareDetail() {
        apiService.getShareById(shareId).enqueue(new Callback<ApiResponse<Share>>() {
            @Override
            public void onResponse(Call<ApiResponse<Share>> call, Response<ApiResponse<Share>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Share> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Share share = apiResponse.getData();
                        etTitle.setText(share.getTitle());
                        etDescription.setText(share.getDescription());
                        if (share.getImageUrl() != null && !share.getImageUrl().isEmpty()) {
                            etImageUrl.setText(share.getImageUrl());
                            showImagePreview(share.getImageUrl());
                        }
                        updateCharCount();
                        updateUpdateButtonState();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Share>> call, Throwable t) {
                Toast.makeText(EditShareActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateShare() {
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
        } else {
            request.put("imageUrl", "");
        }
        request.put("description", description);

        btnUpdate.setEnabled(false);
        apiService.updateShare(shareId, request).enqueue(new Callback<ApiResponse<Share>>() {
            @Override
            public void onResponse(Call<ApiResponse<Share>> call, Response<ApiResponse<Share>> response) {
                btnUpdate.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Share> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(EditShareActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditShareActivity.this, 
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "更新失败", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Share>> call, Throwable t) {
                btnUpdate.setEnabled(true);
                Toast.makeText(EditShareActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void updateCharCount() {
        int length = etDescription.getText().length();
        tvCharCount.setText(length + "/1000");
        if (length > 900) {
            tvCharCount.setTextColor(getResources().getColor(R.color.red_error, getTheme()));
        } else {
            tvCharCount.setTextColor(getResources().getColor(R.color.text_secondary, getTheme()));
        }
    }

    private void updateUpdateButtonState() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        boolean canUpdate = !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description);
        btnUpdate.setEnabled(canUpdate);
        btnUpdate.setAlpha(canUpdate ? 1.0f : 0.5f);
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

