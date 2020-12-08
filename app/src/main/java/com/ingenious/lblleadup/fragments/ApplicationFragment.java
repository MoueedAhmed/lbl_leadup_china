package com.ingenious.lblleadup.fragments;

import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.adapter.ApplicationAdapter;
import com.ingenious.lblleadup.adapter.ClickAndEarnAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Apps;
import com.ingenious.lblleadup.models.PlayApps;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationFragment extends Fragment {

    private RecyclerView rvApp;
    private List<Apps> appsList = new ArrayList<>();
    private ApplicationAdapter applicationAdapter;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_application, container, false);

        dialogue = new CustomProgressDialogue(getContext());

        rvApp = v.findViewById(R.id.rvApp);

        if (Utils.isOnline(Objects.requireNonNull(getContext())))
        {
            setAdapter();
            loadApps();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        rvApp.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadAppsPagination();
                    }
                }
            }
        });

        return v;
    }

    private void loadApps()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.loadApps(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody("0"),
                Utils.getSimpleTextBody(Prefs.getString("language","en"))).enqueue(new Callback<PlayApps>() {
            @Override
            public void onResponse(Call<PlayApps> call, Response<PlayApps> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body() != null && response.body().getSuccess())
                    {
                        appsList = response.body().getApps();
                        applicationAdapter.updateData(appsList);
                    }
                    else
                    {
                        assert response.body() != null;
                        Toasty.error(Objects.requireNonNull(getContext()),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<PlayApps> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loadAppsPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.loadApps(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody("0"),
                Utils.getSimpleTextBody(Prefs.getString("language","en"))).enqueue(new Callback<PlayApps>() {
            @Override
            public void onResponse(Call<PlayApps> call, Response<PlayApps> response) {
                if (response.isSuccessful())
                {
                    if (response.body() != null && response.body().getSuccess())
                    {
                        appsList = response.body().getApps();
                        applicationAdapter.addNewData(appsList);
                    }
                }
            }
            @Override
            public void onFailure(Call<PlayApps> call, Throwable t) {
            }
        });
    }

    private void setAdapter()
    {
        applicationAdapter = new ApplicationAdapter(getContext(),appsList);
        rvApp.setAdapter(applicationAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvApp.setLayoutManager(manager);
    }
}