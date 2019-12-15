package com.example.theangkringan.ui.recipes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.example.theangkringan.models.WishlistModel;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity {


    private TextView tvRecipeTitle;
    private TextView tvCategoryRecipe;
    private TextView tvDetailRating;
    private TextView tvDetailDesc;
    private TextView tvDetailIngridient;
    private TextView tvDetailCook;
    private TextView tvVideoLink;
    private TextView tvDetailProvcity;
    private RatingBar rbDetailRating;
    private Button btnAddReview;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsToolbar;
    private ImageView imgCover;
    private FloatingActionButton favoriteBtn;

    private RecyclerView mRecyclerview;
    private UserCommentAdapter mAdapter;

    private TheAngkringanAPI appApi;
    static final String TAG = DetailRecipeActivity.class.getSimpleName();
    public static String RECIPE_ID = "recipe_id";
    public static String RECIPE_TYPE = "recipe_type";

    private ArrayList<ReviewModel> listReview = new ArrayList<>();
    private AppPreferences userPreference;
    private String recipe_id = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        mToolbar = findViewById(R.id.recipe_toolbar);
        collapsToolbar = findViewById(R.id.collapsing_toolbar_layout);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        imgCover = findViewById(R.id.img_detail_backdrop);
        tvRecipeTitle = findViewById(R.id.tv_recipe_title);
        tvCategoryRecipe = findViewById(R.id.tv_category_recipe);
        tvDetailRating = findViewById(R.id.tv_detail_rating);
        tvDetailDesc = findViewById(R.id.tv_detail_desc);
        tvDetailIngridient = findViewById(R.id.tv_detail_ingridient);
        tvDetailCook = findViewById(R.id.tv_detail_cook_step);
        tvVideoLink = findViewById(R.id.tv_video_link);
        tvDetailProvcity = findViewById(R.id.tv_detail_provcity);
        btnAddReview = findViewById(R.id.btn_add_review);
        rbDetailRating = findViewById(R.id.rb_detail_rating);
        mRecyclerview = findViewById(R.id.rv_list_review);
        favoriteBtn = findViewById(R.id.favorite_btn);
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
                if(!TextUtils.isEmpty(userPreference.getUserToken(DetailRecipeActivity.this))) {
                    ShowDialog();
                }else{
                    ShowInfoDialog();
                }
            }
        });
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(userPreference.getUserToken(DetailRecipeActivity.this))) {
                    if(getIntent().getExtras().getString(RECIPE_TYPE) != null){
                        if(getIntent().getExtras().getString(RECIPE_TYPE).equals("wishlist")) {
                            deleteWishlist(userPreference.getUserUnique(DetailRecipeActivity.this), recipe_id);
                        }
                    }else{
                        addWishlist(userPreference.getUserUnique(DetailRecipeActivity.this), recipe_id);
                    }
                }else{
                    ShowInfoDialog();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    public void ShowInfoDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Information");
        builder.setMessage("Lakukan Login terlebih dahulu untuk menambahkan favorit. Masuk ke halaman akun.");

        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        builder.create();
        builder.show();

    }


    private void setDetailData(String title, String category,
                               String rating, String desc,
                               String ingridient, String step,
                               String image_backdrop, String videoLink, String province, String city){
        mToolbar.setTitle(title);
        collapsToolbar.setTitle(title);
        Glide.with(this)
                .load(image_backdrop)
                .apply(new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo))
                .into(imgCover);
        tvVideoLink.setText(Html.fromHtml(
                "<a href=\""+videoLink+"\">Video lengkap "+title+"</a> "));
        tvVideoLink.setMovementMethod(LinkMovementMethod.getInstance());
        tvRecipeTitle.setText(title);
        tvCategoryRecipe.setText(category);
        tvDetailRating.setText(rating);
        tvDetailDesc.setText(desc);
        tvDetailIngridient.setText(Html.fromHtml(ingridient));
        tvDetailCook.setText(Html.fromHtml(step));
        tvDetailProvcity.setText(province + " - "+city);
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
                                setDetailData(data.getRecipe_name(), "Category : "+data.getRecipe_category(), "5",
                                        data.getDescription(), data.getInggridients(), data.getCookmethd(),
                                        data.getImage(), data.getVideo(), data.getProvince(), data.getCity());
                                retrieveRating(recipe_id);
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
                                tvDetailRating.setText(String.valueOf(response.body().getData().getTotalrating()));

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

    private void addWishlist(String userId, String recipe_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<WishlistModel>> call = appApi.addWishlistRecipe(userId, recipe_id);
            call.enqueue(new Callback<BaseResponse<WishlistModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<WishlistModel>> call, Response<BaseResponse<WishlistModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                Toast.makeText(DetailRecipeActivity.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<WishlistModel>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteWishlist(String userId, String recipe_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(DetailRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse> call = appApi.deleteWishlistRecipe(userId, recipe_id);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailRecipeActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
