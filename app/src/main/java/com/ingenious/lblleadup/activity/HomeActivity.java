package com.ingenious.lblleadup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.fragments.EarningFragment;
import com.ingenious.lblleadup.fragments.MenuFragment;
import com.ingenious.lblleadup.fragments.MyNetworkFragment;
import com.ingenious.lblleadup.fragments.NotificationFragment;
import com.ingenious.lblleadup.fragments.SettingFragment;
import com.ingenious.lblleadup.models.Home;
import com.pixplicity.easyprefs.library.Prefs;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout layout_home, layout_wallet, layout_share, layout_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Binding Views */
        layout_home = findViewById(R.id.layout_home);
        layout_wallet = findViewById(R.id.layout_wallet);
        layout_share = findViewById(R.id.layout_share);
        layout_setting = findViewById(R.id.layout_setting);

        /* Checking Login */
        if (!Prefs.getBoolean("isLogin",false))
        {
            Prefs.remove("isLogin");
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

        /*Load Default app language */
        Utils.setApplicationlanguage(Prefs.getString("language","zh") , HomeActivity.this);

        /* Load Default Fragment  */
        Utils.loadFragment(HomeActivity.this,new MenuFragment());

        /* Click Listener  */
        layout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(HomeActivity.this,new MenuFragment());
            }
        });

        layout_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(HomeActivity.this,new EarningFragment());
            }
        });

        layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(HomeActivity.this,new MyNetworkFragment());
            }
        });

        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(HomeActivity.this,new SettingFragment());
            }
        });

        Log.d("user_id", Prefs.getString("user_id",""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Prefs.getBoolean("isLogin",false))
        {
            Prefs.remove("isLogin");
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
    }
}
