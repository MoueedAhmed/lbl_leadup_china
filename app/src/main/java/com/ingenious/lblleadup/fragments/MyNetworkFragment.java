package com.ingenious.lblleadup.fragments;


import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.MyNetwork;
import com.ingenious.lblleadup.models.PhoneContact;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyNetworkFragment extends Fragment {

    private LinearLayout layout_by_phone, layout_by_manually;
    private TextView tv_total_prospector_count;
    private CustomProgressDialogue dialogue;
    private ShapeOfView shape_pros_count;

    public MyNetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_network, container, false);

        layout_by_phone = v.findViewById(R.id.layout_by_phone);
        layout_by_manually = v.findViewById(R.id.layout_by_manually);
        tv_total_prospector_count = v.findViewById(R.id.tv_total_prospector_count);
        shape_pros_count = v.findViewById(R.id.shape_pros_count);

        layout_by_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ContactFragment());
            }
        });

        layout_by_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new AddMyNetworkFragment());
            }
        });

        shape_pros_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new MyNetworkContactsFragment());
            }
        });

        if (Utils.isOnline(getContext()))
        {
            myNetwork();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        return v;
    }

    private void myNetwork()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.my_Network(Utils.getSimpleTextBody(Prefs.getString("user_id",""))).enqueue(new Callback<MyNetwork>() {
            @Override
            public void onResponse(Call<MyNetwork> call, Response<MyNetwork> response)
            {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body().getSuccess())
                    {
                        tv_total_prospector_count.setText(response.body().getTotalLeads());
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyNetwork> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

}
