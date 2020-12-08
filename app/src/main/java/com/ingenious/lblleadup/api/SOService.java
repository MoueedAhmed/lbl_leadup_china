package com.ingenious.lblleadup.api;

import com.ingenious.lblleadup.models.Announcements;
import com.ingenious.lblleadup.models.Authorization;
import com.ingenious.lblleadup.models.CouponMainClass;
import com.ingenious.lblleadup.models.EReport;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.Home;
import com.ingenious.lblleadup.models.LeadSingleClass;
import com.ingenious.lblleadup.models.LinkClass;
import com.ingenious.lblleadup.models.Login;
import com.ingenious.lblleadup.models.MyIncome;
import com.ingenious.lblleadup.models.MyNetwork;
import com.ingenious.lblleadup.models.MyNetworkContactClass;
import com.ingenious.lblleadup.models.PlayApps;
import com.ingenious.lblleadup.models.ProductMainClass;
import com.ingenious.lblleadup.models.ShareClass;
import com.ingenious.lblleadup.models.VideoClass;
import com.ingenious.lblleadup.models.VideoSingleClass;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SOService {

    @Multipart
    @POST("get_token")
    Call<Authorization> getAuthentication(
            @Part("grant_type") RequestBody grant_type,
            @Part("client_id") RequestBody client_id,
            @Part("client_secret") RequestBody client_secret);

    @Multipart
    @POST("login")
    Call<Login> login(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("fcm") RequestBody fcm);

    @Multipart
    @POST("home")
    Call<Home> home(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("my_network")
    Call<MyNetwork> my_Network(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("my_income")
    Call<MyIncome> my_Income(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("load_announcements")
    Call<Announcements> announcements(@Part("user_id") RequestBody user_id,
                                      @Part("start") RequestBody start);

    @Multipart
    @POST("load_videos")
    Call<VideoClass> video(@Part("user_id") RequestBody user_id,
                           @Part("start") RequestBody start,
                           @Part("language") RequestBody language);

    @Multipart
    @POST("load_click_links")
    Call<LinkClass> links(@Part("user_id") RequestBody user_id,
                          @Part("start") RequestBody start,
                          @Part("language") RequestBody language);

    @Multipart
    @POST("load_shareable_links")
    Call<ShareClass> shareLink(@Part("user_id") RequestBody user_id,
                           @Part("start") RequestBody start,
                           @Part("language") RequestBody language);

    @Multipart
    @POST("link_add_to_click")
    Call<GlobalResponse> click_on_link(@Part("user_id") RequestBody user_id,
                                   @Part("click_link_id") RequestBody click_link_id);

    @Multipart
    @POST("shareable_add_to_list")
    Call<GlobalResponse> click_on_share_earn_link(@Part("user_id") RequestBody user_id,
                                       @Part("link_id") RequestBody link_id);

    @Multipart
    @POST("update_lead")
    Call<GlobalResponse> add_my_network_lead(
            @Part("user_id") RequestBody user_id,
            @Part("lead_id") RequestBody lead_id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("category") RequestBody category,
            @Part("phone") RequestBody phone,
            @Part("whatsapp") RequestBody whatsapp);

    @Multipart
    @POST("load_category_leads")
    Call<MyNetworkContactClass> myNetworkContactsCategoryWise(
            @Part("user_id") RequestBody user_id,
            @Part("category") RequestBody category,
            @Part("start") RequestBody start);
    
    @POST("all_coupans")
    Call<CouponMainClass> couponHome();

    @Multipart
    @POST("store_list")
    Call<CouponMainClass> getBrands(@Part("start") RequestBody start);

    @Multipart
    @POST("category_list")
    Call<CouponMainClass> getCategory(@Part("start") RequestBody start);

    @Multipart
    @POST("latest_coupans")
    Call<CouponMainClass> latest_coupans(@Part("start") RequestBody start);

    @Multipart
    @POST("store_coupans")
    Call<CouponMainClass> coupon_by_brand(
            @Part("store_id") RequestBody store_id,
            @Part("start") RequestBody start
    );

    @Multipart
    @POST("category_coupans")
    Call<CouponMainClass> coupon_by_category(
            @Part("category_id") RequestBody category_id,
            @Part("start") RequestBody start
    );

    @POST("all_products")
    Call<ProductMainClass> productHome();

    @Multipart
    @POST("latest_products")
    Call<ProductMainClass> latest_products(@Part("start") RequestBody start);

    @Multipart
    @POST("product_category_list")
    Call<ProductMainClass> product_category_list(@Part("start") RequestBody start);

    @Multipart
    @POST("category_products")
    Call<ProductMainClass> category_products(
            @Part("start") RequestBody start,
            @Part("category_id") RequestBody category_id
    );

    @Multipart
    @POST("send_contact_message")
    Call<GlobalResponse> contact_us(
            @Part("user_id") RequestBody user_id,
            @Part("device_info") RequestBody name,
            @Part("department") RequestBody department,
            @Part("message") RequestBody message,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video);

    @Multipart
    @POST("remove_lead")
    Call<GlobalResponse> remove_leads(
            @Part("user_id") RequestBody user_id,
            @Part("lead_id") RequestBody lead_id);

    @Multipart
    @POST("load_single_video")
    Call<VideoSingleClass> single_video(
            @Part("user_id") RequestBody user_id,
            @Part("video_id") RequestBody video_id);

    @Multipart
    @POST("load_single_lead")
    Call<LeadSingleClass> lead_detail(
            @Part("user_id") RequestBody user_id,
            @Part("lead_id") RequestBody lead_id);

    @Multipart
    @POST("send_fund_request")
    Call<GlobalResponse> fund_transfer(
            @Part("user_id") RequestBody user_id,
            @Part("request_ammount") RequestBody request_ammount,
            @Part("payment_method") RequestBody payment_method,
            @Part("account_details") RequestBody account_details);

    @Multipart
    @POST("video_add_to_watch")
    Call<GlobalResponse> video_add_to_watch(
            @Part("user_id") RequestBody user_id,
            @Part("video_id") RequestBody video_id,
            @Part("watch_duration") RequestBody watch_duration);

    @Multipart
    @POST("generate_ereport")
    Call<EReport> eReport(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("vendor_products")
    Call<ProductMainClass> vendor_products(
            @Part("start") RequestBody start,
            @Part("vendor_id") RequestBody vendor_id
    );

    @Multipart
    @POST("update_profile_image")
    Call<GlobalResponse> update_profile_image(
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part image);

    @POST("privacy_policy")
    Call<GlobalResponse> privacy_policy();

    @Multipart
    @POST("load_apps")
    Call<PlayApps> loadApps(
            @Part("user_id") RequestBody user_id,
            @Part("start") RequestBody start,
            @Part("language") RequestBody language
    );

    @POST("terms_and_conditions")
    Call<GlobalResponse> terms_and_conditions();

    @Multipart
    @POST("forgetPassword")
    Call<GlobalResponse> forgot_password(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("language") RequestBody language
    );

    @Multipart
    @POST("changePassword")
    Call<GlobalResponse> changePassword(
            @Part("user_id") RequestBody user_id,
            @Part("password") RequestBody password,
            @Part("language") RequestBody language
    );
}