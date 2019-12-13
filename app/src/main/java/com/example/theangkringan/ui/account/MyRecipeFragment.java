package com.example.theangkringan.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class MyRecipeFragment extends Fragment {

    private RecyclerView mRecyclerview;

    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> listRecipeApprove = new ArrayList<>();
    private TheAngkringanAPI appApi;
    static final String TAG = MyRecipeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerview = view.findViewById(R.id.rv_approved_recipe);
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
        mAdapter.setRecipeData(listRecipeApprove);
        retrieveWishlistRecipe();
    }

    // Call Api
    private void retrieveWishlistRecipe() {
        try {
            appApi = TheAngkringanServices.getRetrofit().create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getWishlist("YcLvMVWIPlglI1wTXUd21576165193", "5");
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listRecipeApprove.addAll(response.body().getData());
                                mAdapter.setRecipeData(listRecipeApprove);
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
