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
import com.ingenious.lblleadup.adapter.ShareEarnAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.ShareClass;
import com.ingenious.lblleadup.models.ShareableLink;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareEarnFragment extends Fragment {

    private RecyclerView rvShareEarn;
    private ShareEarnAdapter shareEarnAdapter;
    private List<ShareableLink> shareableLinkList = new ArrayList<>();
    private CustomProgressDialogue dialogue;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    public ShareEarnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_share_earn, container, false);

        rvShareEarn = v.findViewById(R.id.rvShareEarn);

        if (Utils.isOnline(Objects.requireNonNull(getContext())))
        {
            loadLink();
            setAdapter();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        rvShareEarn.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadLinkPagination();
                    }
                }
            }
        });

        return v;
    }

    private void loadLink()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.shareLink(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody("0"),
                Utils.getSimpleTextBody(Prefs.getString("language", Constant.Default_language))).enqueue(new Callback<ShareClass>() {
            @Override
            public void onResponse(Call<ShareClass> call, Response<ShareClass> response) {
                dialogue.cancel();
                if (response.body() != null && response.body().getSuccess())
                {
                    shareableLinkList = response.body().getShareableLinks();
                    shareEarnAdapter.updateData(shareableLinkList);
                }
                else
                {
                    Toasty.error(Objects.requireNonNull(getContext()), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ShareClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loadLinkPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.shareLink(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(String.valueOf(totalItems)),
                Utils.getSimpleTextBody(Prefs.getString("language",Constant.Default_language))).enqueue(new Callback<ShareClass>() {
            @Override
            public void onResponse(Call<ShareClass> call, Response<ShareClass> response) {
                if (response.body() != null && response.body().getSuccess())
                {
                    shareableLinkList = response.body().getShareableLinks();
                    shareEarnAdapter.addNewData(shareableLinkList);
                }
            }
            @Override
            public void onFailure(Call<ShareClass> call, Throwable t) {
            }
        });
    }

    private void setAdapter()
    {
        shareEarnAdapter = new ShareEarnAdapter(getContext(),shareableLinkList);
        rvShareEarn.setAdapter(shareEarnAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvShareEarn.setLayoutManager(manager);
    }

}
