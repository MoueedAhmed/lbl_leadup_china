package com.ingenious.lblleadup.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.LoginActivity;
import com.ingenious.lblleadup.activity.PrivacyPolicyActivity;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.activity.TermsConditionActivity;
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
public class SettingFragment extends Fragment {

    private Dialog mDialog;
    private LinearLayout layout_contact_us, layout_language, layout_logout, layout_privacy, layout_terms_condition, layout_change_password;
    private CustomProgressDialogue progressDialogue;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        progressDialogue = new CustomProgressDialogue(getContext());

        layout_contact_us = v.findViewById(R.id.layout_contact_us);
        layout_language = v.findViewById(R.id.layout_language);
        layout_privacy = v.findViewById(R.id.layout_privacy);
        layout_terms_condition = v.findViewById(R.id.layout_terms_condition);
        layout_logout = v.findViewById(R.id.layout_logout);
        layout_change_password = v.findViewById(R.id.layout_change_password);

        layout_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ContactUsFragment());
            }
        });

        layout_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
            }
        });

        layout_terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TermsConditionActivity.class));
            }
        });

        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.remove("isLogin");
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        layout_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        layout_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        return v;
    }

    private void showChangePasswordDialog()
    {
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.layout_change_password);
        mDialog.setCancelable(true);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(mDialog.getWindow()).setLayout(width,height);
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_password = mDialog.findViewById(R.id.et_password);
        final EditText et_confirm_password = mDialog.findViewById(R.id.et_confirm_password);
        final Button btn_change = mDialog.findViewById(R.id.btn_change);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline(Objects.requireNonNull(getContext())))
                {
                    if (TextUtils.isEmpty(et_password.getText().toString()))
                    {
                        et_password.setError("Required");
                        et_password.requestFocus();
                    }
                    else if (TextUtils.isEmpty(et_confirm_password.getText().toString()))
                    {
                        et_confirm_password.setError("Required");
                        et_confirm_password.requestFocus();
                    }
                    else if (!et_password.getText().toString().equals(et_confirm_password.getText().toString()))
                    {
                        Toasty.error(getContext(),R.string.password_mismatch, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        changePass(et_password.getText().toString(),mDialog);
                    }
                }
                else
                {
                    Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });

        mDialog.show();
    }

    private void changePass(String password, final Dialog dialog)
    {
        progressDialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.changePassword(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(password),
                Utils.getSimpleTextBody(Prefs.getString("language","en"))).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response)
            {
                progressDialogue.dismiss();
                if (response.isSuccessful())
                {
                    dialog.dismiss();
                    if (response.body() != null && response.body().getSuccess())
                    {
                        Toasty.success(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toasty.error(Objects.requireNonNull(getContext()), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                progressDialogue.dismiss();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void showDialog()
    {
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.layout_language);
        LinearLayout layout_english = mDialog.findViewById(R.id.layout_english);
        LinearLayout layout_arabic = mDialog.findViewById(R.id.layout_arabic);
        LinearLayout layout_chines = mDialog.findViewById(R.id.layout_chines);
        LinearLayout layout_french = mDialog.findViewById(R.id.layout_french);

        layout_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Utils.setApplicationlanguage("en", Objects.requireNonNull(getContext()));
                Prefs.putString("language","en");
                reStartActivity(Objects.requireNonNull(getActivity()));
                mDialog.dismiss();
            }
        });

        layout_chines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setApplicationlanguage("zh", Objects.requireNonNull(getContext()));
                Prefs.putString("language","zh");
                reStartActivity(Objects.requireNonNull(getActivity()));
                mDialog.dismiss();
            }
        });

        layout_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setApplicationlanguage("ar",getContext());
                Prefs.putString("language","ar");
                reStartActivity(Objects.requireNonNull(getActivity()));
                mDialog.dismiss();
            }
        });

        layout_french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setApplicationlanguage("fr", Objects.requireNonNull(getContext()));
                Prefs.putString("language","fr");
                reStartActivity(Objects.requireNonNull(getActivity()));
                mDialog.dismiss();
            }
        });

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    private void reStartActivity(Activity activity)
    {
        Intent intent = activity.getIntent();
        activity.finish();
        startActivity(intent);
    }

}
