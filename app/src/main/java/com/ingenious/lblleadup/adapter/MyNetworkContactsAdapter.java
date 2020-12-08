package com.ingenious.lblleadup.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.activity.LoginActivity;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.fragments.AddMyNetworkFragment;
import com.ingenious.lblleadup.models.GlobalResponse;
import com.ingenious.lblleadup.models.Lead;
import com.pixplicity.easyprefs.library.Prefs;

import java.net.URLEncoder;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNetworkContactsAdapter extends RecyclerView.Adapter<MyNetworkContactsAdapter.LeadViewHolder>
{
    private Context context;
    private List<Lead> leadList;
    private CustomProgressDialogue dialogue;

    public MyNetworkContactsAdapter(Context context, List<Lead> leadList)
    {
        this.context = context;
        this.leadList = leadList;
        dialogue = new CustomProgressDialogue(context);
    }

    @Override
    public LeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_my_network_contacts, null);
        return new LeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadViewHolder holder, final int position)
    {
        final Lead lead = leadList.get(position);

        //holder.noti_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_anim_fall_down));

        holder.tv_name.setText(lead.getName());
        holder.tv_email.setText(lead.getEmail());
        holder.tv_category.setText(lead.getCategory());

        holder.img_whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utils.openAndSendWhatsApp(context,lead.getWhatsapp(),lead.getWhatsappMessage());
            }
        });

        holder.img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Utils.openGmail(context,lead.getEmail(),"Join LBL Leadup" , lead.getEmailMessage());
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove_leads(lead.getLeadId() , position);
            }
        });

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragmentWithData(context , new AddMyNetworkFragment() , "id" , lead.getLeadId());
            }
        });

        holder.img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+lead.getPhone()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class LeadViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name, tv_email, tv_category;
        ImageView img_whatsApp, img_phone , img_email , img_delete, img_edit;

        public LeadViewHolder(View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_category = itemView.findViewById(R.id.tv_category);
            img_whatsApp = itemView.findViewById(R.id.img_whatsApp);
            img_phone = itemView.findViewById(R.id.img_phone);
            img_email = itemView.findViewById(R.id.img_email);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_edit = itemView.findViewById(R.id.img_edit);
        }
    }

    private void remove_leads(String lead_id , final int position)
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.remove_leads(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(lead_id)).enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body().getSuccess())
                    {
                        remove_leads(position);
                        Toasty.success(context,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toasty.error(context,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {
                dialogue.cancel();
                Toasty.error(context,R.string.error_on_request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void remove_leads(int posistion)
    {
        leadList.remove(posistion);
        notifyDataSetChanged();
    }

    public void add_new_data(List<Lead> leads)
    {
        leadList.addAll(leads);
        notifyDataSetChanged();
    }

    public void update_data(List<Lead> leads)
    {
        leadList.clear();
        leadList = leads;
        notifyDataSetChanged();
    }

    public void clearRecyclerView()
    {
        leadList.clear();
        notifyDataSetChanged();
    }

    public void search(List<Lead> list)
    {
        leadList = list;
        notifyDataSetChanged();
    }
}
