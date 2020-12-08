package com.ingenious.lblleadup.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.ClickLink;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClickAndEarnAdapter extends RecyclerView.Adapter<ClickAndEarnAdapter.ClickViewHolder>
{
    private Context context;
    private List<ClickLink> clickandEarnList;

    public ClickAndEarnAdapter(Context context, List<ClickLink> clickandEarnList)
    {
        this.context = context;
        this.clickandEarnList = clickandEarnList;
    }

    @Override
    public ClickViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_click_and_earn, null);
        return new ClickViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClickViewHolder holder, final int position)
    {
        final ClickLink clickandEarn = clickandEarnList.get(position);

        holder.tv_title.setText(clickandEarn.getLinkTitle());

        holder.btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_on_link(clickandEarn.getLinkId());
                Uri uri = Uri.parse(clickandEarn.getLinkUrl()); // missing 'http://' will cause crashed
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                clickandEarnList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clickandEarnList.size();
    }

    public class ClickViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        CardView card_click_earn;
        Button btn_click;

        public ClickViewHolder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            card_click_earn = itemView.findViewById(R.id.card_click_earn);
            btn_click = itemView.findViewById(R.id.btn_click);
        }
    }

    private void click_on_link(String link_id)
    {
        SOService soService = ApiUtils.getSOService();
        soService.click_on_link(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(link_id)).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {

                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                Toasty.error(context,R.string.error_on_request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addNewData(List<ClickLink> clickLinks)
    {
        clickandEarnList.addAll(clickLinks);
        notifyDataSetChanged();
    }

    public void updateData(List<ClickLink> clickLinks)
    {
        clickandEarnList.clear();
        clickandEarnList = clickLinks;
        notifyDataSetChanged();
    }
}
