package com.ingenious.lblleadup.activity;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Animation;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Authorization;
import com.pixplicity.easyprefs.library.Prefs;

public class SplashActivity extends AppCompatActivity {

    private ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img_splash = findViewById(R.id.img_splash);

        Glide.with(this).load(R.drawable.splash).diskCacheStrategy(DiskCacheStrategy.NONE).into(img_splash);

        /*Load Default app language */
        Utils.setApplicationlanguage(Prefs.getString("language","zh") , SplashActivity.this);

        if (Utils.isOnline(this))
        {
            if (Prefs.getBoolean("isToken",false))
            {
                if (Prefs.getBoolean("isLogin",false))
                {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    Animation.slideLeft(SplashActivity.this);
                    finish();
                }
                else
                {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    Animation.slideLeft(SplashActivity.this);
                    finish();
                }
            }
            else
            {
                authentication();
            }
        }
        else
        {
            Toasty.error(SplashActivity.this,R.string.no_internet, Toast.LENGTH_LONG).show();
        }

    }

    private void authentication()
    {
        SOService soService = ApiUtils.getSOService();
        soService.getAuthentication(Utils.getSimpleTextBody(ApiUtils.grant_type),
                Utils.getSimpleTextBody(ApiUtils.client_id),
                Utils.getSimpleTextBody(ApiUtils.client_secret)).enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                Log.d("SplashOnResponseBody",response.toString()+"");
                if (response.isSuccessful())
                {
                    Prefs.putString("token",response.body().getAccessToken());
                    Prefs.putBoolean("isToken", true);
                    if (Prefs.getBoolean("isLogin",false))
                    {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        Animation.slideLeft(SplashActivity.this);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        Animation.slideLeft(SplashActivity.this);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t)
            {
                Log.d("SplashException",t.getMessage()+"");
                startActivity(new Intent(SplashActivity.this, ServerErrorActivity.class));
            }
        });
    }
}
