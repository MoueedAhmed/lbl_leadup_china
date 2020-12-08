package com.ingenious.lblleadup.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.models.Apps;
import com.ingenious.lblleadup.models.ClickLink;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.AppsViewHolder>
{
    private Context context;
    private List<Apps> appsList;

    public ApplicationAdapter(Context context, List<Apps> appsList)
    {
        this.context = context;
        this.appsList = appsList;
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_apps, null);
        return new AppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder, int position)
    {
        final Apps apps = appsList.get(position);

        Glide.with(context).load(apps.getAppIcon()).placeholder(R.drawable.placeholder).into(holder.img_logo);
        holder.tv_title.setText(apps.getAppTitle());

        holder.btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openPlayStore(context , apps.getAppUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public class AppsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_logo;
        TextView tv_title;
        CardView card_view;
        Button btn_click;

        public AppsViewHolder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            card_view = itemView.findViewById(R.id.card_view);
            btn_click = itemView.findViewById(R.id.btn_click);
            img_logo = itemView.findViewById(R.id.img_logo);
        }
    }

    public void addNewData(List<Apps> appsList)
    {
        appsList.addAll(appsList);
        notifyDataSetChanged();
    }

    public void updateData(List<Apps> appsListNew)
    {
        appsList.clear();
        appsList = appsListNew;
        notifyDataSetChanged();
    }
}
