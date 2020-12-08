package com.ingenious.lblleadup.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyNetworkContactClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("leads")
@Expose
private List<Lead> leads = null;

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

public List<Lead> getLeads() {
return leads;
}

public void setLeads(List<Lead> leads) {
this.leads = leads;
}

}