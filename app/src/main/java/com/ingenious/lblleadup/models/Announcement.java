package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Announcement {

@SerializedName("announcement_id")
@Expose
private String announcementId;
@SerializedName("announcement_title")
@Expose
private String announcementTitle;
@SerializedName("announcement_description")
@Expose
private String announcementDescription;
@SerializedName("announcement_date")
@Expose
private String announcementDate;

public String getAnnouncementId() {
return announcementId;
}

public void setAnnouncementId(String announcementId) {
this.announcementId = announcementId;
}

public String getAnnouncementTitle() {
return announcementTitle;
}

public void setAnnouncementTitle(String announcementTitle) {
this.announcementTitle = announcementTitle;
}

public String getAnnouncementDescription() {
return announcementDescription;
}

public void setAnnouncementDescription(String announcementDescription) {
this.announcementDescription = announcementDescription;
}

public String getAnnouncementDate() {
return announcementDate;
}

public void setAnnouncementDate(String announcementDate) {
this.announcementDate = announcementDate;
}

}