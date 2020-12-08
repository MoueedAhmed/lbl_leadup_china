package com.ingenious.lblleadup.fragments;


import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.adapter.BrandAdapter;
import com.ingenious.lblleadup.adapter.CategoryAdapter;
import com.ingenious.lblleadup.adapter.CouponAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Brands;
import com.ingenious.lblleadup.models.Category;
import com.ingenious.lblleadup.models.CouponMainClass;
import com.ingenious.lblleadup.models.Coupons;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponFragment extends Fragment {

    private RecyclerView rvBrand , rvCategory, rvCoupons;
    private TextView tv_brand_viewAll , tv_category_viewAll , tv_coupons_viewAll;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;

    //Brands
    private List<Brands> brandsList = new ArrayList<>();
    private BrandAdapter brandAdapter;

    //Category
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    //Coupons
    private List<Coupons> couponsList = new ArrayList<>();
    private CouponAdapter couponAdapter;

    public CouponFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_coupon, container, false);

        rvBrand = v.findViewById(R.id.rvBrand);
        rvCategory = v.findViewById(R.id.rvCategory);
        rvCoupons = v.findViewById(R.id.rvCoupons);
        tv_brand_viewAll = v.findViewById(R.id.tv_brand_viewAll);
        tv_category_viewAll = v.findViewById(R.id.tv_category_viewAll);
        tv_coupons_viewAll = v.findViewById(R.id.tv_coupons_viewAll);

        if (Utils.isOnline(getContext()))
        {
            loadCouponBrandCatgeory();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        tv_brand_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new BrandListFragment());
            }
        });

        tv_category_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new CouponCategoryFragment());
            }
        });

        tv_coupons_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("category" , false);
                bundle.putBoolean("brand" , false);
                Utils.loadFragmentWithMultipleData(getContext() , new CouponListFragment() , bundle);
            }
        });

        return v;
    }

    private void setBrand()
    {
        brandAdapter = new BrandAdapter(getContext(),brandsList);
        rvBrand.setAdapter(brandAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL); // GridLayoutManager.VERTICAL
        rvBrand.setLayoutManager(manager);
    }

    private void setCategory()
    {
        categoryAdapter = new CategoryAdapter(getContext(),categoryList);
        rvCategory.setAdapter(categoryAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL); // GridLayoutManager.VERTICAL
        rvCategory.setLayoutManager(manager);
    }

    private void setCoupon()
    {
        couponAdapter = new CouponAdapter(getContext(),couponsList);
        rvCoupons.setAdapter(couponAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL); // GridLayoutManager.VERTICAL
        rvCoupons.setLayoutManager(manager);
    }

    private void loadCouponBrandCatgeory()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.couponHome().enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body().getSuccess())
                    {
                        brandsList = response.body().getStores();
                        categoryList = response.body().getCategories();
                        couponsList = response.body().getCoupons();

                        setBrand();
                        setCategory();
                        setCoupon();
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

}
