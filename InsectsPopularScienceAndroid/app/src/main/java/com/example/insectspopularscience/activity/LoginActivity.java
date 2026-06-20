package com.example.insectspopularscience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.AuthResponse;
import com.example.insectspopularscience.util.StorageUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etEmail, etNickname;
    private Button btnSubmit;
    private TextView tvSwitch, tvSwitchPrefix, tvWelcome;
    private ImageView ivPasswordToggle;
    private android.view.View cardEmail, cardNickname;
    private boolean isLoginMode = true;
    private boolean isPasswordVisible = false;
    private ApiService apiService;
    private StorageUtil storageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = RetrofitClient.getInstance(this).getApiService();
        storageUtil = StorageUtil.getInstance(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etNickname = findViewById(R.id.et_nickname);
        btnSubmit = findViewById(R.id.btn_submit);
        tvSwitch = findViewById(R.id.tv_switch);
        tvSwitchPrefix = findViewById(R.id.tv_switch_prefix);
        tvWelcome = findViewById(R.id.tv_welcome);
        ivPasswordToggle = findViewById(R.id.iv_password_toggle);
        cardEmail = findViewById(R.id.card_email);
        cardNickname = findViewById(R.id.card_nickname);

        // 设置密码显示/隐藏切换
        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());

        updateUI();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // 隐藏密码
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivPasswordToggle.setImageResource(R.drawable.ic_visibility_off_24);
            isPasswordVisible = false;
        } else {
            // 显示密码
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivPasswordToggle.setImageResource(R.drawable.ic_visibility_24);
            isPasswordVisible = true;
        }
        // 将光标移到末尾
        etPassword.setSelection(etPassword.getText().length());
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(v -> {
            if (isLoginMode) {
                handleLogin();
            } else {
                handleRegister();
            }
        });

        tvSwitch.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateUI();
        });
    }

    private void updateUI() {
        if (isLoginMode) {
            // 登录模式
            cardEmail.setVisibility(View.GONE);
            cardNickname.setVisibility(View.GONE);
            btnSubmit.setText("登录");
            tvWelcome.setText("欢迎回来");
            tvSwitchPrefix.setText("还没有账号？");
            tvSwitch.setText("立即注册");
        } else {
            // 注册模式
            cardEmail.setVisibility(View.VISIBLE);
            cardNickname.setVisibility(View.VISIBLE);
            btnSubmit.setText("注册");
            tvWelcome.setText("创建账号");
            tvSwitchPrefix.setText("已有账号？");
            tvSwitch.setText("立即登录");
        }
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        btnSubmit.setEnabled(false);
        apiService.login(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                btnSubmit.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        AuthResponse authResponse = apiResponse.getData();
                        storageUtil.setToken(authResponse.getToken());
                        storageUtil.setUser(new Gson().toJson(authResponse));
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, apiResponse.getMessage() != null ? apiResponse.getMessage() : "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                btnSubmit.setEnabled(true);
                Toast.makeText(LoginActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        if (!TextUtils.isEmpty(email)) {
            request.put("email", email);
        }
        if (!TextUtils.isEmpty(nickname)) {
            request.put("nickname", nickname);
        }

        btnSubmit.setEnabled(false);
        apiService.register(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                btnSubmit.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        AuthResponse authResponse = apiResponse.getData();
                        storageUtil.setToken(authResponse.getToken());
                        storageUtil.setUser(new Gson().toJson(authResponse));
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, apiResponse.getMessage() != null ? apiResponse.getMessage() : "注册失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                btnSubmit.setEnabled(true);
                Toast.makeText(LoginActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

