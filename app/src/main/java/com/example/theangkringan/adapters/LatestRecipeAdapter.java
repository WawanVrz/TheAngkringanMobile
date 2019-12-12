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
import com.example.theangkringan.models.LatestRecipeModel;
import com.example.theangkringan.models.RecipeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LatestRecipeAdapter extends RecyclerView.Adapter<LatestRecipeAdapter.LatestRecViewHolder> {

    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private OnLatestRecipeClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnLatestRecipeClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public LatestRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_latest_recipe, parent, false);
        return new LatestRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LatestRecViewHolder holder, int position) {
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

    public class LatestRecViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCover;
        TextView tvTitle;
        public LatestRecViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_latest_rec);
            tvTitle = itemView.findViewById(R.id.tv_latest_rec);
        }

        public void bind(RecipeModel data){
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo))
                    .into(imgCover);
            tvTitle.setText(data.getRecipe_name());
        }
    }

    public void setRecipeData(ArrayList<RecipeModel> items) {
        listRecipe.clear();
        listRecipe.addAll(items);
        notifyDataSetChanged();
    }
}
