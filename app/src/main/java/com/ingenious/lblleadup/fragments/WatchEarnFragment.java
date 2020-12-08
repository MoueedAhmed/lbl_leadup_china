package com.ingenious.lblleadup.fragments;


import android.os.Bundle;
import android.content.Intent;

import com.ingenious.lblleadup.Utils.Constant;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.adapter.WatchAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Video;
import com.ingenious.lblleadup.models.VideoClass;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchEarnFragment extends Fragment {

    private RecyclerView rvWatchEarn;
    private LinearLayoutManager manager;
    private WatchAdapter watchAdapter;
    private List<Video> watchEarnList = new ArrayList<>();
    private CustomProgressDialogue dialogue;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    public WatchEarnFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_watch_earn, container, false);

        rvWatchEarn = v.findViewById(R.id.rvWatchEarn);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Utils.isOnline(getContext()))
        {
            setVdieoAdapter();
            video();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }


        rvWatchEarn.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(dy > 0)
                {
                    if (isScrolling && (currentItems + scrollOutItems == totalItems))
                    {
                        //Fetch new Data
                        isScrolling = false;
                        videoPagination();
                    }
                }
            }
        });
    }

    private void video()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.video(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody("0"),
                Utils.getSimpleTextBody(Prefs.getString("language", Constant.Default_language))).enqueue(new Callback<VideoClass>() {
            @Override
            public void onResponse(Call<VideoClass> call, Response<VideoClass> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body().getSuccess())
                    {
                        watchEarnList.clear();
                        watchEarnList = response.body().getVideos();
                        watchAdapter.addNewData(watchEarnList);
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void videoPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.video(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(String.valueOf(totalItems)),
                Utils.getSimpleTextBody(Prefs.getString("language",Constant.Default_language))).enqueue(new Callback<VideoClass>() {
            @Override
            public void onResponse(Call<VideoClass> call, Response<VideoClass> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        watchEarnList = response.body().getVideos();
                        watchAdapter.addNewData(watchEarnList);
                    }
                }
            }
            @Override
            public void onFailure(Call<VideoClass> call, Throwable t) {
            }
        });
    }

    private void setVdieoAdapter()
    {
        watchAdapter = new WatchAdapter(getContext(),watchEarnList);
        rvWatchEarn.setAdapter(watchAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvWatchEarn.setLayoutManager(manager);
    }

}
