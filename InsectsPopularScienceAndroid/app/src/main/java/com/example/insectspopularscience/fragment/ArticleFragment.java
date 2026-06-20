package com.example.insectspopularscience.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.insectspopularscience.R;
import com.example.insectspopularscience.activity.ArticleDetailActivity;
import com.example.insectspopularscience.adapter.ArticleAdapter;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Article;
import com.example.insectspopularscience.model.PageData;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etSearch;
    private MaterialButton btnSearch;
    private ArticleAdapter adapter;
    private List<Article> articleList = new ArrayList<>();
    private ApiService apiService;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;
    private boolean isSearching = false;
    private String searchKeyword = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        
        apiService = RetrofitClient.getInstance(requireContext()).getApiService();
        
        initViews(view);
        setupRecyclerView();
        loadArticles(true);
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        etSearch = view.findViewById(R.id.et_search);
        btnSearch = view.findViewById(R.id.btn_search);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 0;
            hasMore = true;
            loadArticles(true);
        });

        btnSearch.setOnClickListener(v -> performSearch());

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
    }

    private void setupRecyclerView() {
        adapter = new ArticleAdapter(articleList, article -> {
            Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
            intent.putExtra("article_id", article.getId());
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
                    if (lastVisiblePosition >= articleList.size() - 3) {
                        loadArticles(false);
                    }
                }
            }
        });
    }

    private void performSearch() {
        searchKeyword = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchKeyword)) {
            isSearching = false;
            loadArticles(true);
        } else {
            isSearching = true;
            currentPage = 0;
            hasMore = true;
            loadArticles(true);
        }
    }

    private void loadArticles(boolean refresh) {
        if (isLoading) return;
        
        isLoading = true;
        if (refresh) {
            currentPage = 0;
            articleList.clear();
            adapter.notifyDataSetChanged();
        }

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(currentPage));
        params.put("size", "10");

        Call<ApiResponse<PageData<Article>>> call;
        if (isSearching && !TextUtils.isEmpty(searchKeyword)) {
            params.put("keyword", searchKeyword);
            call = apiService.searchArticles(params);
        } else {
            call = apiService.getArticles(params);
        }

        call.enqueue(new Callback<ApiResponse<PageData<Article>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<Article>>> call, Response<ApiResponse<PageData<Article>>> response) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageData<Article>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        PageData<Article> pageData = apiResponse.getData();
                        List<Article> newArticles = pageData.getContent();
                        if (newArticles != null) {
                            int startPosition = articleList.size();
                            articleList.addAll(newArticles);
                            adapter.notifyItemRangeInserted(startPosition, newArticles.size());
                            hasMore = !pageData.isLast();
                            currentPage++;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<Article>>> call, Throwable t) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

