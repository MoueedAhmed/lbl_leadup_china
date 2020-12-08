package com.ingenious.lblleadup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.FullScreenHelper;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.VideoSingleClass;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WatchVideoActivity extends AppCompatActivity {

    private YouTubePlayerView youtube_player_view;
    private long totalSeconds;
    long activity_start_time = 0 , activity_end_time = 0, total_spend_time_on_activity = 0, total_spend_time_on_activity_second = 0 , video_duration_in_long = 0;
    private String videoId = "" , video_URL = "" , video_duration;
    private CustomProgressDialogue dialogue;
    private boolean is_watch = false;
    private View decorView;
    private FullScreenHelper fullScreenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        decorView = getWindow().getDecorView();
        fullScreenHelper = new FullScreenHelper(WatchVideoActivity.this,decorView);

        dialogue = new CustomProgressDialogue(WatchVideoActivity.this);

        youtube_player_view = findViewById(R.id.youtube_player_view);

        if (Utils.isOnline(WatchVideoActivity.this))
        {
            loadVideoDetail();
        }
        else
        {
            Toasty.error(WatchVideoActivity.this,R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        /* player listener **********/
        youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer)
            {
                youTubePlayer.loadVideo(video_URL, 0);
                activity_start_time = System.currentTimeMillis();
                Log.d("Activity_time_start" , activity_start_time+"");
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                String v_state = state+"";
                if (v_state.equals("ENDED"))
                {
                    if (activity_start_time != 0)
                    {
                        activity_end_time = System.currentTimeMillis();
                        total_spend_time_on_activity = activity_end_time - activity_start_time;
                        video_duration_in_long = Long.parseLong(video_duration);
                        total_spend_time_on_activity_second = TimeUnit.MILLISECONDS.toSeconds(total_spend_time_on_activity);

                        Log.d("Video_time", totalSeconds+"");
                        Log.d("activity_start_time", activity_start_time+"");
                        Log.d("activity_end_time", activity_end_time+"");
                        Log.d("TimeSpendOnActivty" , total_spend_time_on_activity_second+"");
                        Log.d("VideoDurationInLong" , video_duration_in_long+"");

                        if (total_spend_time_on_activity_second >= video_duration_in_long)
                        {
                            if (!is_watch)
                            {
                                video_watch();
                            }
                        }
                    }
                }
            }
            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                totalSeconds = Math.round(second);
            }
        });

        youtube_player_view.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                fullScreenHelper.hideSystemUi(decorView);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                fullScreenHelper.showSystemUi(decorView);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });



    }

    private void loadVideoDetail()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.single_video(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(getIntent().getStringExtra("id"))).enqueue(new Callback<VideoSingleClass>() {
            @Override
            public void onResponse(Call<VideoSingleClass> call, Response<VideoSingleClass> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body() != null && response.body().getSuccess())
                    {
                        videoId = response.body().getVideoId();
                        video_URL = response.body().getVideoUrl();
                        video_duration = response.body().getVideoDuration();
                        Log.d("video_duration",video_duration);
                    }
                    else
                    {
                        Toasty.error(WatchVideoActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoSingleClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(WatchVideoActivity.this, ServerErrorActivity.class));
            }
        });
    }

    private void video_watch()
    {
        SOService soService = ApiUtils.getSOService();
        soService.video_add_to_watch(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(videoId),
                Utils.getSimpleTextBody(String.valueOf(total_spend_time_on_activity_second))).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    is_watch = true;
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                startActivity(new Intent(WatchVideoActivity.this, ServerErrorActivity.class));
            }
        });
    }

//    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        youtube_player_view.release();

        if (activity_start_time != 0)
        {
            activity_end_time = System.currentTimeMillis();
            total_spend_time_on_activity = activity_end_time - activity_start_time;
            video_duration_in_long = Long.parseLong(video_duration);
            total_spend_time_on_activity_second = TimeUnit.MILLISECONDS.toSeconds(total_spend_time_on_activity);

            Log.d("Video_time", totalSeconds+"");
            Log.d("activity_start_time", activity_start_time+"");
            Log.d("activity_end_time", activity_end_time+"");
            Log.d("TimeSpendOnActivty" , total_spend_time_on_activity_second+"");
            Log.d("VideoDurationInLong" , video_duration_in_long+"");

            if (total_spend_time_on_activity_second >= video_duration_in_long)
            {
                if (!is_watch)
                {
                    video_watch();
                }
            }
        }
    }

}
