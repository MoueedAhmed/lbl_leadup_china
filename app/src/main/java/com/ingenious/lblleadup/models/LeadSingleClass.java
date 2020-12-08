package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeadSingleClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("lead_id")
@Expose
private String leadId;
@SerializedName("name")
@Expose
private String name;
@SerializedName("email")
@Expose
private String email;
@SerializedName("category")
@Expose
private String category;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("whatsapp")
@Expose
private String whatsapp;

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

public String getLeadId() {
return leadId;
}

public void setLeadId(String leadId) {
this.leadId = leadId;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public String getWhatsapp() {
return whatsapp;
}

public void setWhatsapp(String whatsapp) {
this.whatsapp = whatsapp;
}

}