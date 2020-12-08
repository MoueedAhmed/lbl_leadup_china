package com.ingenious.lblleadup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.models.Announcement;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{
    private Context context;
    private List<Announcement> notificationClassList;

    public NotificationAdapter(Context context, List<Announcement> notificationClassList)
    {
        this.context = context;
        this.notificationClassList = notificationClassList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_notification, null);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position)
    {
        final Announcement notificationClass = notificationClassList.get(position);

        holder.noti_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_anim_fall_down));

        holder.tv_title.setText(notificationClass.getAnnouncementTitle());
        holder.tv_date.setText(notificationClass.getAnnouncementDate());
        holder.tvBody.setText(notificationClass.getAnnouncementDescription());
        holder.tvBody.setShowingLine(4);
        holder.tvBody.addShowMoreText("Read More");
        holder.tvBody.addShowLessText("Read Less");
    }

    @Override
    public int getItemCount() {
        return notificationClassList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title, tv_date;
        ShowMoreTextView tvBody;
        LinearLayout noti_layout;

        public NotificationViewHolder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvBody = itemView.findViewById(R.id.tvBody);
            noti_layout = itemView.findViewById(R.id.noti_layout);
        }
    }

    public void addNewData(List<Announcement> announcement)
    {
        notificationClassList.addAll(announcement);
        notifyDataSetChanged();
    }

    public void updateData(List<Announcement> announcement)
    {
        notificationClassList.clear();
        notificationClassList = announcement;
        notifyDataSetChanged();
    }
}
