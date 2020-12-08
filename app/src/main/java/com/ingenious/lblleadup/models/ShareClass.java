package com.ingenious.lblleadup.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("shareable_links")
@Expose
private List<ShareableLink> shareableLinks = null;

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

public List<ShareableLink> getShareableLinks() {
return shareableLinks;
}

public void setShareableLinks(List<ShareableLink> shareableLinks) {
this.shareableLinks = shareableLinks;
}

}