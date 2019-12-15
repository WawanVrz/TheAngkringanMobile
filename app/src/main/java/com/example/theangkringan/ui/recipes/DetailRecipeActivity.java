package com.example.theangkringan.ui.recipes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.RecipeAdapter;
import com.example.theangkringan.adapters.UserCommentAdapter;
import com.example.theangkringan.interfaces.OnRecipeClickCallback;
import com.example.theangkringan.models.AddRecipeModel;
import com.example.theangkringan.models.AddReviewModel;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RatingModel;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.models.ReviewModel;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity {


    private TextView tvRecipeTitle;
    private TextView tvCategoryRecipe;
    private TextView tvDetailRating;
    private TextView tvDetailDesc;
    private TextView tvDetailIngridient;
    private TextView tvDetailCook;
    private RatingBar rbDetailRating;
    private Button btnAddReview;

    private RecyclerView mRecyclerview;
    private UserCommentAdapter mAdapter;

    private TheAngkringanAPI appApi;
    static final String TAG = DetailRecipeActivity.class.getSimpleName();
    public static String RECIPE_ID = "recipe_id";

    private ArrayList<ReviewModel> listReview = new ArrayList<>();
    private AppPreferences userPreference;
    private String recipe_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        tvRecipeTitle = findViewById(R.id.tv_recipe_title);
        tvCategoryRecipe = findViewById(R.id.tv_category_recipe);
        tvDetailRating = findViewById(R.id.tv_detail_rating);
        tvDetailDesc = findViewById(R.id.tv_detail_desc);
        tvDetailIngridient = findViewById(R.id.tv_detail_ingridient);
        tvDetailCook = findViewById(R.id.tv_detail_cook_step);
        btnAddReview = findViewById(R.id.btn_add_review);
        rbDetailRating = findViewById(R.id.rb_detail_rating);
        mRecyclerview = findViewById(R.id.rv_list_review);
        mRecyclerview.setHasFixedSize(true);
        recipe_id = getIntent().getExtras().getString(RECIPE_ID);
        retrieveAllRecipe(getIntent().getExtras().getString(RECIPE_ID));
        initRecyclerview(getIntent().getExtras().getString(RECIPE_ID));

        userPreference = new AppPreferences(DetailRecipeActivity.this);

        if(!TextUtils.isEmpty(userPreference.getUserToken(this))) {
            btnAddReview.setVisibility(View.VISIBLE);
        }else{
            btnAddReview.setVisibility(View.GONE);
        }

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }

    public void ShowDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle("Add Comment");
        View dialogLayout = inflater.inflate(R.layout.custom_add_review, null);
        final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        ratingBar.setMax(5);
        final EditText editText = dialogLayout.findViewById(R.id.input_text_review);
        builder.setView(dialogLayout);

        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ratingnya : ", ratingBar.getProgress()+"");
                        Log.d("ratingnya : ", editText.getText().toString());
                        addComment(userPreference.getUserUnique(DetailRecipeActivity.this),recipe_id, ratingBar.getProgress()+"", editText.getText().toString());
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.create();
        builder.show();

    }


    private void setDetailData(String title, String category, String rating, String desc, String ingridient, String step){
        tvRecipeTitle.setText(title);
        tvCategoryRecipe.setText(category);
        tvDetailRating.setText(rating);
        tvDetailDesc.setText(desc);
        tvDetailIngridient.setText(Html.fromHtml(ingridient));
        tvDetailCook.setText(Html.fromHtml(step));
    }

    private void initRecyclerview(String recipe_id){
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserCommentAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setData(listReview);
        retrieveAllComment(recipe_id);
    }

    // Call Api
    private void retrieveAllRecipe(final String recipe_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<RecipeModel>> call = appApi.getDetailRecipe(recipe_id);
            call.enqueue(new Callback<BaseResponse<RecipeModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<RecipeModel>> call, Response<BaseResponse<RecipeModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                RecipeModel data = response.body().getData();
                                setDetailData(data.getRecipe_name(), "category A", "5",data.getDescription(), data.getInggridients(), data.getCookmethd());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<RecipeModel>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieveAllComment(final String recipe_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<ReviewModel>>> call = appApi.getListReview(recipe_id);

            call.enqueue(new Callback<BaseResponse<ArrayList<ReviewModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<ReviewModel>>> call, Response<BaseResponse<ArrayList<ReviewModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                listReview.addAll(response.body().getData());
                                mAdapter.setData(listReview);
                                retrieveRating(recipe_id);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<ReviewModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addComment(String userId, final String recipe_id, String rating, String comment) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<AddReviewModel>> call = appApi.addReview(userId, recipe_id, rating, comment);

            call.enqueue(new Callback<BaseResponse<AddReviewModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<AddReviewModel>> call, Response<BaseResponse<AddReviewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                Toast.makeText(DetailRecipeActivity.this, "Comment Succesfull", Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<AddReviewModel>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieveRating(final String recipe_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<RatingModel>> call = appApi.getRatingByRecipeID(recipe_id);

            call.enqueue(new Callback<BaseResponse<RatingModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<RatingModel>> call, Response<BaseResponse<RatingModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                rbDetailRating.setRating(response.body().getData().getTotalrating());
                                tvDetailRating.setText(response.body().getData().getTotalrating()+"");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<RatingModel>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWishlist(String userId, final String recipe_id, String rating, String comment) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<AddReviewModel>> call = appApi.addReview(userId, recipe_id, rating, comment);

            call.enqueue(new Callback<BaseResponse<AddReviewModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<AddReviewModel>> call, Response<BaseResponse<AddReviewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                Toast.makeText(DetailRecipeActivity.this, "Comment Succesfull", Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<AddReviewModel>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
