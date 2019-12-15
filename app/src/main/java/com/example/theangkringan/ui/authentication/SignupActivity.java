package com.example.theangkringan.ui.authentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.theangkringan.HomeActivity;
import com.example.theangkringan.R;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.LoginModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    private TextView tvSignIn;
    private Button btnSignup;

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private TextInputLayout inputFullname;
    private TextInputLayout inputLocation;
    private TextInputLayout inputPhone;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private TheAngkringanAPI appApi;

    static final String TAG = SignupActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputFullname = findViewById(R.id.input_fullname);
        inputLocation = findViewById(R.id.input_location);
        inputPhone = findViewById(R.id.input_phone_num);
        radioGroup = findViewById(R.id.groupradio);
        btnSignup = findViewById(R.id.btn_signup);

        tvSignIn = findViewById(R.id.tv_signin);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
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

    private boolean validatePassword(){
        String password = inputPassword.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            inputPassword.setError("Field can\'t be empty");
            return false;
        }else{
            inputPassword.setError(null);
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

    private void doRegister(){
        boolean isvalid = true;
        if(!validateEmail()){
            isvalid = false;
        }
        if(!validatePassword()){
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
            Log.d("radiobuttonnya: ", radioButton.getText().toString());
            callSingUp(
                    inputEmail.getEditText().getText().toString(),
                    inputPassword.getEditText().getText().toString(),
                    inputFullname.getEditText().getText().toString(),
                    inputPhone.getEditText().getText().toString(),
                    inputLocation.getEditText().getText().toString(),
                    radioButton.getText().toString());
        }
    }

    private void callSingUp(String username, String password, String fullname, String phonenum, String location, String gender){
        appApi = TheAngkringanServices.getRetrofit(SignupActivity.this).create(TheAngkringanAPI.class);
        Call<BaseResponse<LoginModel>> call = appApi.doRegiterMember(fullname, location, username,phonenum, gender, password);
        call.enqueue(new Callback<BaseResponse<LoginModel>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoginModel>> call, Response<BaseResponse<LoginModel>> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getData() != null) {
                            showDialog();
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            showErrorDialog();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<LoginModel>> call, Throwable t) {
                Log.d(TAG, "Error");
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Information!");
        alertDialogBuilder
                .setMessage("Akun berhasil di buat.")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showErrorDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Gagal!");
        alertDialogBuilder
                .setMessage("Akun Gagal di proses")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
