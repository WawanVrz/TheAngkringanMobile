package com.example.theangkringan.ui.account;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.theangkringan.HomeActivity;
import com.example.theangkringan.R;
import com.example.theangkringan.services.AppPreferences;
import com.example.theangkringan.ui.authentication.LoginActivity;
import com.example.theangkringan.ui.event.EventFragment;
import com.example.theangkringan.ui.recipes.RecipesFragment;
import com.google.android.material.tabs.TabLayout;

public class AccountFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private LinearLayout user_layout;
    private LinearLayout sign_in_layout;
    private Button btn_sign_in;


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

        if(!TextUtils.isEmpty(AppPreferences.getUserToken(getActivity()))){

            user_layout.setVisibility(View.VISIBLE);
            sign_in_layout.setVisibility(View.GONE);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

            mViewPager = (ViewPager) view.findViewById(R.id.vp_container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.account_nav_title)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.app_name)));

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }else{
            btn_sign_in = view.findViewById(R.id.btn_sign_in);
            user_layout.setVisibility(View.GONE);
            sign_in_layout.setVisibility(View.VISIBLE);
            btn_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
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

}
