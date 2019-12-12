package com.example.theangkringan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theangkringan.R;
import com.example.theangkringan.interfaces.OnLatestRecipeClickCallback;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.LatestRecipeModel;
import com.example.theangkringan.models.RecipeModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private OnRecipeClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnRecipeClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_recipes, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {
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

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCover;
        TextView tvTitle;
        TextView tvDesc;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_recipe);
            tvTitle = itemView.findViewById(R.id.tv_recipe_title);
            tvDesc = itemView.findViewById(R.id.tv_recipe_desc);
        }

        public void bind(RecipeModel data){
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo))
                    .into(imgCover);
            tvTitle.setText(data.getRecipe_name());
            tvDesc.setText(data.getDescription());
        }
    }

    public void setRecipeData(ArrayList<RecipeModel> items) {
        listRecipe.clear();
        listRecipe.addAll(items);
        notifyDataSetChanged();
    }
}
