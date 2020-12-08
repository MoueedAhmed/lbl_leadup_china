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

import com.bumptech.glide.Glide;
import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.models.Coupons;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder>
{
    private Context context;
    private List<Coupons> couponsList;

    public CouponAdapter(Context context, List<Coupons> couponsList)
    {
        this.context = context;
        this.couponsList = couponsList;
    }

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_coupon, null);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position)
    {
        final Coupons coupon = couponsList.get(position);

        Glide.with(context).load(coupon.getStoreImage()).into(holder.img_brand);
        holder.tvCouponName.setText(coupon.getTitle());
        holder.tvCouponDesc.setText(coupon.getDescription());
        holder.tv_expiry_date.setText(coupon.getExpireDate());
        holder.tv_category_name.setText(coupon.getCategory());
        holder.btn_coupon.setText(coupon.getBtnText());

        holder.btn_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(coupon.getAffiliateUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    public class CouponViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvCouponName , tvCouponDesc , tv_expiry_date , tv_category_name;
        ImageView img_brand;
        Button btn_coupon;

        public CouponViewHolder(View itemView)
        {
            super(itemView);
            tvCouponName = itemView.findViewById(R.id.tvCouponName);
            tvCouponDesc = itemView.findViewById(R.id.tvCouponDesc);
            tv_expiry_date = itemView.findViewById(R.id.tv_expiry_date);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            img_brand = itemView.findViewById(R.id.img_brand);
            btn_coupon = itemView.findViewById(R.id.btn_coupon);
        }
    }
}
