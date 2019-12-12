package com.example.theangkringan.ui.recipes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.RecipeAdapter;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class RecipesFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private TheAngkringanAPI appApi;
    static final String TAG = RecipesFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerview = view.findViewById(R.id.rv_latest_recipe);
        mRecyclerview.setHasFixedSize(true);
        initRecyclerview();
    }

    private void initRecyclerview(){
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new RecipeAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {

            }
        });
        mAdapter.setRecipeData(listRecipe);
        retrieveAllRecipe();
    }

    // Call Api
    private void retrieveAllRecipe() {
        try {
            appApi = TheAngkringanServices.getRetrofit().create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getAllRecipe();
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listRecipe.addAll(response.body().getData());
                                mAdapter.setRecipeData(listRecipe);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ArrayList<RecipeModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}