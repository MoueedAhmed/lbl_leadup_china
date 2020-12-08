package com.ingenious.lblleadup.activity;

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

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsConditionActivity extends AppCompatActivity {

    private CustomProgressDialogue dialogue;
    private MarkdownView markdownView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        dialogue = new CustomProgressDialogue(TermsConditionActivity.this);

        markdownView = (MarkdownView) findViewById(R.id.markdown_view);

        if (Utils.isOnline(TermsConditionActivity.this))
        {
            term_condition();
        }
        else
        {
            Toasty.error(TermsConditionActivity.this,R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private void term_condition()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.terms_and_conditions().enqueue(new Callback<GlobalResponse>() {
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
                        Toasty.error(TermsConditionActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(TermsConditionActivity.this, ServerErrorActivity.class));
            }
        });
    }
}