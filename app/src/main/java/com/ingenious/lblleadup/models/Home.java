package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Home {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("all_time_earning")
    @Expose
    private String allTimeEarning;
    @SerializedName("this_month_earning")
    @Expose
    private String thisMonthEarning;
    @SerializedName("login_flag")
    @Expose
    private Boolean loginFlag;
    @SerializedName("user_token")
    @Expose
    private String userToken;

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllTimeEarning() {
        return allTimeEarning;
    }

    public void setAllTimeEarning(String allTimeEarning) {
        this.allTimeEarning = allTimeEarning;
    }

    public String getThisMonthEarning() {
        return thisMonthEarning;
    }

    public void setThisMonthEarning(String thisMonthEarning) {
        this.thisMonthEarning = thisMonthEarning;
    }

    public Boolean getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(Boolean loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}