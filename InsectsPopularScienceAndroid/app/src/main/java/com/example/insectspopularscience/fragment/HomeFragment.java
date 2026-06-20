package com.example.insectspopularscience.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.activity.InsectDetailActivity;
import com.example.insectspopularscience.adapter.InsectAdapter;
import com.example.insectspopularscience.adapter.BannerAdapter;
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

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etSearch;
    private MaterialButton btnSearch;
    private ViewPager2 viewPager2;
    private android.os.Handler autoScrollHandler;
    
    private InsectAdapter adapter;
    private List<Insect> insectList = new ArrayList<>();
    private ApiService apiService;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;
    private boolean isSearching = false;
    private String searchKeyword = "";

    private String[] bannerImages = {
        "https://ns-strategy.cdn.bcebos.com/ns-strategy/upload/fc_big_pic/part-00386-1180.jpg",
        "https://img1.baidu.com/it/u=3051340333,887555584&fm=253&fmt=auto&app=138&f=JPEG?w=786&h=500",
        "https://img2.baidu.com/it/u=2158841684,1585200651&fm=253&fmt=auto&app=138&f=JPEG?w=751&h=500",
        "https://img1.baidu.com/it/u=3554263137,54827429&fm=253&fmt=auto&app=138&f=JPEG?w=753&h=500"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        apiService = RetrofitClient.getInstance(requireContext()).getApiService();
        autoScrollHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        
        initViews(view);
        setupRecyclerView();
        setupBanner();
        loadInsects(true);
        
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        etSearch = view.findViewById(R.id.et_search);
        btnSearch = view.findViewById(R.id.btn_search);
        viewPager2 = view.findViewById(R.id.view_pager_banner);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 0;
            hasMore = true;
            loadInsects(true);
        });

        btnSearch.setOnClickListener(v -> performSearch());
        
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
    }

    private void setupBanner() {
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        viewPager2.setAdapter(bannerAdapter);
        viewPager2.setOffscreenPageLimit(3);
        
        // 添加轮播图指示器
        if (getView() != null) {
            com.google.android.material.tabs.TabLayout tabLayout = getView().findViewById(R.id.tab_layout_indicator);
            if (tabLayout != null && bannerImages.length > 1) {
                new com.google.android.material.tabs.TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                }).attach();
            }
        }
        
        // 页面切换回调
        viewPager2.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 重置自动播放
                stopAutoScroll();
                startAutoScroll();
            }
        });
        
        // 开始自动播放
        startAutoScroll();
    }
    
    private void startAutoScroll() {
        if (autoScrollHandler != null && viewPager2 != null && bannerImages.length > 1) {
            stopAutoScroll();
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
        }
    }
    
    private void stopAutoScroll() {
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }
    
    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2 != null && bannerImages.length > 1 && autoScrollHandler != null) {
                int currentItem = viewPager2.getCurrentItem();
                int nextItem = (currentItem + 1) % bannerImages.length;
                viewPager2.setCurrentItem(nextItem, true);
                autoScrollHandler.postDelayed(this, 3000);
            }
        }
    };

    private void setupRecyclerView() {
        adapter = new InsectAdapter(insectList, insect -> {
            Intent intent = new Intent(getContext(), InsectDetailActivity.class);
            intent.putExtra("insect_id", insect.getId());
            startActivity(intent);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && hasMore) {
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= insectList.size() - 3) {
                        loadInsects(false);
                    }
                }
            }
        });
    }

    private void performSearch() {
        searchKeyword = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchKeyword)) {
            isSearching = false;
            loadInsects(true);
        } else {
            isSearching = true;
            currentPage = 0;
            hasMore = true;
            loadInsects(true);
        }
    }

    private void loadInsects(boolean refresh) {
        if (isLoading) return;
        
        isLoading = true;
        if (refresh) {
            currentPage = 0;
            insectList.clear();
            adapter.notifyDataSetChanged();
        }

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(currentPage));
        params.put("size", "10");

        Call<ApiResponse<PageData<Insect>>> call;
        if (isSearching && !TextUtils.isEmpty(searchKeyword)) {
            params.put("keyword", searchKeyword);
            call = apiService.searchInsects(params);
        } else {
            call = apiService.getInsects(params);
        }

        call.enqueue(new Callback<ApiResponse<PageData<Insect>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<Insect>>> call, Response<ApiResponse<PageData<Insect>>> response) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageData<Insect>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        PageData<Insect> pageData = apiResponse.getData();
                        List<Insect> newInsects = pageData.getContent();
                        if (newInsects != null) {
                            int startPosition = insectList.size();
                            insectList.addAll(newInsects);
                            adapter.notifyItemRangeInserted(startPosition, newInsects.size());
                            hasMore = !pageData.isLast();
                            currentPage++;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<Insect>>> call, Throwable t) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

