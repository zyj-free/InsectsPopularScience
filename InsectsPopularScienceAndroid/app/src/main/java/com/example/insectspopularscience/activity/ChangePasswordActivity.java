package com.example.insectspopularscience.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private TextView tvPasswordStrength, tvPasswordMatch;
    private MaterialButton btnSave;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        apiService = RetrofitClient.getInstance(this).getApiService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("修改密码");
        }

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        tvPasswordStrength = findViewById(R.id.tv_password_strength);
        tvPasswordMatch = findViewById(R.id.tv_password_match);
        btnSave = findViewById(R.id.btn_save);

        // 监听新密码输入，显示密码强度
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
                checkPasswordMatch();
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 监听确认密码输入，显示匹配状态
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordMatch();
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 监听原密码输入
        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSave.setOnClickListener(v -> changePassword());
        updateButtonState();
    }

    private void updatePasswordStrength(String password) {
        if (TextUtils.isEmpty(password)) {
            tvPasswordStrength.setVisibility(View.GONE);
            return;
        }

        tvPasswordStrength.setVisibility(View.VISIBLE);
        int len = password.length();
        if (len < 6) {
            tvPasswordStrength.setText("密码长度至少6位");
            tvPasswordStrength.setTextColor(0xFFFF5722);
        } else if (len < 8) {
            tvPasswordStrength.setText("密码强度：弱");
            tvPasswordStrength.setTextColor(0xFFFF9800);
        } else if (len < 12) {
            tvPasswordStrength.setText("密码强度：中");
            tvPasswordStrength.setTextColor(0xFF4CAF50);
        } else {
            tvPasswordStrength.setText("密码强度：强");
            tvPasswordStrength.setTextColor(0xFF4CAF50);
        }
    }

    private void checkPasswordMatch() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(confirmPassword)) {
            tvPasswordMatch.setVisibility(View.GONE);
            return;
        }

        tvPasswordMatch.setVisibility(View.VISIBLE);
        if (newPassword.equals(confirmPassword)) {
            tvPasswordMatch.setText("✓ 密码匹配");
            tvPasswordMatch.setTextColor(0xFF4CAF50);
        } else {
            tvPasswordMatch.setText("✗ 密码不一致");
            tvPasswordMatch.setTextColor(0xFFFF5722);
        }
    }

    private void updateButtonState() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean canChange = !TextUtils.isEmpty(oldPassword) &&
                !TextUtils.isEmpty(newPassword) &&
                newPassword.length() >= 6 &&
                !TextUtils.isEmpty(confirmPassword) &&
                newPassword.equals(confirmPassword);

        btnSave.setEnabled(canChange);
        btnSave.setAlpha(canChange ? 1.0f : 0.6f);
    }

    private void changePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> request = new HashMap<>();
        request.put("oldPassword", oldPassword);
        request.put("newPassword", newPassword);

        btnSave.setEnabled(false);
        apiService.changePassword(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                btnSave.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, 
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "修改失败", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(ChangePasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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

