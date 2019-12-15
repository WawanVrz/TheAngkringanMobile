package com.example.theangkringan.ui.account;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theangkringan.HomeActivity;
import com.example.theangkringan.R;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.DetailUser;
import com.example.theangkringan.models.RecipeModel;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;
import com.example.theangkringan.ui.authentication.LoginActivity;
import com.example.theangkringan.ui.event.EventFragment;
import com.example.theangkringan.ui.recipes.RecipesFragment;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AccountFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private LinearLayout user_layout;
    private LinearLayout sign_in_layout;
    private Button btn_sign_in;

    private ArrayList<DetailUser> userData = new ArrayList<>();
    static final String TAG = AccountFragment.class.getSimpleName();

    private CircleImageView img_latest_rec;
    private TextView tv_full_name;
    private TextView tv_detail_email;
    private TextView tv_gender;
    private TextView tv_phone;
    private TextView btn_gotosettings;

    private TheAngkringanAPI appApi;
    private AppPreferences userPreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user_layout = view.findViewById(R.id.user_layout);
        sign_in_layout = view.findViewById(R.id.sign_in_layout);
        btn_sign_in = view.findViewById(R.id.btn_sign_in);

        img_latest_rec = view.findViewById(R.id.img_latest_rec);
        tv_full_name = view.findViewById(R.id.tv_full_name);
        tv_detail_email = view.findViewById(R.id.tv_detail_email);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_phone = view.findViewById(R.id.tv_phone);
        btn_gotosettings = view.findViewById(R.id.btn_gotosettings);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_container);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        userPreference = new AppPreferences(getActivity());
        if(!TextUtils.isEmpty(userPreference.getUserToken(getActivity()))){
            user_layout.setVisibility(View.VISIBLE);
            sign_in_layout.setVisibility(View.GONE);
        }else{
            user_layout.setVisibility(View.GONE);
            sign_in_layout.setVisibility(View.VISIBLE);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        retrieveUserDetail(userPreference.getUserUnique(getActivity()));

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_my_recipe)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_my_wishlist)));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        btn_gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return new MyRecipeFragment();
                case 1:
                    return new WishlistFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    // =================================
    // CALL API
    // =================================

    private void retrieveUserDetail(String user_id) {
        try {
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<DetailUser>>> call = appApi.getDetailUser(user_id);
            call.enqueue(new Callback<BaseResponse<ArrayList<DetailUser>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<DetailUser>>> call, Response<BaseResponse<ArrayList<DetailUser>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                userData.addAll(response.body().getData());
                                setUserData(userData);
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
    // =================================

    private void setUserData(ArrayList<DetailUser> data){
        Glide.with(getActivity())
                .load(data.get(0).getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo))
                .into(img_latest_rec);
        tv_full_name.setText(data.get(0).getFullname());
        tv_detail_email.setText(data.get(0).getEmail());
        tv_gender.setText(data.get(0).getGender());
        tv_phone.setText(data.get(0).getPhone());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(userPreference.getUserToken(getActivity()))) {
            user_layout.setVisibility(View.VISIBLE);
            sign_in_layout.setVisibility(View.GONE);
        } else{
            user_layout.setVisibility(View.GONE);
            sign_in_layout.setVisibility(View.VISIBLE);
        }
    }
}
