package com.ingenious.lblleadup.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.BuildConfig;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.ClickLink;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.ShareableLink;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ShareEarnAdapter extends RecyclerView.Adapter<ShareEarnAdapter.ShareViewHolder>
{
    private Context context;
    private List<ShareableLink> shareableLinkList;

    public ShareEarnAdapter(Context context, List<ShareableLink> shareableLinkList)
    {
        this.context = context;
        this.shareableLinkList = shareableLinkList;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_share_earn, null);
        return new ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, int position)
    {
        final ShareableLink shareableLink = shareableLinkList.get(position);

        holder.card_view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_anim_fall_down));
        holder.tv_title.setText(shareableLink.getLinkTitle());

        holder.img_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openFacebook(context , shareableLink.getLinkUrl());
                click_on_link(shareableLink.getLinkId());
            }
        });

        holder.img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openTwitter(context , shareableLink.getLinkUrl(),"");
                click_on_link(shareableLink.getLinkId());
            }
        });

        holder.img_whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWhatsApp(context , shareableLink.getLinkUrl());
                click_on_link(shareableLink.getLinkId());
            }
        });

        holder.img_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWeChat(context, shareableLink.getLinkUrl());
                click_on_link(shareableLink.getLinkId());
            }
        });

        holder.img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("token", shareableLink.getLinkUrl());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return shareableLinkList.size();
    }

    private void click_on_link(String link_id)
    {
        SOService soService = ApiUtils.getSOService();
        soService.click_on_share_earn_link(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(link_id)).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {

                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                //Toasty.error(context,R.string.error_on_request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class ShareViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        CardView card_view;
        ImageView img_fb , img_twitter , img_wechat, img_whatsApp, img_copy;

        public ShareViewHolder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            card_view = itemView.findViewById(R.id.card_view);
            img_fb = itemView.findViewById(R.id.img_fb);
            img_twitter = itemView.findViewById(R.id.img_twitter);
            img_wechat = itemView.findViewById(R.id.img_wechat);
            img_whatsApp = itemView.findViewById(R.id.img_whatsApp);
            img_copy = itemView.findViewById(R.id.img_copy);
        }
    }

    public void addNewData(List<ShareableLink> shareableLinks)
    {
        shareableLinkList.addAll(shareableLinks);
        notifyDataSetChanged();
    }

    public void updateData(List<ShareableLink> shareableLinks)
    {
        shareableLinkList.clear();
        shareableLinkList = shareableLinks;
        notifyDataSetChanged();
    }
}
