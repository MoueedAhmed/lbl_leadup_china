package com.ingenious.lblleadup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.mukesh.MarkdownView;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private CustomProgressDialogue dialogue;
    private MarkdownView markdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        dialogue = new CustomProgressDialogue(PrivacyPolicyActivity.this);

        markdownView = (MarkdownView) findViewById(R.id.markdown_view);

        if (Utils.isOnline(PrivacyPolicyActivity.this))
        {
            laodPrivacyPolicy();
        }
        else
        {
            Toasty.error(PrivacyPolicyActivity.this,R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private void laodPrivacyPolicy()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.privacy_policy().enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body() != null && response.body().getSuccess())
                    {
                        markdownView.setMarkDownText(response.body().getMessage()); //Displays markdown text
                    }
                    else
                    {
                        Toasty.error(PrivacyPolicyActivity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(PrivacyPolicyActivity.this, ServerErrorActivity.class));
            }
        });
    }
}