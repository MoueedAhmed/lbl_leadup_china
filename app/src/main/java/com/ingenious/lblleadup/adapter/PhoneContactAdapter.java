package com.ingenious.lblleadup.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.fragments.AddMyNetworkFragment;
import com.ingenious.lblleadup.models.PhoneContact;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhoneContactAdapter extends RecyclerView.Adapter<PhoneContactAdapter.PhoneViewHolder>
{
    private Context context;
    private List<PhoneContact> phoneContactList;

    public PhoneContactAdapter(Context context, List<PhoneContact> phoneContactList)
    {
        this.context = context;
        this.phoneContactList = phoneContactList;
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_my_phone_contacts, null);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position)
    {
        final PhoneContact phoneContact = phoneContactList.get(position);

        holder.tv_name.setText(phoneContact.getName());
        holder.tv_phone_no.setText(phoneContact.getMobileNumber());
        holder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name",phoneContact.getName());
                bundle.putString("phone",phoneContact.getMobileNumber());
                Utils.loadFragmentWithMultipleData(context, new AddMyNetworkFragment(), bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneContactList.size();
    }

    public class PhoneViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name, tv_phone_no;
        ImageView img_add;

        public PhoneViewHolder(View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone_no = itemView.findViewById(R.id.tv_phone_no);
            img_add = itemView.findViewById(R.id.img_add);
        }
    }

    public void search(List<PhoneContact> list)
    {
        phoneContactList = list;
        notifyDataSetChanged();
    }
}
