package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoSingleClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("video_id")
@Expose
private String videoId;
@SerializedName("video_duration")
@Expose
private String videoDuration;
@SerializedName("video_url")
@Expose
private String videoUrl;

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

public String getVideoId() {
return videoId;
}

public void setVideoId(String videoId) {
this.videoId = videoId;
}

public String getVideoDuration() {
return videoDuration;
}

public void setVideoDuration(String videoDuration) {
this.videoDuration = videoDuration;
}

public String getVideoUrl() {
return videoUrl;
}

public void setVideoUrl(String videoUrl) {
this.videoUrl = videoUrl;
}

}