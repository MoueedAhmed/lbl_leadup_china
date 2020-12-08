package com.ingenious.lblleadup.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.HomeActivity;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningDashboardFragment extends Fragment {

    private LinearLayout layout_my_network, layout_project_income, layout_my_income, layout_click_earn, layout_watch_earn, layout_shop_earn, layout_share_earn, layout_application;

    public EarningDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_earning_dashboard, container, false);

        layout_my_network = v.findViewById(R.id.layout_my_network);
        layout_watch_earn = v.findViewById(R.id.layout_watch_earn);
        layout_project_income = v.findViewById(R.id.layout_project_income);
        layout_my_income = v.findViewById(R.id.layout_my_income);
        layout_click_earn = v.findViewById(R.id.layout_click_earn);
        layout_watch_earn = v.findViewById(R.id.layout_watch_earn);
        layout_shop_earn = v.findViewById(R.id.layout_shop_earn);
        layout_share_earn = v.findViewById(R.id.layout_share_earn);
        layout_application = v.findViewById(R.id.layout_application);

        layout_project_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ProjectedIncomeFragment());
            }
        });

        layout_share_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ShareEarnFragment());
            }
        });

        layout_shop_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ShopFragment());
            }
        });

        layout_my_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new MyNetworkFragment());
            }
        });

        layout_my_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new EarningFragment());
            }
        });

        layout_click_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ClickEarnFragment());
            }
        });

        layout_watch_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new WatchEarnFragment());
            }
        });

        layout_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.loadFragment(getContext(),new ApplicationFragment());
            }
        });
        return v;
    }

}
