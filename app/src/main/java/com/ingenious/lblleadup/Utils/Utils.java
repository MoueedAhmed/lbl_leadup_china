package com.ingenious.lblleadup.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.Locale;
import java.util.concurrent.Callable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils
{
    public static boolean isOnline(Context ctx) {

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void openPlayStore(Context context, String pkgName)
    {
        // Bring user to the market or let them choose an app?
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("market://details?id=" + pkgName));
        context.startActivity(intent);
    }

    public static void openGmail(Context context, String to, String subject , String message)
    {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        if (intent != null)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            // The intent does not have a URI, so declare the "text/plain" MIME type
            emailIntent.setType("text/plain");
            emailIntent.setPackage("com.google.android.gm");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {to}); // recipients
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(emailIntent);
        }
        else
        {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.google.android.gm"));
            context.startActivity(intent);

            //Show Error message
        }
    }

    public static void openWeChat(Context context,String message)
    {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        if (intent != null)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            // The intent does not have a URI, so declare the "text/plain" MIME type
            emailIntent.setType("text/plain");
            emailIntent.setPackage("com.tencent.mm");
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(emailIntent);
        }
        else
        {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.tencent.mm"));
            context.startActivity(intent);

            //Show Error message
        }
    }

    public static void openFacebook(Context context , String text) {

        //Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // change with required  application package

        intent.setPackage("com.facebook.katana");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            context.startActivity(Intent.createChooser(intent, text));
        } else
        {
            Toast.makeText(context, R.string.app_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openTwitter(Context context , String url , String msg)
    {
        String text = "http://www.twitter.com/intent/tweet?url="+url+"&text="+msg;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(text));
        context.startActivity(i);
    }

    public static void openWhatsApp(Context context, String msg)
    {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        if (intent != null)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        }
        else
        {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.whatsapp"));
            context.startActivity(intent);

            //Show Error message
        }
    }

    public static void openAndSendWhatsApp(Context context, String no, String msg)
    {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        if (intent != null)
        {
            PackageManager pm = context.getPackageManager();
            no = no.replace("+", "").replace(" ", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.putExtra("jid", no + "@s.whatsapp.net");
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
            try
            {
                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
        {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.whatsapp"));
            context.startActivity(intent);

            //Show Error message
        }
    }

    public static void loadFragment(Context context, Fragment fragment)
    {
        // Create new fragment and transaction
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public static void openCloseFragment(Context context, Fragment removeFragment , Fragment fragment)
    {
        // Create new fragment and transaction
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().remove(removeFragment).commit();
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack

        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public static void loadFragmentWithMultipleData(Context context, Fragment fragment, Bundle bundle)
    {
        fragment.setArguments(bundle);

        // Create new fragment and transaction
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public static void loadFragmentWithData(Context context, Fragment fragment, String key, String value)
    {
        Bundle arguments = new Bundle();
        arguments.putString(key, value);
        fragment.setArguments(arguments);

        // Create new fragment and transaction
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public static String getAndroidID(Context context)
    {
        return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void hideStatusBar(Activity activity)
    {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void setApplicationlanguage(String language, Context context)
    {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);
    }

//    public static void choose_language_dialog(final Activity activity, String title, String message)
//    {
//        new FancyAlertDialog.Builder(activity)
//                .setTitle(title)
//                .setBackgroundColor(Color.parseColor("#03A678"))  //Don't pass R.color.colorvalue
//                .setMessage(message)
//                .setNegativeBtnText("English")
//                .setPositiveBtnBackground(Color.parseColor("#03A678"))  //Don't pass R.color.colorvalue
//                .setPositiveBtnText("اردو")
//                .setNegativeBtnBackground(Color.parseColor("#212121"))  //Don't pass R.color.colorvalue
//                .setAnimation(Animation.SLIDE)
//                .isCancellable(true)
//                .setIcon(R.drawable.language, Icon.Visible)
//                .OnPositiveClicked(new FancyAlertDialogListener() {
//                    @Override
//                    public void OnClick() {
//                        Utils.setApplicationlanguage("ur",activity);
//                        activity.startActivity(new Intent(activity, HomeActivity.class));
//                        com.ingenious.mysouqpk.api.Animation.fade(activity);
//                        activity.finish();
//                    }
//                })
//                .OnNegativeClicked(new FancyAlertDialogListener() {
//                    @Override
//                    public void OnClick() {
//                        Utils.setApplicationlanguage("en",activity);
//                        activity.startActivity(new Intent(activity, HomeActivity.class));
//                        com.ingenious.mysouqpk.api.Animation.fade(activity);
//                        activity.finish();
//                    }
//                })
//                .build();
//    }

    public static void success_dialog(final Activity activity, String title, String message, final Callable<Void> methodParam)
    {
        new FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setBackgroundColor(Color.parseColor("#2ecc71"))  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#2ecc71"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.SIDE)
                .isCancellable(false)
                .setIcon(R.drawable.success, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        try
                        {
                            methodParam.call();

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }

    public static void error_dialog(final Activity activity, String title, String message, final Callable<Void> methodParam)
    {
        new FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setBackgroundColor(Color.parseColor("#e74c3c"))  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#e74c3c"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.SIDE)
                .isCancellable(false)
                .setIcon(R.drawable.error, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        try
                        {
                            methodParam.call();

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }

    public static void info_dialog(final Activity activity, String message, final Callable<Void> methodParam)
    {
        new FancyAlertDialog.Builder(activity)
                .setTitle("Info")
                .setBackgroundColor(Color.parseColor("#FF6347"))  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#FF6347"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.SIDE)
                .isCancellable(false)
                .setIcon(R.drawable.info, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        try
                        {
                            methodParam.call();

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }

    public static void applyStrike(TextView textView)
    {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static boolean isValidEmail(String email)
    {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static RequestBody getSimpleTextBody(String param)
    {
        return RequestBody.create(MediaType.parse("text/plain"),param);
    }
}