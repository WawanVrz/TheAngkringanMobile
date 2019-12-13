package com.example.theangkringan.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.example.theangkringan.R;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity {


    private TextView tvRecipeTitle;
    private TextView tvCategoryRecipe;
    private TextView tvDetailRating;
    private TextView tvDetailDesc;
    private TextView tvDetailIngridient;
    private TextView tvDetailCook;

    private TheAngkringanAPI appApi;
    static final String TAG = DetailRecipeActivity.class.getSimpleName();
    public static String RECIPE_ID = "recipe_id";

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
        retrieveAllRecipe(getIntent().getExtras().getString(RECIPE_ID));
    }


    private void setDetailData(String title, String category, String rating, String desc, String ingridient, String step){
        tvRecipeTitle.setText(title);
        tvCategoryRecipe.setText(category);
        tvDetailRating.setText(rating);
        tvDetailDesc.setText(desc);
        tvDetailIngridient.setText(Html.fromHtml(ingridient));
        tvDetailCook.setText(Html.fromHtml(step));
    }

    // Call Api
    private void retrieveAllRecipe(final String recipe_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit().create(TheAngkringanAPI.class);
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
}
