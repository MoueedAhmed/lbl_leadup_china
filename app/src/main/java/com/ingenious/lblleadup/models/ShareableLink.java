package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareableLink {

@SerializedName("link_id")
@Expose
private String linkId;
@SerializedName("link_title")
@Expose
private String linkTitle;
@SerializedName("link_url")
@Expose
private String linkUrl;

public String getLinkId() {
return linkId;
}

public void setLinkId(String linkId) {
this.linkId = linkId;
}

public String getLinkTitle() {
return linkTitle;
}

public void setLinkTitle(String linkTitle) {
this.linkTitle = linkTitle;
}

public String getLinkUrl() {
return linkUrl;
}

public void setLinkUrl(String linkUrl) {
this.linkUrl = linkUrl;
}

}