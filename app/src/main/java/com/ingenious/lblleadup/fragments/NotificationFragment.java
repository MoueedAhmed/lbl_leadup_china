package com.ingenious.lblleadup.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.adapter.NotificationAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Announcement;
import com.ingenious.lblleadup.models.Announcements;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private List<Announcement> notificationClassList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private RecyclerView rvNotification;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        rvNotification = v.findViewById(R.id.rvNotification);

        if (Utils.isOnline(getContext()))
        {
            setNotificationAdapter();
            notification();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        notificationPagination();
                    }
                }
            }
        });

        return v;
    }

    private void notification()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.announcements(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody("0")).enqueue(new Callback<Announcements>() {
            @Override
            public void onResponse(Call<Announcements> call, Response<Announcements> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body().getSuccess())
                    {
                        notificationClassList = response.body().getAnnouncements();
                        notificationAdapter.updateData(notificationClassList);
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Announcements> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void notificationPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.announcements(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<Announcements>() {
            @Override
            public void onResponse(Call<Announcements> call, Response<Announcements> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        notificationClassList = response.body().getAnnouncements();
                        notificationAdapter.addNewData(notificationClassList);
                    }
                }
            }
            @Override
            public void onFailure(Call<Announcements> call, Throwable t) {
            }
        });
    }

    private void setNotificationAdapter()
    {
        notificationAdapter = new NotificationAdapter(getContext(),notificationClassList);
        rvNotification.setAdapter(notificationAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvNotification.setLayoutManager(manager);
    }

}
