package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyIncome {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("network_earning")
    @Expose
    private String networkEarning;
    @SerializedName("click_earning")
    @Expose
    private String clickEarning;
    @SerializedName("watch_earning")
    @Expose
    private String watchEarning;
    @SerializedName("share_earning")
    @Expose
    private String shareEarning;
    @SerializedName("lifetime_earning")
    @Expose
    private String lifetimeEarning;
    @SerializedName("30day_earning")
    @Expose
    private String _30dayEarning;
    @SerializedName("today_earning")
    @Expose
    private String todayEarning;
    @SerializedName("app_earning")
    @Expose
    private String appEarning;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNetworkEarning() {
        return networkEarning;
    }

    public void setNetworkEarning(String networkEarning) {
        this.networkEarning = networkEarning;
    }

    public String getClickEarning() {
        return clickEarning;
    }

    public void setClickEarning(String clickEarning) {
        this.clickEarning = clickEarning;
    }

    public String getWatchEarning() {
        return watchEarning;
    }

    public void setWatchEarning(String watchEarning) {
        this.watchEarning = watchEarning;
    }

    public String getShareEarning() {
        return shareEarning;
    }

    public void setShareEarning(String shareEarning) {
        this.shareEarning = shareEarning;
    }

    public String getLifetimeEarning() {
        return lifetimeEarning;
    }

    public void setLifetimeEarning(String lifetimeEarning) {
        this.lifetimeEarning = lifetimeEarning;
    }

    public String get30dayEarning() {
        return _30dayEarning;
    }

    public void set30dayEarning(String _30dayEarning) {
        this._30dayEarning = _30dayEarning;
    }

    public String getTodayEarning() {
        return todayEarning;
    }

    public void setTodayEarning(String todayEarning) {
        this.todayEarning = todayEarning;
    }

    public String getAppEarning() {
        return appEarning;
    }

    public void setAppEarning(String appEarning) {
        this.appEarning = appEarning;
    }

}