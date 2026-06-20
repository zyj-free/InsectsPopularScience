package com.example.insectspopularscience.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.activity.ChangePasswordActivity;
import com.example.insectspopularscience.activity.LoginActivity;
import com.example.insectspopularscience.activity.MyFavoritesActivity;
import com.example.insectspopularscience.activity.MySharesActivity;
import com.example.insectspopularscience.activity.UpdateProfileActivity;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.User;
import com.example.insectspopularscience.util.StorageUtil;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private CircleImageView ivAvatar;
    private TextView tvNickname;
    private TextView tvUsername;
    private TextView tvEmail;
    private Button btnLogout;
    private ApiService apiService;
    private StorageUtil storageUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        apiService = RetrofitClient.getInstance(requireContext()).getApiService();
        storageUtil = StorageUtil.getInstance(requireContext());
        
        initViews(view);
        loadUserInfo();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        btnLogout = view.findViewById(R.id.btn_logout);
        
        view.findViewById(R.id.ll_my_favorites).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MyFavoritesActivity.class));
        });
        
        view.findViewById(R.id.ll_my_shares).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MySharesActivity.class));
        });
        
        view.findViewById(R.id.ll_update_profile).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), UpdateProfileActivity.class));
        });
        
        view.findViewById(R.id.ll_change_password).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ChangePasswordActivity.class));
        });
        
        btnLogout.setOnClickListener(v -> {
            storageUtil.clear();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });
    }

    private void loadUserInfo() {
        apiService.getCurrentUser().enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        User user = apiResponse.getData();
                        updateUI(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Toast.makeText(getContext(), "加载用户信息失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User user) {
        tvNickname.setText(user.getNickname() != null ? user.getNickname() : user.getUsername());
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(user.getAvatar())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivAvatar);
        }
    }
}

