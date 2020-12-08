package com.ingenious.lblleadup.api;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import com.bumptech.glide.Glide;
import com.ingenious.lblleadup.Utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

public class MainClass extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}