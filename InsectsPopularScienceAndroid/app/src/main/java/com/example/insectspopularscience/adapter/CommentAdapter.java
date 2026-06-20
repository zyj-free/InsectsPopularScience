package com.example.insectspopularscience.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.model.Comment;
import com.example.insectspopularscience.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.tvContent.setText(comment.getContent() != null ? comment.getContent() : "");
        holder.tvTime.setText(DateUtil.formatDate(comment.getCreatedAt()));

        if (comment.getUser() != null) {
            holder.tvUsername.setText(comment.getUser().getNickname() != null ? 
                    comment.getUser().getNickname() : comment.getUser().getUsername());
            
            // 加载头像，如果有数据库头像就使用，否则使用默认头像head_icon
            String avatarUrl = comment.getUser().getAvatar();
            if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
                // 判断是本地路径还是网络URL
                if (avatarUrl.startsWith("http://") || avatarUrl.startsWith("https://")) {
                    // 网络图片
                    Glide.with(holder.itemView.getContext())
                            .load(avatarUrl)
                            .placeholder(R.drawable.head_icon)
                            .error(R.drawable.head_icon)
                            .circleCrop()
                            .into(holder.ivAvatar);
                } else {
                    // 本地路径
                    try {
                        android.net.Uri avatarUri = android.net.Uri.parse(avatarUrl.startsWith("file://") 
                                ? avatarUrl 
                                : "file://" + avatarUrl);
                        Glide.with(holder.itemView.getContext())
                                .load(avatarUri)
                                .placeholder(R.drawable.head_icon)
                                .error(R.drawable.head_icon)
                                .circleCrop()
                                .into(holder.ivAvatar);
                    } catch (Exception e) {
                        // 解析失败，使用默认头像
                        Glide.with(holder.itemView.getContext())
                                .load(R.drawable.head_icon)
                                .circleCrop()
                                .into(holder.ivAvatar);
                    }
                }
            } else {
                // 没有头像，使用默认头像head_icon
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.head_icon)
                        .circleCrop()
                        .into(holder.ivAvatar);
            }
        } else {
            // 用户信息为空，使用默认头像
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.head_icon)
                    .circleCrop()
                    .into(holder.ivAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivAvatar;
        TextView tvUsername;
        TextView tvTime;
        TextView tvContent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}

