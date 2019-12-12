package com.example.theangkringan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.theangkringan.R;
import com.example.theangkringan.interfaces.OnEventClickCallback;
import com.example.theangkringan.models.EventModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<EventModel> listEvent = new ArrayList<>();
    private OnEventClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnEventClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_event_promo, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, int position) {
        holder.bind(listEvent.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listEvent.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCover;
        TextView tvTitle;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_event_promo);
            tvTitle = itemView.findViewById(R.id.tv_event_promo);
        }

        public void bind(EventModel data){
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .into(imgCover);
            tvTitle.setText(data.getTitle());
        }
    }

    public void setEventData(ArrayList<EventModel> items) {
        listEvent.clear();
        listEvent.addAll(items);
        notifyDataSetChanged();
    }
}
