package com.example.theangkringan.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.RecipeAdapter;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class DetailRecipeListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private TheAngkringanAPI appApi;
    static final String TAG = DetailRecipeListActivity.class.getSimpleName();
    public static final String TAG_TYPE_RECIPE = "tag_type_recipe";
    public static final String TAG_DATA_ID = "tag_data_id";
    public static final String TAG_DATA_TITLE = "tag_data_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("My title");
        }
        mRecyclerview = findViewById(R.id.rv_list_recipe);
        mRecyclerview.setHasFixedSize(true);
        initRecyclerview();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initRecyclerview(){
        mRecyclerview.setLayoutManager(new GridLayoutManager(DetailRecipeListActivity.this, 2));
        mAdapter = new RecipeAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {

            }
        });
        mAdapter.setRecipeData(listRecipe);
        if(getIntent().getExtras().getString(TAG_TYPE_RECIPE).equals("Location")){
            if (getSupportActionBar() != null) { getSupportActionBar().setTitle("Location - "+getIntent().getExtras().getString(TAG_DATA_TITLE));}
            retrieveLocRecipe(getIntent().getExtras().getString(TAG_DATA_ID));
        }else if(getIntent().getExtras().getString(TAG_TYPE_RECIPE).equals("Category")){
            if (getSupportActionBar() != null) { getSupportActionBar().setTitle("Category - "+getIntent().getExtras().getString(TAG_DATA_TITLE));}
            retrieveCatRecipe(getIntent().getExtras().getString(TAG_DATA_ID));
        }
    }

    // Call Api
    // retrieve recipe by category id
    private void retrieveCatRecipe(String id) {
        try {
            appApi = TheAngkringanServices.getRetrofit().create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getRecipeByCategory(id);
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

    // retrieve recipe by location id
    private void retrieveLocRecipe(String id) {
        try {
            appApi = TheAngkringanServices.getRetrofit().create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getRecipeByLocation(id);
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
