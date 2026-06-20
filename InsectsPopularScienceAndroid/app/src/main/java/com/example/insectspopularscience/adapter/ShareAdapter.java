package com.example.insectspopularscience.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.model.Share;
import com.example.insectspopularscience.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private List<Share> shareList;
    private OnItemClickListener listener;
    private OnEditClickListener editListener;
    private OnDeleteClickListener deleteListener;
    private boolean showActionButtons = false;

    public interface OnItemClickListener {
        void onItemClick(Share share);
    }

    public interface OnEditClickListener {
        void onEditClick(Share share);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Share share);
    }

    public ShareAdapter(List<Share> shareList, OnItemClickListener listener) {
        this.shareList = shareList;
        this.listener = listener;
    }

    public void setShowActionButtons(boolean show) {
        this.showActionButtons = show;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_share, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Share share = shareList.get(position);
        
        holder.tvTitle.setText(share.getTitle());
        holder.tvDescription.setText(share.getDescription());
        holder.tvLikeCount.setText(String.valueOf(share.getLikeCount() != null ? share.getLikeCount() : 0));
        holder.tvCommentCount.setText(String.valueOf(share.getCommentCount() != null ? share.getCommentCount() : 0));
        
        if (share.getUser() != null) {
            holder.tvUsername.setText(share.getUser().getNickname() != null ? 
                    share.getUser().getNickname() : share.getUser().getUsername());
            
            // 加载头像，如果有数据库头像就使用，否则使用默认头像head_icon
            String avatarUrl = share.getUser().getAvatar();
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
        
        if (share.getImageUrl() != null && !share.getImageUrl().isEmpty()) {
            holder.cardImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(share.getImageUrl())
                    .placeholder(R.drawable.head_icon)
                    .error(R.drawable.head_icon)
                    .centerCrop()
                    .into(holder.ivImage);
        } else {
            holder.cardImage.setVisibility(View.GONE);
        }
        
        holder.tvTime.setText(DateUtil.formatDate(share.getCreatedAt()));

        // 显示/隐藏编辑和删除按钮
        if (showActionButtons) {
            holder.llActionButtons.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onEditClick(share);
                }
            });
            holder.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(share);
                }
            });
        } else {
            holder.llActionButtons.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(share);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivAvatar;
        TextView tvUsername;
        TextView tvTime;
        TextView tvTitle;
        ImageView ivImage;
        com.google.android.material.card.MaterialCardView cardImage;
        TextView tvDescription;
        TextView tvLikeCount;
        TextView tvCommentCount;
        LinearLayout llActionButtons;
        com.google.android.material.button.MaterialButton btnEdit;
        com.google.android.material.button.MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImage = itemView.findViewById(R.id.iv_image);
            cardImage = itemView.findViewById(R.id.card_image);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            llActionButtons = itemView.findViewById(R.id.ll_action_buttons);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}

