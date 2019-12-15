package com.example.theangkringan.services;

import com.example.theangkringan.models.AddRecipeModel;
import com.example.theangkringan.models.AddReviewModel;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.CategoryRecipeModel;
import com.example.theangkringan.models.CityModel;
import com.example.theangkringan.models.DetailUser;
import com.example.theangkringan.models.EventModel;
import com.example.theangkringan.models.LocationModel;
import com.example.theangkringan.models.LoginModel;
import com.example.theangkringan.models.RatingModel;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.models.ReviewModel;
import com.example.theangkringan.models.WishlistModel;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheAngkringanAPI {

    @GET("sliders") //done
    Call<BaseResponse<ArrayList<EventModel>>> getAllPromos();

    @GET("categories") //done
    Call<BaseResponse<ArrayList<CategoryRecipeModel>>> getAllCategories();

    @GET("provinces") //done
    Call<BaseResponse<ArrayList<LocationModel>>> getAllProvinces();

    @GET("cities") //done
    Call<BaseResponse<ArrayList<CityModel>>> getCityByProvince(
            @Query("province_id") String province_id
    );

    @GET("recipes") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getAllRecipe();

    @GET("recipes/lastest") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getNewRecipe();

    @GET("recipes/get_wishlist") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getWishlist(
//            @Header("Authorization") String authorization,
            @Query("user_id") String user_id
    );

    @GET("recipes/search") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> searchRecipe(
            @Query("string") String string_name
    );

    @GET("recipes/Detail")
    Call<BaseResponse<RecipeModel>> getDetailRecipe(
            @Query("recipe_id") String recipe_id
    );

    @GET("recipes/member")
    Call<BaseResponse<ArrayList<RecipeModel>>> getMemberRecipe(
            @Query("user_id") String user_id
    );

    @GET("account/get_user")
    Call<BaseResponse<ArrayList<DetailUser>>> getDetailUser(
            @Query("user_id") String user_id
    );

    @GET("recipes/get_review")
    Call<BaseResponse<ArrayList<ReviewModel>>> getListReview(
            @Query("recipe_id") String recipe_id
    );

    @GET("recipes/get_rating")
    Call<BaseResponse<RatingModel>> getRatingByRecipeID(
            @Query("recipe_id") String recipe_id
    );

    @GET("recipes/approval")
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeApproval(
            @Query("user_id") String user_id
    );

    @GET("recipes/waiting")
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeInProgress(
            @Query("user_id") String user_id
    );

    @GET("recipes/rejected")
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeRejected(
            @Query("user_id") String user_id
    );

    @GET("recipes/location") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeByLocation(
            @Query("province") String province
    );

    @GET("recipes/category") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeByCategory(
            @Query("recipe_category") String recipe_category
    );
    @FormUrlEncoded
    @POST("recipes/add_wishlist")
    Call<BaseResponse<WishlistModel>> addWishlistRecipe (
            @Field("user_id") String user_id,
            @Field("recipe_id") String recipe_id
    );
    @FormUrlEncoded
    @POST("recipes/delete_wishlist")
    Call<BaseResponse> deleteWishlistRecipe (
            @Field("user_id") String user_id,
            @Field("recipe_id") String recipe_id
    );

    @FormUrlEncoded //done
    @POST("account/login")
    Call<BaseResponse<LoginModel>> doLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("recipes/add_review")
    Call<BaseResponse<AddReviewModel>> addReview(
            @Field("user_id") String user_id,
            @Field("recipe_id") String recipe_id,
            @Field("rating") String rating,
            @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("account/register") //done
    Call<BaseResponse<LoginModel>> doRegiterMember(
            @Field("fullname") String fullname,
            @Field("address") String address,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("gender") String gender,
            @Field("password") String password
    );

    @Multipart
    @POST("recipes/add") //done
    Call<BaseResponse<AddRecipeModel>> addRecipe(
            @Query("user_id") int user_id,
            @Query("recipe_category") int recipe_category,
            @Query("recipe_name") String recipe_name,
            @Query("description") String description,
            @Part MultipartBody.Part image,
            @Query("video") String video,
            @Query("ingredients") String ingredients,
            @Query("cookmethd") String cookmethd,
            @Query("notes") String notes,
            @Query("province") int province,
            @Query("city") int city,
            @Query("status") int status
    );



    @POST("account/logout") //done
    Call<BaseResponse> doLogout(
            @Query("user_id") int user_id
    );
}
