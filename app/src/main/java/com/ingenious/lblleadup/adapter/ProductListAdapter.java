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
import com.ingenious.lblleadup.models.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>
{
    private Context context;
    private List<Product> productList;

    public ProductListAdapter(Context context, List<Product> productList)
    {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_product_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position)
    {
        final Product product = productList.get(position);

        Glide.with(context).load(product.getImage()).placeholder(R.drawable.placeholder).into(holder.img_brand);
        holder.tvName.setText(product.getTitle());
        holder.tvDesc.setText(product.getDescription());
        holder.tv_category_name.setText(product.getCategory());

        holder.btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(product.getUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName , tvDesc , tv_category_name;
        ImageView img_brand;
        Button btn_shop;

        public ProductViewHolder(View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            img_brand = itemView.findViewById(R.id.img_brand);
            btn_shop = itemView.findViewById(R.id.btn_shop);
        }
    }

    public void addNewData(List<Product> newProductsnew)
    {
        productList.addAll(newProductsnew);
        notifyDataSetChanged();
    }

    public void updateData(List<Product> newProductsnew)
    {
        productList.clear();
        productList = newProductsnew;
        notifyDataSetChanged();
    }

    public void search(List<Product> list)
    {
        productList = list;
        notifyDataSetChanged();
    }
}
