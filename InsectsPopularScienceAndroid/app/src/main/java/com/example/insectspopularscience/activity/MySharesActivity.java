package com.example.insectspopularscience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.activity.ShareDetailActivity;
import com.example.insectspopularscience.adapter.ShareAdapter;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.PageData;
import com.example.insectspopularscience.model.Share;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySharesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShareAdapter adapter;
    private List<Share> shareList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shares);

        apiService = RetrofitClient.getInstance(this).getApiService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("我的分享");
        }

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new ShareAdapter(shareList, share -> {
            Intent intent = new Intent(this, ShareDetailActivity.class);
            intent.putExtra("share_id", share.getId());
            startActivity(intent);
        });
        
        // 显示编辑和删除按钮
        adapter.setShowActionButtons(true);
        adapter.setOnEditClickListener(share -> {
            Intent intent = new Intent(this, EditShareActivity.class);
            intent.putExtra("share_id", share.getId());
            startActivity(intent);
        });
        adapter.setOnDeleteClickListener(share -> {
            showDeleteConfirmDialog(share);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadMyShares();
    }

    private void showDeleteConfirmDialog(Share share) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这条分享吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    deleteShare(share.getId());
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteShare(Long shareId) {
        apiService.deleteShare(shareId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(MySharesActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        loadMyShares();
                    } else {
                        Toast.makeText(MySharesActivity.this, 
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "删除失败", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(MySharesActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyShares();
    }

    private void loadMyShares() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "100");

        apiService.getMyShares(params).enqueue(new Callback<ApiResponse<PageData<Share>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<Share>>> call, Response<ApiResponse<PageData<Share>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageData<Share>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        shareList.clear();
                        shareList.addAll(apiResponse.getData().getContent());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<Share>>> call, Throwable t) {
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

