package com.ingenious.lblleadup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authorization {

@SerializedName("access_token")
@Expose
private String accessToken;
@SerializedName("expires_in")
@Expose
private Integer expiresIn;
@SerializedName("token_type")
@Expose
private String tokenType;
@SerializedName("scope")
@Expose
private Object scope;

public String getAccessToken() {
return accessToken;
}

public void setAccessToken(String accessToken) {
this.accessToken = accessToken;
}

public Integer getExpiresIn() {
return expiresIn;
}

public void setExpiresIn(Integer expiresIn) {
this.expiresIn = expiresIn;
}

public String getTokenType() {
return tokenType;
}

public void setTokenType(String tokenType) {
this.tokenType = tokenType;
}

public Object getScope() {
return scope;
}

public void setScope(Object scope) {
this.scope = scope;
}

}