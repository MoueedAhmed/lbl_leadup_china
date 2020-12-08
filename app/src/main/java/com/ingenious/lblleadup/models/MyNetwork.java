package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyNetwork {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("total_leads")
@Expose
private String totalLeads;

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

public String getTotalLeads() {
return totalLeads;
}

public void setTotalLeads(String totalLeads) {
this.totalLeads = totalLeads;
}

}