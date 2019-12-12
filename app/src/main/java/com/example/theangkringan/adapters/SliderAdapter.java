package com.example.theangkringan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.theangkringan.R;
import com.example.theangkringan.models.EventModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<EventModel> bannerList = new ArrayList<>();

    public SliderAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.item_slider_banner, container, false);

//        mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, DetailEventActivity.class);
//                mContext.startActivity(intent);
//            }
//        });

        ImageView imgView = mView.findViewById(R.id.img_item_banner);
        Glide.with(mContext)
                .load(bannerList.get(position).getImage())
                .into(imgView);
        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setBannerList(ArrayList<EventModel> items) {
        bannerList.clear();
        bannerList.addAll(items);
        notifyDataSetChanged();
    }
}
