package com.example.theangkringan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.theangkringan.R;
import com.example.theangkringan.interfaces.OnLatestRecipeClickCallback;
import com.example.theangkringan.interfaces.OnLocationClickCallback;
import com.example.theangkringan.models.LatestRecipeModel;
import com.example.theangkringan.models.LocationModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private ArrayList<LocationModel> listLocation = new ArrayList<>();
    private OnLocationClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnLocationClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationViewHolder holder, int position) {
        holder.bind(listLocation.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listLocation.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listLocation.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCover;
        TextView tvTitle;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_location);
            tvTitle = itemView.findViewById(R.id.tv_location);
        }

        public void bind(LocationModel data){
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .into(imgCover);
            tvTitle.setText(data.getName());
        }
    }

    public void setLocationData(ArrayList<LocationModel> items) {
        listLocation.clear();
        listLocation.addAll(items);
        notifyDataSetChanged();
    }
}
