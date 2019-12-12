package com.example.theangkringan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theangkringan.R;
import com.example.theangkringan.interfaces.OnCategoryRecipeClickCallback;
import com.example.theangkringan.interfaces.OnLatestRecipeClickCallback;
import com.example.theangkringan.models.CategoryRecipeModel;
import com.example.theangkringan.models.LatestRecipeModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryRecipeAdapter extends RecyclerView.Adapter<CategoryRecipeAdapter.CategoryViewHolder> {

    private ArrayList<CategoryRecipeModel> listRecipe = new ArrayList<>();
    private OnCategoryRecipeClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnCategoryRecipeClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_category_recipe, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        holder.bind(listRecipe.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listRecipe.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRecipe.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCover;
        TextView tvTitle;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_category_rec);
            tvTitle = itemView.findViewById(R.id.tv_category_rec);
        }

        public void bind(CategoryRecipeModel data){
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo))
                    .into(imgCover);
            tvTitle.setText(data.getCategory_name());
        }
    }

    public void setCategoryData(ArrayList<CategoryRecipeModel> items) {
        listRecipe.clear();
        listRecipe.addAll(items);
        notifyDataSetChanged();
    }
}
