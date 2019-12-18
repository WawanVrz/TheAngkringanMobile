package com.example.theangkringan.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;
import retrofit2.http.Query;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theangkringan.R;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.DetailUser;
import com.example.theangkringan.models.UpdateProfile;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.example.theangkringan.ui.authentication.LoginActivity;
import com.example.theangkringan.ui.authentication.SignupActivity;
import com.example.theangkringan.ui.recipes.AddRecipeActivity;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;

public class AccountUpdateActivity extends AppCompatActivity {

    private TextInputLayout inputEmail;
    private TextInputLayout inputFullname;
    private TextInputLayout inputLocation;
    private TextInputLayout inputPhone;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private ImageView img_profile;
    private Button btnUpdate;
    private Button btnImage;
    private ProgressBar progressBar;
    private ArrayList<DetailUser> userData = new ArrayList<>();
    static final String TAG = AccountUpdateActivity.class.getSimpleName();

    private TheAngkringanAPI appApi;
    private AppPreferences userPreference;
    Uri imageUri = null;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);

        inputEmail = findViewById(R.id.update_email);
        inputFullname = findViewById(R.id.update_fullname);
        inputLocation = findViewById(R.id.update_location);
        inputPhone = findViewById(R.id.update_phone_num);
        radioGroup = findViewById(R.id.update_groupradio);
        btnUpdate = findViewById(R.id.btn_update_profile);
        btnImage = findViewById(R.id.btn_image);
        progressBar = findViewById(R.id.update_loading);
        img_profile = findViewById(R.id.img_profile);

        inputEmail.setEnabled(false);
        userPreference = new AppPreferences(this);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        doCamera();

        retrieveUserDetail(userPreference.getUserUnique(this));

    }

    private void setDataMember(String email, String name, String loc, String phone, String image, String gender){
        inputEmail.getEditText().setText(email);
        inputFullname.getEditText().setText(name);
        inputLocation.getEditText().setText(loc);
        inputPhone.getEditText().setText(phone);
        Glide.with(this)
                .load(image)
                .apply(new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo))
                .into(img_profile);
        if(gender.equals("Male")){
            ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        }else{
            ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
        }

    }


    private boolean validateEmail(){
        String emailAddr = inputEmail.getEditText().getText().toString().trim();
        if(emailAddr.isEmpty()){
            inputEmail.setError("Field can\'t be empty");
            return false;
        }else{
            inputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePhone(){
        String phone = inputPhone.getEditText().getText().toString().trim();
        if(phone.isEmpty()){
            inputPhone.setError("Field can\'t be empty");
            return false;
        }else{
            inputPhone.setError(null);
            return true;
        }
    }

    private boolean validateLocation(){
        String location = inputLocation.getEditText().getText().toString().trim();
        if(location.isEmpty()){
            inputLocation.setError("Field can\'t be empty");
            return false;
        }else{
            inputLocation.setError(null);
            return true;
        }
    }

    private boolean validateFullname(){
        String fullname = inputFullname.getEditText().getText().toString().trim();
        if(fullname.isEmpty()){
            inputFullname.setError("Field can\'t be empty");
            return false;
        }else{
            inputFullname.setError(null);
            return true;
        }
    }

    private void updateProfile(){
        boolean isvalid = true;
        if(!validateEmail()){
            isvalid = false;
        }
        if(!validatePhone()){
            isvalid = false;
        }
        if(!validateFullname()){
            isvalid = false;
        }
        if(!validateLocation()){
            isvalid = false;
        }

        if (isvalid){
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);

            String filePath = getRealPathFromURIPath(imageUri, AccountUpdateActivity.this);
            File file = new File(filePath);

            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);

            sendProfile(
                    Integer.parseInt(userPreference.getUserUnique(AccountUpdateActivity.this)),
                    inputFullname.getEditText().getText().toString(),
                    inputEmail.getEditText().getText().toString(),
                    inputPhone.getEditText().getText().toString(),
                    fileToUpload,
                    inputLocation.getEditText().getText().toString(),
                    radioButton.getText().toString());
        }
    }

    private void doCamera(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btnImage.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        btnImage.setOnClickListener(new View.OnClickListener() {
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
                btnImage.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            if (resultCode == RESULT_OK) {
                try {
                    imageUri = data.getData();
                    img_profile.setImageURI(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AccountUpdateActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(AccountUpdateActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
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

    //call api
    private void retrieveUserDetail(String user_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(this).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<DetailUser>>> call = appApi.getDetailUser(user_id);
            call.enqueue(new Callback<BaseResponse<ArrayList<DetailUser>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<DetailUser>>> call, Response<BaseResponse<ArrayList<DetailUser>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                userData.addAll(response.body().getData());
                                setDataMember(userData.get(0).getEmail(),userData.get(0).getFullname(),
                                        userData.get(0).getAddress(),userData.get(0).getPhone(),
                                        userData.get(0).getAvatar(), userData.get(0).getGender());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<DetailUser>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendProfile(int user_id, String fullname, String email, String phone, MultipartBody.Part avatar,String address, String gender) {
        try {
            showLoading(true);
            appApi = TheAngkringanServices.getRetrofit(this).create(TheAngkringanAPI.class);
            Call<BaseResponse<UpdateProfile>> call = appApi.updateProfile(user_id, fullname,  email,  phone, avatar, address,  gender);
            call.enqueue(new Callback<BaseResponse<UpdateProfile>>() {
                @Override
                public void onResponse(Call<BaseResponse<UpdateProfile>> call, Response<BaseResponse<UpdateProfile>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                showLoading(false);
                                Toast.makeText(AccountUpdateActivity.this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        showLoading(false);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<UpdateProfile>> call, Throwable t) {
                    showLoading(false);
                }
            });
        } catch (Exception e) {
            showLoading(false);
            e.printStackTrace();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
