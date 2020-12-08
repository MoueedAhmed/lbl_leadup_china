package com.ingenious.lblleadup.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.ingenious.lblleadup.BuildConfig;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.LoginActivity;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    private EditText et_message;
    private Spinner spinner_department;
    private Button btn_send;
    private String[] department = {"Support" , "Technical" , "Sales"};
    private ImageView img_image_picker, img_video_picker;
    private CustomProgressDialogue dialogue;
    private String video_path = "",  photoPath="";
    private int FILE_REQUEST_CODE = 1001;
    private long video_size_in_MB = 0;
    private String TAG = "ContactUsFragment";

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment'
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        et_message = v.findViewById(R.id.et_message);
        spinner_department = v.findViewById(R.id.spinner_department);
        btn_send = v.findViewById(R.id.btn_send);
        img_image_picker = v.findViewById(R.id.img_image_picker);
        img_video_picker = v.findViewById(R.id.img_video_picker);

        spinner_department.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_dropdown_item,department));

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline(Objects.requireNonNull(getContext())))
                {

                    if (TextUtils.isEmpty(et_message.getText().toString()))
                    {
                        et_message.setError("Required");
                        et_message.requestFocus();
                    }
                    else
                    {
                        contact_us();
                    }

                }
                else
                {
                    Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });


        /*Image Picker*/
        img_image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ImagePicker.Companion.with(ContactUsFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        img_video_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext() , FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .setShowVideos(true)
                        .setMaxSelection(1)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });

        /*Device information*/
        Log.i(TAG,"DEVCIE_DETAIL "+ getDeviceDetail());

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == FILE_REQUEST_CODE)
            {
                ArrayList<MediaFile> files= data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                if (files.isEmpty())
                {
                    Toasty.error(getContext(),"No Video Selected", Toast.LENGTH_LONG).show();
                }
                else
                {
                    video_size_in_MB =  files.get(0).getSize() / 1024 / 1024; // Size in Byte -> KB -> MB
                    if (video_size_in_MB < 15)
                    {
                        video_path = files.get(0).getPath();

                        // MINI_KIND, size: 512 x 384 thumbnail
                        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.MINI_KIND);
                        img_video_picker.setImageBitmap(bmThumbnail);
                    }
                    else
                    {
                        Toasty.error(getContext(),"Please choose less then 15 mb", Toast.LENGTH_LONG).show();
                    }
                }
            }
            else
            {
                photoPath = ImagePicker.Companion.getFilePath(data);
                Bitmap bmImg = BitmapFactory.decodeFile(photoPath);
                img_image_picker.setImageBitmap(bmImg);
            }

        }
    }

    private void contact_us()
    {
        dialogue.show();

        File photofile = null;
        File videofile = null;

        if (!photoPath.isEmpty() && photoPath != null) {
            photofile  = new File(photoPath);
        }

        if (!video_path.isEmpty() && video_path != null) {
            videofile = new File(video_path);
        }

        // Parsing any Media type file
        RequestBody requestBodyImage;
        RequestBody requestBodyVideo;

        MultipartBody.Part body_Iamge = null;
        MultipartBody.Part body_Video = null;

        if(photofile != null){
            requestBodyImage = RequestBody.create(MediaType.parse("*/*"), photofile);
            body_Iamge = MultipartBody.Part.createFormData("image", photofile.getName(), requestBodyImage);
        }

        if(videofile != null){
            requestBodyVideo = RequestBody.create(MediaType.parse("*/*"), videofile);
            body_Video = MultipartBody.Part.createFormData("video", videofile.getName(), requestBodyVideo);
        }

        SOService soService = ApiUtils.getSOService();
        soService.contact_us(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(getDeviceDetail()),
                Utils.getSimpleTextBody(spinner_department.getSelectedItem().toString()),
                Utils.getSimpleTextBody(et_message.getText().toString()),body_Iamge,body_Video).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response)
            {
                dialogue.cancel();
                Log.d("ContactResponse",response+"");
                if (response.isSuccessful())
                {
                    if (response.body() != null && response.body().getSuccess())
                    {
                        Toasty.success(Objects.requireNonNull(getContext()),response.body().getMessage(), Toast.LENGTH_LONG).show();
                        clearAllViews();
                    }
                    else
                    {
                        Toasty.error(Objects.requireNonNull(getContext()), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                Log.d("ContactFailure" , t.getMessage()+"");
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private String getDeviceDetail()
    {
        String  details =
                "\nDEVICE_OS : "+Build.VERSION.RELEASE
                +"\nBRAND : "+Build.BRAND
                +"\nHARDWARE : "+Build.HARDWARE
                +"\nHOST : "+Build.HOST
                +"\nID : "+Build.ID
                +"\nMANUFACTURER : "+Build.MANUFACTURER
                +"\nMODEL : "+Build.MODEL
                +"\nAPP_VERSION : "+BuildConfig.VERSION_NAME;
        return details;
    }

    private void clearAllViews()
    {
        et_message.setText("");
        Glide.with(getContext()).load(R.drawable.add_images).into(img_image_picker);
        Glide.with(getContext()).load(R.drawable.video_camera).into(img_video_picker);
    }
}
