package com.example.insectspopularscience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.activity.InsectDetailActivity;
import com.example.insectspopularscience.adapter.InsectAdapter;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Insect;
import com.example.insectspopularscience.model.PageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private InsectAdapter adapter;
    private List<Insect> insectList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        apiService = RetrofitClient.getInstance(this).getApiService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("我的收藏");
        }

        recyclerView = findViewById(R.id.recycler_view);
        tvEmpty = findViewById(R.id.tv_empty);
        
        adapter = new InsectAdapter(insectList, insect -> {
            Intent intent = new Intent(this, InsectDetailActivity.class);
            intent.putExtra("insect_id", insect.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "100");

        apiService.getFavorites(params).enqueue(new Callback<ApiResponse<PageData<Insect>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<Insect>>> call, Response<ApiResponse<PageData<Insect>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageData<Insect>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        PageData<Insect> pageData = apiResponse.getData();
                        List<Insect> favorites = pageData.getContent();
                        insectList.clear();
                        if (favorites != null && !favorites.isEmpty()) {
                            insectList.addAll(favorites);
                            // 使用notifyDataSetChanged确保更新
                            adapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                            tvEmpty.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<Insect>>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 页面显示时刷新数据
        loadFavorites();
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

