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
import com.ingenious.lblleadup.models.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>
{
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList)
    {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_category, null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position)
    {
        final Category category = categoryList.get(position);
        Glide.with(context).load(category.getImage()).into(holder.img_category);
        holder.tvcategoryName.setText(category.getName());

        holder.img_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("category" , true);
                bundle.putBoolean("brand" , false);
                bundle.putString("category_id" , category.getId());
                Utils.loadFragmentWithMultipleData(context , new CouponListFragment() , bundle);
            }
        });
    }

    @Override
    public int getItemCount() { return categoryList.size(); }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvcategoryName;
        ImageView img_category;
        public CategoryViewHolder(View itemView)
        {
            super(itemView);
            tvcategoryName = itemView.findViewById(R.id.tvCategoryName);
            img_category = itemView.findViewById(R.id.img_category);
        }
    }

    public void addNewData(List<Category> newProductsnew)
    {
        categoryList.addAll(newProductsnew);
        notifyDataSetChanged();
    }

    public void updateData(List<Category> newProductsnew)
    {
        categoryList.clear();
        categoryList = newProductsnew;
        notifyDataSetChanged();
    }

    public void search(List<Category> list)
    {
        categoryList = list;
        notifyDataSetChanged();
    }
}