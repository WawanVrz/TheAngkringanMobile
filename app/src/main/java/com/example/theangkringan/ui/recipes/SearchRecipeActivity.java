package com.example.theangkringan.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theangkringan.R;
import com.example.theangkringan.adapters.RecipeAdapter;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class SearchRecipeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private RecipeAdapter mAdapter;

    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private TheAngkringanAPI appApi;
    static final String TAG = SearchRecipeActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private LinearLayout layoutError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        progressBar = findViewById(R.id.search_loading);
        layoutError = findViewById(R.id.layout_error);
        mRecyclerview = findViewById(R.id.rv_search_recipe);
        mRecyclerview.setHasFixedSize(true);
        initRecyclerview();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            final SearchView searchView = (SearchView) (menu.findItem(R.id.act_search)).getActionView();
            searchView.setFocusable(true);
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String data) {
                    searchView.clearFocus();
                    retrieveRecipe(data);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String data) {
                    return false;
                }
            });
        }
        return true;
    }

    private void initRecyclerview(){
        mRecyclerview.setLayoutManager(new GridLayoutManager(SearchRecipeActivity.this, 2));
        mAdapter = new RecipeAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {
                Intent intent = new Intent(SearchRecipeActivity.this, DetailRecipeActivity.class);
                intent.putExtra(DetailRecipeActivity.RECIPE_ID, String.valueOf(data.getId()));
                startActivity(intent);
            }
        });
        mAdapter.setRecipeData(listRecipe);
    }


    private void retrieveRecipe(String param) {
        try {
            showLoading(true);
            appApi = TheAngkringanServices.getRetrofit(SearchRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.searchRecipe(param);
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null){
                                    if(response.body().getData().size() > 0) {
                                        layoutError.setVisibility(View.GONE);
                                        mRecyclerview.setVisibility(View.VISIBLE);
                                        listRecipe.clear();
                                        listRecipe.addAll(response.body().getData());
                                        mAdapter.setRecipeData(listRecipe);
                                        showLoading(false);
                                    }
                            }else{
                                showLoading(false);
                                dataNotFound();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLoading(false);
                        dataNotFound();
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ArrayList<RecipeModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                    showLoading(false);
                    dataNotFound();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showLoading(false);
            dataNotFound();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void dataNotFound(){
        mRecyclerview.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
    }
}
