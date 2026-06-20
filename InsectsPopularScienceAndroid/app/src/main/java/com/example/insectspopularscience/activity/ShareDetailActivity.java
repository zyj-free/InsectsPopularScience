package com.example.insectspopularscience.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.adapter.CommentAdapter;
import com.example.insectspopularscience.api.ApiService;
import com.example.insectspopularscience.api.RetrofitClient;
import com.example.insectspopularscience.model.ApiResponse;
import com.example.insectspopularscience.model.Comment;
import com.example.insectspopularscience.model.PageData;
import com.example.insectspopularscience.model.Share;
import com.example.insectspopularscience.util.DateUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareDetailActivity extends AppCompatActivity {
    private Long shareId;
    private ApiService apiService;
    private CircleImageView ivAvatar;
    private TextView tvUsername, tvTime, tvTitle, tvDescription;
    private ImageView ivImage;
    private RecyclerView recyclerView;
    private EditText etComment;
    private MaterialButton btnSend;
    private CommentAdapter adapter;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        shareId = getIntent().getLongExtra("share_id", 0);
        if (shareId == 0) {
            finish();
            return;
        }

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        loadShareDetail();
        loadComments();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("分享详情");
        }

        ivAvatar = findViewById(R.id.iv_avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvTime = findViewById(R.id.tv_time);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        ivImage = findViewById(R.id.iv_image);
        recyclerView = findViewById(R.id.recycler_view);
        etComment = findViewById(R.id.et_comment);
        btnSend = findViewById(R.id.btn_send);

        adapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendComment());
    }

    private void loadShareDetail() {
        apiService.getShareById(shareId).enqueue(new Callback<ApiResponse<Share>>() {
            @Override
            public void onResponse(Call<ApiResponse<Share>> call, Response<ApiResponse<Share>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Share> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Share share = apiResponse.getData();
                        updateUI(share);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Share>> call, Throwable t) {
                Toast.makeText(ShareDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Share share) {
        tvTitle.setText(share.getTitle());
        tvDescription.setText(share.getDescription());
        tvTime.setText(DateUtil.formatDate(share.getCreatedAt()));

        if (share.getUser() != null) {
            tvUsername.setText(share.getUser().getNickname() != null ? 
                    share.getUser().getNickname() : share.getUser().getUsername());
            
            // 加载头像，如果有数据库头像就使用，否则使用默认头像head_icon
            String avatarUrl = share.getUser().getAvatar();
            if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
                // 判断是本地路径还是网络URL
                if (avatarUrl.startsWith("http://") || avatarUrl.startsWith("https://")) {
                    // 网络图片
                    Glide.with(this)
                            .load(avatarUrl)
                            .placeholder(R.drawable.head_icon)
                            .error(R.drawable.head_icon)
                            .circleCrop()
                            .into(ivAvatar);
                } else {
                    // 本地路径
                    try {
                        android.net.Uri avatarUri = android.net.Uri.parse(avatarUrl.startsWith("file://") 
                                ? avatarUrl 
                                : "file://" + avatarUrl);
                        Glide.with(this)
                                .load(avatarUri)
                                .placeholder(R.drawable.head_icon)
                                .error(R.drawable.head_icon)
                                .circleCrop()
                                .into(ivAvatar);
                    } catch (Exception e) {
                        // 解析失败，使用默认头像
                        Glide.with(this)
                                .load(R.drawable.head_icon)
                                .circleCrop()
                                .into(ivAvatar);
                    }
                }
            } else {
                // 没有头像，使用默认头像head_icon
                Glide.with(this)
                        .load(R.drawable.head_icon)
                        .circleCrop()
                        .into(ivAvatar);
            }
        } else {
            // 用户信息为空，使用默认头像
            Glide.with(this)
                    .load(R.drawable.head_icon)
                    .circleCrop()
                    .into(ivAvatar);
        }

        if (share.getImageUrl() != null && !share.getImageUrl().isEmpty()) {
            ivImage.setVisibility(android.view.View.VISIBLE);
            Glide.with(this).load(share.getImageUrl()).into(ivImage);
        } else {
            ivImage.setVisibility(android.view.View.GONE);
        }
    }

    private void loadComments() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "100");

        apiService.getCommentsByShareId(shareId, params).enqueue(new Callback<ApiResponse<PageData<Comment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<Comment>>> call, Response<ApiResponse<PageData<Comment>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageData<Comment>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        commentList.clear();
                        commentList.addAll(apiResponse.getData().getContent());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<Comment>>> call, Throwable t) {
            }
        });
    }

    private void sendComment() {
        String content = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> request = new HashMap<>();
        request.put("shareId", shareId);
        request.put("content", content);

        apiService.createComment(request).enqueue(new Callback<ApiResponse<Comment>>() {
            @Override
            public void onResponse(Call<ApiResponse<Comment>> call, Response<ApiResponse<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Comment> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        etComment.setText("");
                        loadComments();
                        Toast.makeText(ShareDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Comment>> call, Throwable t) {
                Toast.makeText(ShareDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
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

