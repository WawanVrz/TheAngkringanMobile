package com.example.theangkringan.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.RecipeAdapter;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.example.theangkringan.ui.recipes.DetailRecipeActivity;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private TheAngkringanAPI appApi;
    static final String TAG = WishlistFragment.class.getSimpleName();
    private AppPreferences userPreference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPreference = new AppPreferences(getActivity());
        mRecyclerview = view.findViewById(R.id.rv_wishlist);
        mRecyclerview.setHasFixedSize(true);
        initRecyclerview();
    }

    private void initRecyclerview(){
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdapter = new RecipeAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {
                Intent intent = new Intent(getActivity(), DetailRecipeActivity.class);
                intent.putExtra(DetailRecipeActivity.RECIPE_ID, String.valueOf(data.getId()));
                intent.putExtra(DetailRecipeActivity.RECIPE_TYPE, "wishlist");
                startActivity(intent);
            }
        });
        mAdapter.setRecipeData(listRecipe);
        retrieveWishlistRecipe(userPreference.getUserUnique(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveWishlistRecipe(userPreference.getUserUnique(getActivity()));
    }

    // Call Api
    private void retrieveWishlistRecipe(String userid) {
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getWishlist(userid);
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            listRecipe.clear();
                            if (response.body().getData() != null){
                                if(response.body().getData().size() > 0) {
                                    listRecipe.addAll(response.body().getData());
                                    mAdapter.setRecipeData(listRecipe);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }else{
                                mAdapter.setRecipeData(listRecipe);
                                mAdapter.notifyDataSetChanged();
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
