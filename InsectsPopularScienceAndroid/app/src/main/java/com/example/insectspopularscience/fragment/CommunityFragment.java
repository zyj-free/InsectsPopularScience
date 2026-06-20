package com.example.insectspopularscience.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.activity.PublishShareActivity;
import com.example.insectspopularscience.activity.ShareDetailActivity;
import com.example.insectspopularscience.adapter.ShareAdapter;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.PageData;
import com.example.insectspopularscience.model.Share;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabPublish;
    private com.google.android.material.button.MaterialButton btnPublishTop;
    private LinearLayout llEmpty;
    private ShareAdapter adapter;
    private List<Share> shareList = new ArrayList<>();
    private ApiService apiService;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        
        apiService = RetrofitClient.getInstance(requireContext()).getApiService();
        
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        fabPublish = view.findViewById(R.id.fab_publish);
        btnPublishTop = view.findViewById(R.id.btn_publish_top);
        llEmpty = view.findViewById(R.id.ll_empty);
        
        adapter = new ShareAdapter(shareList, share -> {
            Intent intent = new Intent(getContext(), ShareDetailActivity.class);
            intent.putExtra("share_id", share.getId());
            startActivity(intent);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        
        // 添加滚动监听，实现上拉加载更多
        recyclerView.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                androidx.recyclerview.widget.LinearLayoutManager layoutManager = 
                        (androidx.recyclerview.widget.LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    
                    if (!isLoading && hasMore && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount 
                            && firstVisibleItemPosition >= 0) {
                        loadShares(false);
                    }
                }
            }
        });
        
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 0;
            hasMore = true;
            loadShares(true);
        });
        
        fabPublish.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PublishShareActivity.class));
        });
        
        btnPublishTop.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PublishShareActivity.class));
        });
        
        loadShares(true);
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 从发布页面返回时刷新列表
        loadShares(true);
    }

    private void loadShares(boolean refresh) {
        if (isLoading) return;
        
        isLoading = true;
        if (refresh) {
            currentPage = 0;
            shareList.clear();
            adapter.notifyDataSetChanged();
        }

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(currentPage));
        params.put("size", "10");

        apiService.getShares(params).enqueue(new Callback<ApiResponse<PageData<Share>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<Share>>> call, Response<ApiResponse<PageData<Share>>> response) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageData<Share>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        PageData<Share> pageData = apiResponse.getData();
                        List<Share> newShares = pageData.getContent();
                        if (newShares != null) {
                            if (refresh) {
                                shareList.clear();
                                shareList.addAll(newShares);
                                adapter.notifyDataSetChanged();
                            } else {
                                int startPosition = shareList.size();
                                shareList.addAll(newShares);
                                adapter.notifyItemRangeInserted(startPosition, newShares.size());
                            }
                            hasMore = !pageData.isLast();
                            currentPage++;
                            
                            // 更新空状态显示
                            if (shareList.isEmpty()) {
                                llEmpty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                llEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<Share>>> call, Throwable t) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

