package com.ingenious.lblleadup.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Animation;
import com.ingenious.lblleadup.Utils.Constant;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.HomeActivity;
import com.ingenious.lblleadup.activity.LoginActivity;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.Home;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private LinearLayout layout_earning_dashboard, layout_my_leads, layout_fund_transfer, layout_coupons_discount;
    private TextView tv_name, tv_all_time_Earning, tv_this_month_earning, tv_token;
    private ImageView img_avatar, img_notification, img_edit_pic, img_copy;
    private CustomProgressDialogue dialogue;
    private String photoPath="";

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        layout_earning_dashboard = v.findViewById(R.id.layout_earning_dashboard);
        layout_my_leads = v.findViewById(R.id.layout_my_leads);
        layout_fund_transfer = v.findViewById(R.id.layout_fund_transfer);
        layout_coupons_discount = v.findViewById(R.id.layout_coupons_discount);

        tv_name = v.findViewById(R.id.tv_name);
        tv_all_time_Earning = v.findViewById(R.id.tv_all_time_Earning);
        tv_this_month_earning = v.findViewById(R.id.tv_this_month_earning);
        tv_token = v.findViewById(R.id.tv_token);
        img_avatar = v.findViewById(R.id.img_avatar);
        img_notification = v.findViewById(R.id.img_notification);
        img_edit_pic = v.findViewById(R.id.img_edit_pic);
        img_copy = v.findViewById(R.id.img_copy);

        if (Utils.isOnline(getContext()))
        {
            home();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new NotificationFragment());
            }
        });

        layout_earning_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new EarningDashboardFragment());
            }
        });

        layout_fund_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new FundTransferFragment());
            }
        });


        layout_my_leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.learnbuildlead.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });

        layout_coupons_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new CouponFragment());
            }
        });

        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MenuFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        img_edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MenuFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("user_token", tv_token.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            photoPath = ImagePicker.Companion.getFilePath(data);
            Bitmap bmImg = BitmapFactory.decodeFile(photoPath);
            img_avatar.setImageBitmap(bmImg);
            update_profile_image();
        }
    }

    private void update_profile_image()
    {
        dialogue.show();

        File photofile = null;
        if (!photoPath.isEmpty() && photoPath != null) {
            photofile  = new File(photoPath);
        }

        // Parsing any Media type file
        final RequestBody requestBodyImage;
        MultipartBody.Part body_Iamge = null;

        if(photofile != null){
            requestBodyImage = RequestBody.create(MediaType.parse("*/*"), photofile);
            body_Iamge = MultipartBody.Part.createFormData("image", photofile.getName(), requestBodyImage);
        }

        SOService soService = ApiUtils.getSOService();
        soService.update_profile_image(Utils.getSimpleTextBody(Prefs.getString("user_id","")),body_Iamge).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body().getSuccess())
                    {
                        Toasty.success(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void home()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.home(Utils.getSimpleTextBody(Prefs.getString("user_id",""))).enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.isSuccessful())
                {
                    Log.d("HomeFragment" , response+"");
                    dialogue.cancel();
                    if (response.body() != null && response.body().getSuccess())
                    {
                        if (response.body().getLoginFlag())
                        {
                            Prefs.remove("isLogin");
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            Animation.slideLeft(getContext());
                        }
                        else
                        {
                            Glide.with(getContext())
                                    .load(response.body().getProfileImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .placeholder(R.drawable.avatar)
                                    .into(img_avatar);
                            tv_name.setText(response.body().getName());
                            tv_all_time_Earning.setText(response.body().getAllTimeEarning());
                            tv_this_month_earning.setText(response.body().getThisMonthEarning());
                            tv_token.setText(response.body().getUserToken());
                            Constant.Currency = "GBP";
                        }
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                dialogue.cancel();
                Log.d("HomeFragment" , t.getMessage()+"");
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

}
