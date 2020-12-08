package com.ingenious.lblleadup.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.fragments.CouponListFragment;
import com.ingenious.lblleadup.fragments.VendorProductFragment;
import com.ingenious.lblleadup.models.Vendor;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder>
{
    private Context context;
    private List<Vendor> vendorList;

    public VendorAdapter(Context context, List<Vendor> vendorList)
    {
        this.context = context;
        this.vendorList = vendorList;
    }

    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_brand, null);
        return new VendorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewHolder holder, int position)
    {
        final Vendor vendor = vendorList.get(position);
        Glide.with(context).load(vendor.getImage()).into(holder.img_brand);
        holder.tvBrandName.setText(vendor.getName());

        holder.img_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("vendor_id" , vendor.getId());
                Utils.loadFragmentWithMultipleData(context , new VendorProductFragment() , bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }

    public class VendorViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvBrandName;
        ImageView img_brand;
        public VendorViewHolder(View itemView)
        {
            super(itemView);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);
            img_brand = itemView.findViewById(R.id.img_brand);
        }
    }
}
