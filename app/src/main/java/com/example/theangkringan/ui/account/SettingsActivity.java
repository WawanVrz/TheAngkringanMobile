package com.example.theangkringan.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.theangkringan.R;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.DetailUser;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private TheAngkringanAPI appApi;
    static final String TAG = SettingsActivity.class.getSimpleName();

    Button btn_logout;
    private AppPreferences userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btn_logout = findViewById(R.id.btn_logout);
        userPreference = new AppPreferences(this);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveUserDetail(Integer.parseInt(userPreference.getUserUnique(SettingsActivity.this)));
            }
        });
    }

    // =================================
    // CALL API
    // =================================

    private void retrieveUserDetail(int user_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(SettingsActivity.this).create(TheAngkringanAPI.class);
            Call<BaseResponse> call = appApi.doLogout(user_id);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("Logout successfully")) {
                                userPreference.clearUserToken(SettingsActivity.this);
                                finish();
                            }
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
    // =================================
}
