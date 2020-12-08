package com.ingenious.lblleadup.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponMainClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("stores")
@Expose
private List<Brands> stores = null;
@SerializedName("categories")
@Expose
private List<Category> categories = null;
@SerializedName("coupons")
@Expose
private List<Coupons> coupons = null;

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

public List<Brands> getStores() {
return stores;
}

public void setStores(List<Brands> stores) {
this.stores = stores;
}

public List<Category> getCategories() {
return categories;
}

public void setCategories(List<Category> categories) {
this.categories = categories;
}

public List<Coupons> getCoupons() {
return coupons;
}

public void setCoupons(List<Coupons> coupons) {
this.coupons = coupons;
}

}