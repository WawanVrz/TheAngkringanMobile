package com.example.theangkringan.services;

import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.CategoryRecipeModel;
import com.example.theangkringan.models.EventModel;
import com.example.theangkringan.models.LocationModel;
import com.example.theangkringan.models.LoginModel;
import com.example.theangkringan.models.RecipeModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TheAngkringanAPI {

    @GET("sliders") //done
    Call<BaseResponse<ArrayList<EventModel>>> getAllPromos();

    @GET("categories") //done
    Call<BaseResponse<ArrayList<CategoryRecipeModel>>> getAllCategories();

    @GET("provinces") //done
    Call<BaseResponse<ArrayList<LocationModel>>> getAllProvinces();

    @GET("recipes") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getAllRecipe();

    @GET("lastest") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getNewRecipe();

    @GET("recipes/get_wishlist") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getWishlist(
            @Header("Authorization") String authorization,
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

    @GET("recipes/location") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeByLocation(
            @Query("province") String province
    );

    @GET("recipes/category") //done
    Call<BaseResponse<ArrayList<RecipeModel>>> getRecipeByCategory(
            @Query("recipe_category") String recipe_category
    );

    @GET("recipes/add_wishlist")
    Call<BaseResponse<ArrayList<RecipeModel>>> addWishlistRecipe (
            @Header("Authorization") String authorization,
            @Query("user_id") String user_id,
            @Query("recipe_id") String recipe_id
    );

    @FormUrlEncoded //done
    @POST("account/login")
    Call<BaseResponse<LoginModel>> doLogin(
            @Field("email") String email,
            @Field("password") String password
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
}
