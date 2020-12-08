package com.ingenious.lblleadup.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.EReport;
import com.ingenious.lblleadup.models.MyIncome;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private TextView tv_earning_from_network, tv_earning_from_click_earn, tv_earning_from_watch_earn, tv_earning_lifetime, tv_earning_last_30_days, tv_earning_today, tv_earning_from_share_earn, tv_earning_from_application;
    private CustomProgressDialogue dialogue;
    private Button btn_fundTransfer, btn_eReport;
    private String file_url = "";
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public EarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_earning, container, false);

        tv_earning_from_network = v.findViewById(R.id.tv_earning_from_network);
        tv_earning_from_click_earn = v.findViewById(R.id.tv_earning_from_click_earn);
        tv_earning_from_watch_earn = v.findViewById(R.id.tv_earning_from_watch_earn);
        tv_earning_lifetime = v.findViewById(R.id.tv_earning_lifetime);
        tv_earning_last_30_days = v.findViewById(R.id.tv_earning_last_30_days);
        tv_earning_from_share_earn = v.findViewById(R.id.tv_earning_from_share_earn);
        tv_earning_from_application = v.findViewById(R.id.tv_earning_from_application);
        tv_earning_today = v.findViewById(R.id.tv_earning_today);
        btn_fundTransfer = v.findViewById(R.id.btn_fundTransfer);
        btn_eReport = v.findViewById(R.id.btn_eReport);

        if (Utils.isOnline(getContext()))
        {
            myIncome();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }


        btn_fundTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.loadFragment(getContext(),new FundTransferFragment());
            }
        });

        btn_eReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline(getContext()))
                {
                    generate_eReport();
                }
                else
                {
                    Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    private void myIncome()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.my_Income(Utils.getSimpleTextBody(Prefs.getString("user_id",""))).enqueue(new Callback<MyIncome>() {
            @Override
            public void onResponse(Call<MyIncome> call, Response<MyIncome> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body().getSuccess())
                    {
                        tv_earning_from_network.setText(response.body().getNetworkEarning());
                        tv_earning_from_click_earn.setText(response.body().getClickEarning());
                        tv_earning_from_watch_earn.setText(response.body().getWatchEarning());
                        tv_earning_from_share_earn.setText(response.body().getShareEarning());
                        tv_earning_from_application.setText(response.body().getAppEarning());
                        tv_earning_lifetime.setText(response.body().getLifetimeEarning());
                        tv_earning_last_30_days.setText(response.body().get30dayEarning());
                        tv_earning_today.setText(response.body().getTodayEarning());

                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyIncome> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void generate_eReport()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.eReport(Utils.getSimpleTextBody(Prefs.getString("user_id",""))).enqueue(new Callback<EReport>() {
            @Override
            public void onResponse(Call<EReport> call, Response<EReport> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body() != null &&response.body().getSuccess())
                    {
                        file_url = response.body().getReportUrl();
                        startDownloading();
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EReport> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    @AfterPermissionGranted(123)
    private void startDownloading()
    {
        if (EasyPermissions.hasPermissions(getContext(), perms))
        {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(file_url));
            request.setTitle("Download");
            request.setDescription("Downloading eReport");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, ""+System.currentTimeMillis()); //Set Current time for file name

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) Objects.requireNonNull(getContext()).getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
        else
        {
            EasyPermissions.requestPermissions(this, "We need permissions", 123, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list)
    {
        // Some permissions have been granted
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) getContext(), list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
