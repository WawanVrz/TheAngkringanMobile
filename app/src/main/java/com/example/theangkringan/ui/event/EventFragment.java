package com.example.theangkringan.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.theangkringan.R;
import com.example.theangkringan.adapters.EventAdapter;
import com.example.theangkringan.interfaces.OnEventClickCallback;
import com.example.theangkringan.models.BaseResponse;
import com.example.theangkringan.models.EventModel;
import com.example.theangkringan.services.TheAngkringanAPI;
import com.example.theangkringan.services.TheAngkringanServices;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private EventAdapter mAdapter;
    private ArrayList<EventModel> listEvent = new ArrayList<>();

    private TheAngkringanAPI appApi;
    private ArrayList<EventModel> promosList = new ArrayList<>();
    private ProgressBar progressBar;
    private LinearLayout layoutError;

    static final String TAG = EventFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_promo, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.event_loading);
        layoutError = view.findViewById(R.id.layout_error);

        mRecyclerview = view.findViewById(R.id.rv_latest_recipe);
        mRecyclerview.setHasFixedSize(true);
        initRecyclerview();
        retrievePromo();
    }

    private void initRecyclerview(){
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EventAdapter();
        mAdapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickCallback(new OnEventClickCallback() {
            @Override
            public void onItemClicked(EventModel data) {
                Intent intent = new Intent(getActivity(), DetailEventActivity.class);
                intent.putExtra(DetailEventActivity.DATA_TITLE, data.getTitle());
                intent.putExtra(DetailEventActivity.DATA_DESC, data.getSubtitle());
                intent.putExtra(DetailEventActivity.DATA_IMG, data.getImage());
                startActivity(intent);
            }
        });
    }

    private void retrievePromo() {
        try {
            showLoading(true);
            appApi = TheAngkringanServices.getRetrofit(getActivity()).create(TheAngkringanAPI.class);
            Call<BaseResponse<ArrayList<EventModel>>> call = appApi.getAllPromos();
            call.enqueue(new Callback<BaseResponse<ArrayList<EventModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<EventModel>>> call, Response<BaseResponse<ArrayList<EventModel>>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getData() != null){
                                if(response.body().getData().size() > 0) {
                                    dataFound();
                                    listEvent.addAll(response.body().getData());
                                    mAdapter.setEventData(listEvent);
                                    showLoading(false);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLoading(false);
                        dataNotFound();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<EventModel>>> call, Throwable t) {
                    Log.d(TAG, "Error");
                    showLoading(false);
                    dataNotFound();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showLoading(false);
            dataNotFound();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void dataNotFound(){
        mRecyclerview.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
    }

    private void dataFound(){
        mRecyclerview.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }
}