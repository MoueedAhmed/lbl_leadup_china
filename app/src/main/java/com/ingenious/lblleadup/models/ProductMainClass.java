package com.ingenious.lblleadup.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProductMainClass {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("message")
@Expose
private String message;
@SerializedName("vendors")
@Expose
private List<Vendor> vendors = null;
@SerializedName("product_categories")
@Expose
private List<ProductCategory> productCategories = null;
@SerializedName("products")
@Expose
private List<Product> products = null;

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

public List<Vendor> getVendors() {
return vendors;
}

public void setVendors(List<Vendor> vendors) {
this.vendors = vendors;
}

public List<ProductCategory> getProductCategories() {
return productCategories;
}

public void setProductCategories(List<ProductCategory> productCategories) {
this.productCategories = productCategories;
}

public List<Product> getProducts() {
return products;
}

public void setProducts(List<Product> products) {
this.products = products;
}

}