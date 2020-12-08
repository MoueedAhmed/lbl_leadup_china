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
    import com.ingenious.lblleadup.models.Brands;

    import java.util.List;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>
{ 
    private Context context;
    private List<Brands> BrandList;

    public BrandAdapter(Context context, List<Brands> BrandList)
    {
        this.context = context;
        this.BrandList = BrandList;
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_brand, null);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position)
    {
        final Brands Brand = BrandList.get(position);
        Glide.with(context).load(Brand.getImage()).into(holder.img_brand);
        holder.tvBrandName.setText(Brand.getName());

        holder.img_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("category" , false);
                bundle.putBoolean("brand" , true);
                bundle.putString("brand_id" , Brand.getId());
                Utils.loadFragmentWithMultipleData(context , new CouponListFragment() , bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return BrandList.size();
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvBrandName;
        ImageView img_brand;
        public BrandViewHolder(View itemView)
        {
            super(itemView);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);
            img_brand = itemView.findViewById(R.id.img_brand);
        }
    }

    public void addNewData(List<Brands> newProductsnew)
    {
        BrandList.addAll(newProductsnew);
        notifyDataSetChanged();
    }

    public void updateData(List<Brands> newProductsnew)
    {
        BrandList.clear();
        BrandList = newProductsnew;
        notifyDataSetChanged();
    }

    public void search(List<Brands> list)
    {
        BrandList = list;
        notifyDataSetChanged();
    }
}