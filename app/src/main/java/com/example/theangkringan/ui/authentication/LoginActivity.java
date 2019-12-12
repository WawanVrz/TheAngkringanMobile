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
import android.widget.TextView;

import com.example.theangkringan.HomeActivity;
import com.example.theangkringan.R;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.LoginModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPassword;
    private TextView linkToSignUp;
    private Button btnSignIn;

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;

    private TheAngkringanAPI appApi;

    static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkToSignUp = findViewById(R.id.tv_signup);
        forgotPassword = findViewById(R.id.tv_btn_forgot_password);
        btnSignIn = findViewById(R.id.btn_signin);

        inputEmail = findViewById(R.id.edt_username);
        inputPassword = findViewById(R.id.edt_password);

        linkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
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

    private void doLogin(){
        if(validateEmail() && validatePassword()){
            callLogin(inputEmail.getEditText().getText().toString(), inputPassword.getEditText().getText().toString());
        }
    }

    private void callLogin(String username, String password){
        appApi = TheAngkringanServices.getRetrofit().create(TheAngkringanAPI.class);
        Call<BaseResponse<LoginModel>> call = appApi.doLogin(username, password);
        call.enqueue(new Callback<BaseResponse<LoginModel>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoginModel>> call, Response<BaseResponse<LoginModel>> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getData() != null) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            showDialog();
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
                .setMessage("Akun yang anda masukan tidak terdaftar di sistem!")
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
