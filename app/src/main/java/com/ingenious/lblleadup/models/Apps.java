package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Apps
{
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("app_icon")
    @Expose
    private String appIcon;
    @SerializedName("app_title")
    @Expose
    private String appTitle;
    @SerializedName("app_url")
    @Expose
    private String appUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
