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
import com.ingenious.lblleadup.Utils.Constant;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.adapter.ClickAndEarnAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.ClickLink;
import com.ingenious.lblleadup.models.LinkClass;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClickEarnFragment extends Fragment {

    private List<ClickLink> clickandEarnList = new ArrayList<>();
    private ClickAndEarnAdapter clickAndEarnAdapter;
    private RecyclerView rvClickEarn;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;
    private String TAG = "ClickEarnFragmentResponse";

    public ClickEarnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_click_earn, container, false);

        rvClickEarn = v.findViewById(R.id.rvClickEarn);

        if (Utils.isOnline(Objects.requireNonNull(getContext())))
        {
            setClickAndEarnAdapter();
            link();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }


        rvClickEarn.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        linkPagination();
                    }
                }
            }
        });

        return v;
    }

    private void link()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.links(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody("0"),
                Utils.getSimpleTextBody(Prefs.getString("language", Constant.Default_language))).enqueue(new Callback<LinkClass>() {
            @Override
            public void onResponse(Call<LinkClass> call, Response<LinkClass> response)
            {
                Log.d(TAG , response.toString());
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body() != null && response.body().getSuccess())
                    {
                        clickandEarnList = response.body().getClickLinks();
                        clickAndEarnAdapter.updateData(clickandEarnList);
                    }
                    else
                    {
                        assert response.body() != null;
                        Toasty.error(Objects.requireNonNull(getContext()),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<LinkClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void linkPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.links(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(String.valueOf(totalItems)),
                Utils.getSimpleTextBody(Prefs.getString("language",Constant.Default_language))).enqueue(new Callback<LinkClass>() {
            @Override
            public void onResponse(Call<LinkClass> call, Response<LinkClass> response) {
                if (response.isSuccessful())
                {
                    if (response.body() != null && response.body().getSuccess()) {
                        clickandEarnList = response.body().getClickLinks();
                        clickAndEarnAdapter.addNewData(clickandEarnList);
                    }
                }
            }
            @Override
            public void onFailure(Call<LinkClass> call, Throwable t) {
            }
        });
    }

    private void setClickAndEarnAdapter()
    {
        clickAndEarnAdapter = new ClickAndEarnAdapter(getContext(),clickandEarnList);
        rvClickEarn.setAdapter(clickAndEarnAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvClickEarn.setLayoutManager(manager);
    }

}
