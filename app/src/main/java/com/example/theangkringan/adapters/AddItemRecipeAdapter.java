package com.example.theangkringan.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.theangkringan.R;
import com.example.theangkringan.interfaces.OnCategoryRecipeClickCallback;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;

public class AddItemRecipeAdapter extends RecyclerView.Adapter<AddItemRecipeAdapter.AddViewHolder> {

    private ArrayList<String> listRecipe = new ArrayList<>();
    private Map<Integer, String> map = new ArrayMap<>();
    private Context mContext;
    private OnCategoryRecipeClickCallback onItemClickCallback;

    public AddItemRecipeAdapter(Context mContext){
        this.mContext = mContext;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_recipe, parent, false);
        return new AddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddViewHolder holder, final int position) {
        String text = map.get(holder.getAdapterPosition());
        holder.inputText.setText(text != null ? text : "");
        holder.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                map.put(holder.getAdapterPosition(), s.toString());
                listRecipe.set(holder.getAdapterPosition(), map.get(holder.getAdapterPosition()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRecipe.remove(holder.getAdapterPosition());
                rearrangeData();
                notifyDataSetChanged();
            }
        });

    }


    private void rearrangeData(){
        map.clear();
        for(int i = 0; i<listRecipe.size(); i++)
        {
            map.put(i, listRecipe.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return listRecipe.size();
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

    public void setData(String items) {
        listRecipe.add(items);
        notifyDataSetChanged();
    }
}
