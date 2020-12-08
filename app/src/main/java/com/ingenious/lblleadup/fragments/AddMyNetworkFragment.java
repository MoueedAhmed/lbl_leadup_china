package com.ingenious.lblleadup.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
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
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.LeadSingleClass;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMyNetworkFragment extends Fragment {

    private List<String> categoriesList = new ArrayList<>();

    private Spinner spinner_category;
    private EditText et_name, et_email, et_phone, et_whats_app;
    private Button btn_save;
    private CustomProgressDialogue dialogue;
    private Bundle bundle;
    private String name = "" , phone = "" , id = "0";

    public AddMyNetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());
        bundle = getArguments();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_my_network, container, false);

        spinner_category = v.findViewById(R.id.spinner_category);
        et_name = v.findViewById(R.id.et_name);
        et_email = v.findViewById(R.id.et_email);
        et_phone = v.findViewById(R.id.et_phone);
        et_whats_app = v.findViewById(R.id.et_whats_app);
        btn_save = v.findViewById(R.id.btn_save);

        if (bundle != null)
        {
            name = bundle.getString("name");
            phone = bundle.getString("phone");
            id = bundle.getString("id","0");

            et_name.setText(name);
            et_phone.setText(phone);

            if (Utils.isOnline(getContext()))
            {
                load_my_lead();
            }
        }

        categoriesList.add("Family");
        categoriesList.add("Friends");
        categoriesList.add("Business");
        categoriesList.add("Other");
        spinner_category.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,categoriesList));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline(getContext()))
                {
                    if (TextUtils.isEmpty(et_name.getText().toString()))
                    {
                        et_name.setError("Required");
                        et_name.requestFocus();
                    }
                    else if (TextUtils.isEmpty(et_email.getText().toString()))
                    {
                        et_email.setError("Required");
                        et_email.requestFocus();
                    }
                    else if (!Utils.isValidEmail(et_email.getText().toString()))
                    {
                        Toasty.error(getContext(),R.string.in_valid_email, Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(et_phone.getText().toString()))
                    {
                        et_phone.setError("Required");
                        et_phone.requestFocus();
                    }
                    else if (TextUtils.isEmpty(et_whats_app.getText().toString()))
                    {
                        et_whats_app.setError("Required");
                        et_whats_app.requestFocus();
                    }
                    else
                    {
                        add_my_network();
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

    private void load_my_lead()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.lead_detail(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(id)).enqueue(new Callback<LeadSingleClass>() {
            @Override
            public void onResponse(Call<LeadSingleClass> call, Response<LeadSingleClass> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body().getSuccess())
                    {
                        et_name.setText(response.body().getName());
                        et_email.setText(response.body().getEmail());
                        et_phone.setText(response.body().getPhone());
                        et_whats_app.setText(response.body().getWhatsapp());
                        spinner_category.setSelection(categoriesList.indexOf(response.body().getCategory()));
                    }
                }
            }

            @Override
            public void onFailure(Call<LeadSingleClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void add_my_network()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.add_my_network_lead(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(id),
                Utils.getSimpleTextBody(et_name.getText().toString()),
                Utils.getSimpleTextBody(et_email.getText().toString()),
                Utils.getSimpleTextBody(spinner_category.getSelectedItem().toString()),
                Utils.getSimpleTextBody(et_phone.getText().toString()),
                Utils.getSimpleTextBody(et_whats_app.getText().toString())).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body().getSuccess())
                    {
                        Toasty.success(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
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
