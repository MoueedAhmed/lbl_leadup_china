package com.ingenious.lblleadup.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("click_links")
@Expose
private List<ClickLink> clickLinks = null;

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

public List<ClickLink> getClickLinks() {
return clickLinks;
}

public void setClickLinks(List<ClickLink> clickLinks) {
this.clickLinks = clickLinks;
}

}