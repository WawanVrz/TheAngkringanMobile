package com.example.theangkringan.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.Button;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.UserRecipeAdapter;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.example.theangkringan.ui.recipes.AddRecipeActivity;
import com.example.theangkringan.ui.recipes.DetailRecipeActivity;
import com.example.theangkringan.ui.recipes.DetailRecipeListActivity;

import java.util.ArrayList;

public class MyRecipeFragment extends Fragment {

    private RecyclerView mRecApprove;
    private RecyclerView mRecWaiting;
    private RecyclerView mRecRejected;

    private Button AddRecipe;

    private UserRecipeAdapter mAdapterApprove;
    private UserRecipeAdapter mAdapterWaiting;
    private UserRecipeAdapter mAdapterRejected;
    private ArrayList<RecipeModel> listRecipeApprove = new ArrayList<>();
    private ArrayList<RecipeModel> listRecipeWaiting = new ArrayList<>();
    private ArrayList<RecipeModel> listRecipeRejected = new ArrayList<>();

    private TheAngkringanAPI appApi;
    static final String TAG = MyRecipeFragment.class.getSimpleName();

    private AppPreferences userPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPreference = new AppPreferences(getActivity());

        mRecApprove = view.findViewById(R.id.rv_approved_recipe);
        mRecWaiting = view.findViewById(R.id.rv_inprogress_recipe);
        mRecRejected = view.findViewById(R.id.rv_revise_recipe);
        mRecApprove.setHasFixedSize(true);
        mRecWaiting.setHasFixedSize(true);
        mRecRejected.setHasFixedSize(true);

        AddRecipe = view.findViewById(R.id.add_recipe_btn);
        AddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });
        initRecApproval();
        initRecWaiting();
        initRecRejected();
    }

    private void initRecApproval(){
        mRecApprove.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapterApprove = new UserRecipeAdapter();
        mAdapterApprove.notifyDataSetChanged();
        mRecApprove.setAdapter(mAdapterApprove);

        mAdapterApprove.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {
                Intent intent = new Intent(getActivity(), DetailRecipeActivity.class);
                intent.putExtra(DetailRecipeActivity.RECIPE_ID, String.valueOf(data.getId()));
                startActivity(intent);
            }
        });
        mAdapterApprove.setRecipeData(listRecipeApprove);
        approvalRecipe(userPreference.getUserUnique(getActivity()));
    }

    private void initRecWaiting(){
        mRecWaiting.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapterWaiting = new UserRecipeAdapter();
        mAdapterWaiting.notifyDataSetChanged();
        mRecWaiting.setAdapter(mAdapterWaiting);

        mAdapterWaiting.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {

            }
        });
        mAdapterWaiting.setRecipeData(listRecipeApprove);
        inProgressRecipe(userPreference.getUserUnique(getActivity()));
    }

    private void initRecRejected(){
        mRecRejected.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapterRejected = new UserRecipeAdapter();
        mAdapterRejected.notifyDataSetChanged();
        mRecRejected.setAdapter(mAdapterRejected);

        mAdapterRejected.setOnItemClickCallback(new OnRecipeClickCallback() {
            @Override
            public void onItemClicked(RecipeModel data) {

            }
        });
        mAdapterRejected.setRecipeData(listRecipeApprove);
        RejectedRecipe(userPreference.getUserUnique(getActivity()));
    }

    // Call Api
    private void approvalRecipe(String user_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getRecipeApproval(user_id);
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listRecipeApprove.addAll(response.body().getData());
                                mAdapterApprove.setRecipeData(listRecipeApprove);
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

    private void inProgressRecipe(String user_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getRecipeInProgress(user_id);
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listRecipeWaiting.addAll(response.body().getData());
                                mAdapterWaiting.setRecipeData(listRecipeWaiting);
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

    private void RejectedRecipe(String user_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<RecipeModel>>> call = appApi.getRecipeRejected(user_id);
            call.enqueue(new Callback<BaseResponse<ArrayList<RecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<RecipeModel>>> call, Response<BaseResponse<ArrayList<RecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listRecipeRejected.addAll(response.body().getData());
                                mAdapterRejected.setRecipeData(listRecipeRejected);
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
