package com.example.theangkringan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.CategoryRecipeAdapter;
import com.example.theangkringan.adapters.LatestRecipeAdapter;
import com.example.theangkringan.adapters.LocationAdapter;
import com.example.theangkringan.adapters.SliderAdapter;
import com.example.theangkringan.interfaces.OnBannerClickCallback;
import com.example.theangkringan.interfaces.OnCategoryRecipeClickCallback;
import com.example.theangkringan.interfaces.OnLatestRecipeClickCallback;
import com.example.theangkringan.interfaces.OnLocationClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.CategoryRecipeModel;
import com.example.theangkringan.models.EventModel;
import com.example.theangkringan.models.LocationModel;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.example.theangkringan.ui.event.DetailEventActivity;
import com.example.theangkringan.ui.recipes.DetailRecipeActivity;
import com.example.theangkringan.ui.recipes.DetailRecipeListActivity;
import com.example.theangkringan.ui.recipes.SearchRecipeActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private RecyclerView mRecyclerviewCat;
    private RecyclerView mRecyclerviewLoc;
    private ViewPager mViewPager;
    private ProgressBar progressBar;

    private LatestRecipeAdapter mAdapter;
    private CategoryRecipeAdapter mCatAdapter;
    private LocationAdapter mAdapterLoc;
    private SliderAdapter slider;
    private LinearLayout searchLayout;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    private TheAngkringanAPI appApi;
    private ArrayList<RecipeModel> listRecipe = new ArrayList<>();
    private ArrayList<EventModel> promosList = new ArrayList<>();
    private ArrayList<CategoryRecipeModel> categoryList = new ArrayList<>();
    private ArrayList<LocationModel> locationList = new ArrayList<>();

    static final String TAG = HomeFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.event_loading);
        searchLayout = view.findViewById(R.id.search_layout);
        mRecyclerview = view.findViewById(R.id.rv_latest_recipe);
        mRecyclerviewCat = view.findViewById(R.id.rv_category_recipe);
        mRecyclerviewLoc = view.findViewById(R.id.rv_location);
        mViewPager = view.findViewById(R.id.vp_promo_banner);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerviewCat.setHasFixedSize(true);
        mRecyclerviewLoc.setHasFixedSize(true);
        retrieveLatestRecipe();
        retrieveCatRecipe();
        retrieveLocation();
        retrievePromo();

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchRecipeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBanner(){
        slider = new SliderAdapter(getActivity());
        slider.setBannerList(promosList);
        mViewPager.setAdapter(slider);

        slider.setOnItemClickCallback(new OnBannerClickCallback() {
            @Override
            public void onItemClicked(EventModel data) {
                Intent intent = new Intent(getActivity(), DetailEventActivity.class);
                intent.putExtra(DetailEventActivity.DATA_TITLE, data.getTitle());
                intent.putExtra(DetailEventActivity.DATA_DESC, data.getSubtitle());
                intent.putExtra(DetailEventActivity.DATA_IMG, data.getImage());
                startActivity(intent);
            }
        });

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == promosList.size()) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void initRecyclerview(){
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new LatestRecipeAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickCallback(new OnLatestRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {
                Intent intent = new Intent(getActivity(), DetailRecipeActivity.class);
                intent.putExtra(DetailRecipeActivity.RECIPE_ID, String.valueOf(data.getId()));
                startActivity(intent);
            }
        });
        mAdapter.setRecipeData(listRecipe);
    }

    private void initRecyclerviewCat(){
        mRecyclerviewCat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mCatAdapter = new CategoryRecipeAdapter();
        mCatAdapter.notifyDataSetChanged();
        mRecyclerviewCat.setAdapter(mCatAdapter);

        mCatAdapter.setOnItemClickCallback(new OnCategoryRecipeClickCallback() {
            @Override
            public void onItemClicked(CategoryRecipeModel data) {
                Intent intent = new Intent(getActivity(), DetailRecipeListActivity.class);
                intent.putExtra(DetailRecipeListActivity.TAG_DATA_ID, String.valueOf(data.getId()));
                intent.putExtra(DetailRecipeListActivity.TAG_TYPE_RECIPE,"Category");
                intent.putExtra(DetailRecipeListActivity.TAG_DATA_TITLE, data.getCategory_name());
                startActivity(intent);
            }
        });
        mCatAdapter.setCategoryData(categoryList);
    }

    private void initRecyclerviewLoc(){
        mRecyclerviewLoc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapterLoc = new LocationAdapter();
        mAdapterLoc.notifyDataSetChanged();
        mRecyclerviewLoc.setAdapter(mAdapterLoc);

        mAdapterLoc.setOnItemClickCallback(new OnLocationClickCallback() {
            @Override
            public void onItemClicked(LocationModel data) {
                Intent intent = new Intent(getActivity(), DetailRecipeListActivity.class);
                intent.putExtra(DetailRecipeListActivity.TAG_DATA_ID, String.valueOf(data.getId()));
                intent.putExtra(DetailRecipeListActivity.TAG_TYPE_RECIPE,"Location");
                intent.putExtra(DetailRecipeListActivity.TAG_DATA_TITLE, data.getName());
                startActivity(intent);
            }
        });
        mAdapterLoc.setLocationData(locationList);
    }
    // ======================================
    // ============== call API ==============
    // ======================================
    private void retrievePromo(){
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<EventModel>>> call = appApi.getAllPromos();
            call.enqueue(new Callback<BaseResponse<ArrayList<EventModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<EventModel>>> call, Response<BaseResponse<ArrayList<EventModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                promosList.addAll(response.body().getData());
                                initBanner();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ArrayList<EventModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void retrieveCatRecipe(){
        try {
            showLoading(true);
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<CategoryRecipeModel>>> call = appApi.getAllCategories();
            call.enqueue(new Callback<BaseResponse<ArrayList<CategoryRecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<CategoryRecipeModel>>> call, Response<BaseResponse<ArrayList<CategoryRecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                categoryList.addAll(response.body().getData());
                                initRecyclerviewCat();
                                showLoading(false);
                            }
                        }
                    } catch (Exception e) {
                        showLoading(false);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<CategoryRecipeModel>>> call, Throwable t) {
                    showLoading(false);
                    Log.d(TAG, "Error");
                }
            });
        }catch (Exception e){
            showLoading(false);
            e.printStackTrace();
        }
    }

    private void retrieveLocation(){
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<LocationModel>>> call = appApi.getAllProvinces();
            call.enqueue(new Callback<BaseResponse<ArrayList<LocationModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<LocationModel>>> call, Response<BaseResponse<ArrayList<LocationModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                locationList.addAll(response.body().getData());
                                initRecyclerviewLoc();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<LocationModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void retrieveLatestRecipe() {
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getNewRecipe();
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listRecipe.addAll(response.body().getData());
                                initRecyclerview();
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

    // ============== call API ==============

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}