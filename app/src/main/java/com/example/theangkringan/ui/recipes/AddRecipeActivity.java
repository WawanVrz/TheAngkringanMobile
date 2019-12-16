package com.example.theangkringan.ui.recipes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.AddItemRecipeAdapter;
import com.example.theangkringan.models.AddRecipeModel;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.CategoryRecipeModel;
import com.example.theangkringan.models.CityModel;
import com.example.theangkringan.models.LocationModel;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipeActivity extends AppCompatActivity {


    private RecyclerView mRecIngridient;
    private AddItemRecipeAdapter mIngAdapter;
    private RecyclerView mRecCook;
    private AddItemRecipeAdapter mCookAdapter;

    private Spinner spinCategory;
    private Spinner spinProvince;
    private Spinner spinCity;
    private EditText input_title;
    private EditText input_link_video;
    private EditText input_notes;
    private EditText input_descrition;


    static final String TAG = AddRecipeActivity.class.getSimpleName();

    private Button takePictureButton;
    private ImageView imageView;
    private static final int PICK_IMAGE = 1;

    //api
    private TheAngkringanAPI appApi;
    private ArrayList<LocationModel> locationList = new ArrayList<>();
    private ArrayList<CityModel> cityList = new ArrayList<>();
    private ArrayList<CategoryRecipeModel> categoryList = new ArrayList<>();

    private int categoryPosition;
    private int cityPosition;
    private int ProvincePosition;
    Uri imageUri = null;
    private AppPreferences userPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Button addIng = findViewById(R.id.add_ingridients);
        final Button addStep = findViewById(R.id.add_cook_step);
        final Button btnProcess = findViewById(R.id.btn_process);
        input_title = findViewById(R.id.input_title);
        input_link_video = findViewById(R.id.input_link_video);
        input_notes = findViewById(R.id.input_notes);
        input_descrition = findViewById(R.id.input_descrition);

        spinCategory = findViewById(R.id.spin_category);
        spinProvince = findViewById(R.id.spin_province);
        spinCity = findViewById(R.id.spin_city);
        userPreference = new AppPreferences(AddRecipeActivity.this);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvincePosition = position;
                retrieveCity(String.valueOf(locationList.get(position).getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityPosition = position;
                retrieveCity(String.valueOf(locationList.get(position).getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecIngridient = findViewById(R.id.rv_add_ingridients);
        mRecIngridient.setHasFixedSize(true);
        initingridientview();

        mRecCook = findViewById(R.id.rv_add_cookstep);
        mRecCook.setHasFixedSize(true);
        initCookview();

        retrieveCategory();
        retrieveLocation();

        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInputRecipe();
            }
        });

        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInputStep();
            }
        });
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> ingridients = new ArrayList<>();
                ArrayList<String> stepcook = new ArrayList<>();

                Iterator IngridientIterator = mIngAdapter.getMap().keySet().iterator();
                Iterator CookStepIterator = mCookAdapter.getMap().keySet().iterator();

                while(IngridientIterator.hasNext()) {
                    int key=(int)IngridientIterator.next();
                    String value= mIngAdapter.getMap().get(key);
                    ingridients.add(value);
                }
                while(CookStepIterator.hasNext()) {
                    int key=(int)CookStepIterator.next();
                    String value= mCookAdapter.getMap().get(key);
                    stepcook.add(value);
                }
                String final_ingridients = convertJSON(ingridients, "ingredientsList","title_ing");
                String final_cooksteps = convertJSON(stepcook, "stepsList","title_step");

                String filePath = getRealPathFromURIPath(imageUri, AddRecipeActivity.this);
                File file = new File(filePath);

                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);

                SaveRecipe(userPreference.getUserToken(AddRecipeActivity.this),
                        Integer.parseInt(userPreference.getUserUnique(AddRecipeActivity.this)),
                        categoryList.get(categoryPosition).getId(),
                        input_title.getText().toString(),
                        input_descrition.getText().toString(),
                        fileToUpload,
                        input_link_video.getText().toString(),
                        final_ingridients,
                        final_cooksteps,
                        input_notes.getText().toString(),
                        locationList.get(ProvincePosition).getId(),
                        cityList.get(cityPosition).getId(),
                        1);
            }
        });

        doCamera();
    }

    private void doCamera(){
        takePictureButton = findViewById(R.id.button_image);
        imageView = findViewById(R.id.imageview);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            if (resultCode == RESULT_OK) {
                try {
                    imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddRecipeActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(AddRecipeActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    private void initingridientview(){
        mRecIngridient.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
        mIngAdapter = new AddItemRecipeAdapter(getApplicationContext());
        mIngAdapter.notifyDataSetChanged();
        mRecIngridient.setAdapter(mIngAdapter);

//        mIngAdapter.setOnItemClickCallback(new OnRecipeClickCallback() {
//            @Override
//            public void onItemClicked(RecipeModel data) {
//            }
//        });
    }

    private void initCookview(){
        mRecCook.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
        mCookAdapter = new AddItemRecipeAdapter(getApplicationContext());
        mCookAdapter.notifyDataSetChanged();
        mRecCook.setAdapter(mCookAdapter);
    }


    public void addInputRecipe(){
        mIngAdapter.setData("");
    }

    public void addInputStep(){
        mCookAdapter.setData("");
    }

    public String convertJSON(ArrayList<String> data, String type, String title){
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonMlistDao = new JSONArray();
        for (String dao:data
             ) {
            jsonMlistDao.put(dao);
        }

        JSONObject ingredientsObj = new JSONObject();
        try {
            ingredientsObj.put("mListIngDao", jsonMlistDao);
            ingredientsObj.put(title, "Bahan Utama");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArray.put(ingredientsObj);
        JSONObject generalObj = new JSONObject();

        try {
            generalObj.put(type, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonStr = generalObj.toString();
        return jsonStr;
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    //========================
    //Call API
    //province
    private void retrieveLocation(){
        try {
            appApi = TheAngkringanServices.getRetrofit(AddRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<LocationModel>>> call = appApi.getAllProvinces();
            call.enqueue(new Callback<BaseResponse<ArrayList<LocationModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<LocationModel>>> call, Response<BaseResponse<ArrayList<LocationModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                locationList.clear();
                                locationList.addAll(response.body().getData());
                                ArrayAdapter<LocationModel> adapter =
                                        new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list_item, locationList);
                                adapter.setDropDownViewResource(R.layout.dropdown_list_item);
                                spinProvince.setAdapter(adapter);
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
    //city
    private void retrieveCity(String province_id){
        try {
            appApi = TheAngkringanServices.getRetrofit(AddRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<CityModel>>> call = appApi.getCityByProvince(province_id);
            call.enqueue(new Callback<BaseResponse<ArrayList<CityModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<CityModel>>> call, Response<BaseResponse<ArrayList<CityModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                cityList.clear();
                                cityList.addAll(response.body().getData());
                                ArrayAdapter<CityModel> adapter =
                                        new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list_item, cityList);
                                adapter.setDropDownViewResource(R.layout.dropdown_list_item);
                                spinCity.setAdapter(adapter);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<CityModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //category
    private void retrieveCategory(){
        try {
            appApi = TheAngkringanServices.getRetrofit(AddRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<CategoryRecipeModel>>> call = appApi.getAllCategories();
            call.enqueue(new Callback<BaseResponse<ArrayList<CategoryRecipeModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<CategoryRecipeModel>>> call, Response<BaseResponse<ArrayList<CategoryRecipeModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null
                                    || response.body().getData().size() > 0) {
                                categoryList.clear();
                                categoryList.addAll(response.body().getData());
                                ArrayAdapter<CategoryRecipeModel> adapter =
                                        new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list_item, categoryList);
                                adapter.setDropDownViewResource(R.layout.dropdown_list_item);
                                spinCategory.setAdapter(adapter);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<CategoryRecipeModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveRecipe(String authorization,
                            int user_id, int recipe_category, String recipe_name, String description, MultipartBody.Part image,
                            String video, String ingredients, String cookmethd, String notes, int province, int city, int status){
        try {
            appApi = TheAngkringanServices.getRetrofit(AddRecipeActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse<AddRecipeModel>> call = appApi.addRecipe(user_id, recipe_category, recipe_name, description, image,
                    video, ingredients, cookmethd, notes, province, city, status);
            call.enqueue(new Callback<BaseResponse<AddRecipeModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<AddRecipeModel>> call, Response<BaseResponse<AddRecipeModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                Toast.makeText(getApplicationContext(), "Success Create Recipe", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<AddRecipeModel>> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //========================

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
