package com.ingenious.lblleadup.fragments;


import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FundTransferFragment extends Fragment {

    private String[] payment_method = {"Paypal" , "Bank Transfer" , "Other"};
    private Spinner spinner_method;
    private EditText et_requested_amount, et_message;
    private CustomProgressDialogue dialogue;
    private Button btn_request;

    public FundTransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fund_transfer, container, false);

        spinner_method = v.findViewById(R.id.spinner_method);
        et_requested_amount = v.findViewById(R.id.et_requested_amount);
        et_message = v.findViewById(R.id.et_message);
        btn_request = v.findViewById(R.id.btn_request);

        spinner_method.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_dropdown_item,payment_method));

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline(Objects.requireNonNull(getContext())))
                {
                    if (TextUtils.isEmpty(et_requested_amount.getText().toString()))
                    {
                        et_requested_amount.setError("Required");
                        et_requested_amount.requestFocus();
                    }
                    else if (TextUtils.isEmpty(et_message.getText().toString()))
                    {
                        et_message.setError("Required");
                        et_message.requestFocus();
                    }
                    else
                    {
                        fundTransfer();
                    }
                }
                else
                {
                    Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    private void fundTransfer()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.fund_transfer(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(et_requested_amount.getText().toString()),
                Utils.getSimpleTextBody(spinner_method.getSelectedItem().toString()),
                Utils.getSimpleTextBody(et_message.getText().toString())).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body() != null && response.body().getSuccess())
                    {
                        Toasty.success(Objects.requireNonNull(getContext()),response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    }
                    else
                    {
                        assert response.body() != null;
                        Toasty.error(Objects.requireNonNull(getContext()),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }
}
