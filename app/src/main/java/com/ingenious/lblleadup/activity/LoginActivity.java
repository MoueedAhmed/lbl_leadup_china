package com.ingenious.lblleadup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Animation;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.Login;
import com.pixplicity.easyprefs.library.Prefs;

import org.w3c.dom.Text;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ImageView img_top, img_logo;
    private EditText et_email, et_pass;
    private Button btn_login, btn_sign_up;
    private CustomProgressDialogue dialogue;
    private String FCM_Token = "I AM FROM CHINA";
    private TextView tv_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Initialize Loading dialog  **********/
        dialogue = new CustomProgressDialogue(this);

        /* Binding Views **********/
        img_top  = findViewById(R.id.img_top);
        img_logo = findViewById(R.id.img_logo);
        et_email = findViewById(R.id.et_user_id);
        et_pass  = findViewById(R.id.et_pass);
        btn_login  = findViewById(R.id.btn_login);
        btn_sign_up  = findViewById(R.id.btn_sign_up);
        tv_forget  = findViewById(R.id.tv_forget);

        /* loading image **********/
        Glide.with(this).load(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.NONE).into(img_logo);

        /* login listener **********/
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline(LoginActivity.this))
                {
                    if (TextUtils.isEmpty(et_email.getText().toString()))
                    {
                        et_email.setError("Required");
                        et_email.requestFocus();
                    }
                    else if (TextUtils.isEmpty(et_pass.getText().toString()))
                    {
                        et_pass.setError("Required");
                        et_pass.requestFocus();
                    }
                    else
                    {
                        login();
                    }
                }
                else
                {
                    Toasty.error(LoginActivity.this,R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.learnbuildlead.com/lbl/LeadUpSignup"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPasswordDialog();
            }
        });
    }

    private void login()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.login(Utils.getSimpleTextBody(et_email.getText().toString()),
                Utils.getSimpleTextBody(et_pass.getText().toString()),
                Utils.getSimpleTextBody(FCM_Token)).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response)
            {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body() != null && response.body().getSuccess())
                    {
                        Prefs.putBoolean("isLogin",true);
                        Prefs.putString("user_id",response.body().getUserId());
                        Toasty.success(LoginActivity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        Animation.slideLeft(LoginActivity.this);
                        finish();
                    }
                    else
                    {
                        Toasty.error(LoginActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    dialogue.cancel();
                    Toasty.error(LoginActivity.this,R.string.error_on_request_failed, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialogue.cancel();
                Log.e("LoginActivityError" , t.getMessage()+"");
                startActivity(new Intent(LoginActivity.this, ServerErrorActivity.class));
            }
        });
    }

    private void showForgetPasswordDialog()
    {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_forget_password);
        dialog.setCancelable(true);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(dialog.getWindow()).setLayout(width,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_user_id = dialog.findViewById(R.id.et_user_id);
        final EditText et_email = dialog.findViewById(R.id.et_email);
        final Button btn_send = dialog.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget_pass_request(et_user_id.getText().toString(), et_email.getText().toString(),dialog);
            }
        });
        dialog.show();
    }

    private void forget_pass_request(String username, String email, final Dialog dialog)
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.forgot_password(Utils.getSimpleTextBody(username),
                Utils.getSimpleTextBody(email),
                Utils.getSimpleTextBody(Prefs.getString("language","en"))).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body() != null && response.body().getSuccess())
                    {
                        Toasty.success(LoginActivity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toasty.error(LoginActivity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                dialogue.cancel();
                Log.e("LoginActivityError" , t.getMessage()+"");
                startActivity(new Intent(LoginActivity.this, ServerErrorActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        moveTaskToBack(true);
        Animation.slideRight(LoginActivity.this);
    }

}
