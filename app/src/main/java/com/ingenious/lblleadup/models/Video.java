package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

@SerializedName("video_id")
@Expose
private String videoId;
@SerializedName("video_thumbnail")
@Expose
private String videoThumbnail;
@SerializedName("video_title")
@Expose
private String videoTitle;
@SerializedName("video_language")
@Expose
private String videoLanguage;

public String getVideoId() {
return videoId;
}

public void setVideoId(String videoId) {
this.videoId = videoId;
}

public String getVideoThumbnail() {
return videoThumbnail;
}

public void setVideoThumbnail(String videoThumbnail) {
this.videoThumbnail = videoThumbnail;
}

public String getVideoTitle() {
return videoTitle;
}

public void setVideoTitle(String videoTitle) {
this.videoTitle = videoTitle;
}

public String getVideoLanguage() {
return videoLanguage;
}

public void setVideoLanguage(String videoLanguage) {
this.videoLanguage = videoLanguage;
}

}