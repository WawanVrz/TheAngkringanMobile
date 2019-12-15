package com.example.theangkringan.ui.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.theangkringan.R;

public class DetailEventActivity extends AppCompatActivity {

    public static String DATA_IMG = "data_img";
    public static String DATA_TITLE = "data_title";
    public static String DATA_DESC = "data_desc";

    private ImageView imgCover;
    private TextView tvTitle;
    private TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.event_and_promo_title));
        }

        setContentView(R.layout.activity_detail_event);
        imgCover = findViewById(R.id.img_event_promo);
        tvTitle = findViewById(R.id.tv_event_promo);
        tvDesc = findViewById(R.id.tv_event_content);

        Glide.with(getApplicationContext())
                .load(getIntent().getExtras().get(DATA_IMG).toString())
                .into(imgCover);
        tvTitle.setText(getIntent().getExtras().get(DATA_TITLE).toString());
        tvDesc.setText(getIntent().getExtras().get(DATA_DESC).toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
