package com.example.theangkringan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.theangkringan.R;
import com.example.theangkringan.interfaces.OnCategoryRecipeClickCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddItemRecipeAdapter extends RecyclerView.Adapter<AddItemRecipeAdapter.AddViewHolder> {

    private ArrayList<String> listRecipe = new ArrayList<>();
    private OnCategoryRecipeClickCallback onItemClickCallback;

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_recipe, parent, false);
        return new AddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {

        EditText inputText;
        Button btnDel;
        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            inputText = itemView.findViewById(R.id.input_data_row);
            btnDel = itemView.findViewById(R.id.del_btn);
        }
    }
}
