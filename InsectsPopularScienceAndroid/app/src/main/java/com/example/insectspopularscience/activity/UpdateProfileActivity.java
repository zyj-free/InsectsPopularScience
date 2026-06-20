package com.example.insectspopularscience.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText etNickname, etEmail, etPhone;
    private MaterialButton btnSave;
    private ImageView ivAvatar;
    private FloatingActionButton fabEditAvatar;
    private ApiService apiService;
    private String selectedAvatarPath = "";
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        apiService = RetrofitClient.getInstance(this).getApiService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("修改资料");
        }

        etNickname = findViewById(R.id.et_nickname);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnSave = findViewById(R.id.btn_save);
        ivAvatar = findViewById(R.id.iv_avatar);
        fabEditAvatar = findViewById(R.id.fab_edit_avatar);

        // 初始化图片选择器
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            selectedAvatarPath = selectedImageUri.toString();
                            Glide.with(this)
                                    .load(selectedImageUri)
                                    .circleCrop()
                                    .placeholder(R.drawable.head_icon)
                                    .error(R.drawable.head_icon)
                                    .into(ivAvatar);
                            Toast.makeText(this, "头像已选择", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // 设置头像点击事件
        fabEditAvatar.setOnClickListener(v -> selectAvatar());
        ivAvatar.setOnClickListener(v -> selectAvatar());

        loadUserInfo();
        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void selectAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void loadUserInfo() {
        apiService.getCurrentUser().enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        User user = apiResponse.getData();
                        etNickname.setText(user.getNickname());
                        etEmail.setText(user.getEmail());
                        etPhone.setText(user.getPhone());
                        
                        // 加载头像
                        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                            // 如果是本地路径（file://开头）
                            if (user.getAvatar().startsWith("file://") || user.getAvatar().startsWith("/")) {
                                try {
                                    Uri avatarUri = Uri.parse(user.getAvatar().startsWith("file://") 
                                            ? user.getAvatar() 
                                            : "file://" + user.getAvatar());
                                    Glide.with(UpdateProfileActivity.this)
                                            .load(avatarUri)
                                            .circleCrop()
                                            .placeholder(R.drawable.head_icon)
                                            .error(R.drawable.head_icon)
                                            .into(ivAvatar);
                                } catch (Exception e) {
                                    Glide.with(UpdateProfileActivity.this)
                                            .load(R.drawable.head_icon)
                                            .circleCrop()
                                            .into(ivAvatar);
                                }
                            } else {
                                // 网络图片或Content URI
                                Glide.with(UpdateProfileActivity.this)
                                        .load(user.getAvatar())
                                        .circleCrop()
                                        .placeholder(R.drawable.head_icon)
                                        .error(R.drawable.head_icon)
                                        .into(ivAvatar);
                            }
                        } else {
                            Glide.with(UpdateProfileActivity.this)
                                    .load(R.drawable.head_icon)
                                    .circleCrop()
                                    .into(ivAvatar);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
            }
        });
    }

    private void updateProfile() {
        Map<String, String> request = new HashMap<>();
        String nickname = etNickname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (!TextUtils.isEmpty(nickname)) {
            request.put("nickname", nickname);
        }
        if (!TextUtils.isEmpty(email)) {
            request.put("email", email);
        }
        if (!TextUtils.isEmpty(phone)) {
            request.put("phone", phone);
        }
        // 如果有选择新头像，添加avatar字段
        if (!TextUtils.isEmpty(selectedAvatarPath)) {
            request.put("avatar", selectedAvatarPath);
        }

        btnSave.setEnabled(false);
        apiService.updateProfile(request).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                btnSave.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(UpdateProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, 
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "修改失败", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(UpdateProfileActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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

