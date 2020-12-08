package com.ingenious.lblleadup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.WatchVideoActivity;
import com.ingenious.lblleadup.models.Video;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.WatchViewHolder>
{
    private Context context;
    private List<Video> watchEarnList;

    public WatchAdapter(Context context, List<Video> watchEarnList)
    {
        this.context = context;
        this.watchEarnList = watchEarnList;
    }

    @Override
    public WatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_watch_earn, null);
        return new WatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchViewHolder holder, int position)
    {
        final Video video = watchEarnList.get(position);

        holder.tv_title.setText(video.getVideoTitle());
        Glide.with(context).load(video.getVideoThumbnail()).placeholder(R.drawable.video_thumbnail).into(holder.img_thumbnail);

        holder.btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utils.loadFragmentWithData(context, new WatchVideoFragment(), "id", video.getVideoId());
                context.startActivity(new Intent(context, WatchVideoActivity.class).putExtra("id",video.getVideoId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return watchEarnList.size();
    }

    public class WatchViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        CardView card_watch_earn;
        Button btn_click;
        ImageView img_thumbnail;

        public WatchViewHolder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            card_watch_earn = itemView.findViewById(R.id.card_watch_earn);
            btn_click = itemView.findViewById(R.id.btn_click);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }

    public void addNewData(List<Video> videos)
    {
        watchEarnList.addAll(videos);
        notifyDataSetChanged();
    }

    public void updateData(List<Video> videos)
    {
        watchEarnList.clear();
        watchEarnList = videos;
        notifyDataSetChanged();
    }
}
