package com.ingenious.lblleadup.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectedIncomeFragment extends Fragment {

    private TextView tv_clicks_count , tv_earning_count , tv_video_count, tv_watch_count , tv_network_count, tv_network_earning_count , tv_share_count , tv_share_earning_count;
    private CrystalSeekbar seekClick , watch_seek, network_seek, share_seek;

    public ProjectedIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_projected_income, container, false);

        seekClick = v.findViewById(R.id.seekClick);
        watch_seek = v.findViewById(R.id.watch_seek);
        network_seek = v.findViewById(R.id.network_seek);
        share_seek = v.findViewById(R.id.share_seek);
        tv_clicks_count = v.findViewById(R.id.tv_clicks_count);
        tv_earning_count = v.findViewById(R.id.tv_earning_count);
        tv_video_count = v.findViewById(R.id.tv_video_count);
        tv_watch_count = v.findViewById(R.id.tv_watch_count);
        tv_network_count = v.findViewById(R.id.tv_network_count);
        tv_network_earning_count = v.findViewById(R.id.tv_network_earning_count);
        tv_share_count = v.findViewById(R.id.tv_share_count);
        tv_share_earning_count = v.findViewById(R.id.tv_share_earning_count);


        seekClick.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number i)
            {
                tv_clicks_count.setText(String.valueOf(i));
                if (i.intValue()>0 && i.intValue()<10)
                    tv_earning_count.setText(Constant.Currency+" 0.00");

                if (i.intValue()>10 && i.intValue()<20)
                    tv_earning_count.setText(Constant.Currency+" 0.15");

                if (i.intValue()>20 && i.intValue()<30)
                    tv_earning_count.setText(Constant.Currency+" 0.30");

                if (i.intValue()>30 && i.intValue()<40)
                    tv_earning_count.setText(Constant.Currency+" 0.45");

                if (i.intValue()>40 && i.intValue()<50)
                    tv_earning_count.setText(Constant.Currency+" 0.60");

                if (i.intValue()>50 && i.intValue()<60)
                    tv_earning_count.setText(Constant.Currency+" 0.75");

                if (i.intValue()>60 && i.intValue()<70)
                    tv_earning_count.setText(Constant.Currency+" 0.90");

                if (i.intValue()>70 && i.intValue()<80)
                    tv_earning_count.setText(Constant.Currency+" 1.05");

                if (i.intValue()>80 && i.intValue()<90)
                    tv_earning_count.setText(Constant.Currency+" 1.20");

                if (i.intValue()>90 && i.intValue()<100)
                    tv_earning_count.setText(Constant.Currency+" 1.35");
            }
        });

        watch_seek.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number i)
            {
                tv_video_count.setText(String.valueOf(i));
                if (i.intValue()>0 && i.intValue()<10)
                    tv_watch_count.setText(Constant.Currency+" 0.00");

                if (i.intValue()>10 && i.intValue()<20)
                    tv_watch_count.setText(Constant.Currency+" 0.15");

                if (i.intValue()>20 && i.intValue()<30)
                    tv_watch_count.setText(Constant.Currency+" 0.30");

                if (i.intValue()>30 && i.intValue()<40)
                    tv_watch_count.setText(Constant.Currency+" 0.45");

                if (i.intValue()>40 && i.intValue()<50)
                    tv_watch_count.setText(Constant.Currency+" 0.60");

                if (i.intValue()>50 && i.intValue()<60)
                    tv_watch_count.setText(Constant.Currency+" 0.75");

                if (i.intValue()>60 && i.intValue()<70)
                    tv_watch_count.setText(Constant.Currency+" 0.90");

                if (i.intValue()>70 && i.intValue()<80)
                    tv_watch_count.setText(Constant.Currency+" 1.05");

                if (i.intValue()>80 && i.intValue()<90)
                    tv_watch_count.setText(Constant.Currency+" 1.20");

                if (i.intValue()>90 && i.intValue()<100)
                    tv_watch_count.setText(Constant.Currency+" 1.35");
            }
        });

        network_seek.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number i)
            {
                tv_network_count.setText(String.valueOf(i));
                if (i.intValue()<10)
                    tv_network_earning_count.setText(Constant.Currency+" 0");

                if (i.intValue()==10)
                    tv_network_earning_count.setText(Constant.Currency+" 1.5");

                if (i.intValue()>10 || i.intValue() == 10 && i.intValue()<20)
                    tv_network_earning_count.setText(Constant.Currency+" 1.5");

                if (i.intValue()>20 || i.intValue() == 20 && i.intValue()<30)
                    tv_network_earning_count.setText(Constant.Currency+" 3");

                if (i.intValue()>30 || i.intValue() == 30 && i.intValue()<40)
                    tv_network_earning_count.setText(Constant.Currency+"4.5");

                if (i.intValue()>40 || i.intValue() == 40 && i.intValue()<50)
                    tv_network_earning_count.setText(Constant.Currency+" 6");

                if (i.intValue()>50 || i.intValue() == 50 && i.intValue()<60)
                    tv_network_earning_count.setText(Constant.Currency+" 7.5");

                if (i.intValue()>60 || i.intValue() == 60 && i.intValue()<70)
                    tv_network_earning_count.setText(Constant.Currency+" 9");

                if (i.intValue()>70 || i.intValue() == 70 && i.intValue()<80)
                    tv_network_earning_count.setText(Constant.Currency+" 10.5");

                if (i.intValue()>80 || i.intValue() == 80 && i.intValue()<90)
                    tv_network_earning_count.setText(Constant.Currency+" 12");

                if (i.intValue()>90 || i.intValue() == 90 && i.intValue()<100)
                    tv_network_earning_count.setText(Constant.Currency+" 13.5");

            }
        });

        share_seek.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number i)
            {
                tv_share_count.setText(String.valueOf(i));
                if (i.intValue()>0 && i.intValue()<10)
                    tv_share_earning_count.setText(Constant.Currency+" 0.00");

                if (i.intValue()>10 && i.intValue()<20)
                    tv_share_earning_count.setText(Constant.Currency+" 0.15");

                if (i.intValue()>20 && i.intValue()<30)
                    tv_share_earning_count.setText(Constant.Currency+" 0.30");

                if (i.intValue()>30 && i.intValue()<40)
                    tv_share_earning_count.setText(Constant.Currency+" 0.45");

                if (i.intValue()>40 && i.intValue()<50)
                    tv_share_earning_count.setText(Constant.Currency+" 0.60");

                if (i.intValue()>50 && i.intValue()<60)
                    tv_share_earning_count.setText(Constant.Currency+" 0.75");

                if (i.intValue()>60 && i.intValue()<70)
                    tv_share_earning_count.setText(Constant.Currency+" 0.90");

                if (i.intValue()>70 && i.intValue()<80)
                    tv_share_earning_count.setText(Constant.Currency+" 1.05");

                if (i.intValue()>80 && i.intValue()<90)
                    tv_share_earning_count.setText(Constant.Currency+" 1.20");

                if (i.intValue()>90 && i.intValue()<100)
                    tv_share_earning_count.setText(Constant.Currency+" 1.35");
            }
        });

        return v;
    }

}
