package com.example.insectspopularscience.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insectspopularscience.R;
import com.example.insectspopularscience.model.Insect;
import com.example.insectspopularscience.util.ImageUtil;

import java.util.List;

public class InsectAdapter extends RecyclerView.Adapter<InsectAdapter.ViewHolder> {
    private List<Insect> insectList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Insect insect);
    }

    public InsectAdapter(List<Insect> insectList, OnItemClickListener listener) {
        this.insectList = insectList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_insect, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Insect insect = insectList.get(position);
        
        // 确保TextView可见并且文字颜色正确
        holder.tvName.setVisibility(View.VISIBLE);
        holder.tvScientificName.setVisibility(View.VISIBLE);
        holder.tvDescription.setVisibility(View.VISIBLE);
        
        // 设置文字，确保不为null
        String name = insect.getName() != null ? insect.getName() : "未知昆虫";
        String scientificName = insect.getScientificName() != null ? insect.getScientificName() : "";
        String description = insect.getDescription() != null ? insect.getDescription() : "";
        
        holder.tvName.setText(name);
        holder.tvName.setTextColor(0xFF1A1A1A); // 确保文字颜色是黑色
        
        holder.tvScientificName.setText(scientificName);
        holder.tvScientificName.setTextColor(0xFF666666);
        
        holder.tvDescription.setText(description);
        holder.tvDescription.setTextColor(0xFF666666);

        // 加载图片
        String imageUrls = insect.getImageUrls();
        String imageUrl = null;
        if (imageUrls != null && !imageUrls.trim().isEmpty()) {
            imageUrl = ImageUtil.getFirstImageUrl(imageUrls);
        }
        
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // 加载网络图片
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.ivImage);
        } else {
            // 如果没有图片URL，显示占位图
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.ivImage);
        }
        
        // 确保ImageView可见
        holder.ivImage.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(insect);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insectList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvScientificName;
        TextView tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvScientificName = itemView.findViewById(R.id.tv_scientific_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}

