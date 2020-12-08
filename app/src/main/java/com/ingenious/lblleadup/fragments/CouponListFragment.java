package com.ingenious.lblleadup.fragments;


import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.adapter.CouponAdapter;
import com.ingenious.lblleadup.adapter.CouponListAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.CouponMainClass;
import com.ingenious.lblleadup.models.Coupons;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponListFragment extends Fragment {

    private CustomProgressDialogue dialogue;
    private EditText et_search;
    private RecyclerView rvRecycler;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;
    private String category_id , brand_id;
    private boolean is_cat , is_brand;
    private Bundle arguments;

    //Coupons
    private List<Coupons> couponsList = new ArrayList<>();
    private List<Coupons> searchCouponsList = new ArrayList<>();
    private CouponListAdapter couponAdapter;

    public CouponListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());
        arguments = getArguments();

        is_cat = arguments.getBoolean("category" , false);
        is_brand = arguments.getBoolean("brand" , false);

        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_coupon_list, container, false);

        et_search = v.findViewById(R.id.et_search);
        rvRecycler = v.findViewById(R.id.rvRecycler);

        if (Utils.isOnline(getContext()))
        {
            setAdapter();
            if (is_cat)
            {
                loaCouponByCategory(arguments.getString("category_id"));
            }
            else if (is_brand)
            {
                loaCouponByBrand(arguments.getString("brand_id"));
            }
            else
            {
                loaCoupon();
            }

        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rvRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(dy > 0)
                {
                    if (isScrolling && (currentItems + scrollOutItems == totalItems))
                    {
                        //Fetch new Data
                        isScrolling = false;
                        if (is_cat)
                        {
                            loaCouponByCategoryPagination(arguments.getString("category_id"));
                        }
                        else if (is_brand)
                        {
                            loaCouponByBrandPagination(arguments.getString("brand_id"));
                        }
                        else
                        {
                            loaCouponPagination();
                        }
                    }
                }
            }
        });
        return v;
    }

    private void loaCoupon()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.latest_coupans(Utils.getSimpleTextBody("0")).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    couponsList = response.body().getCoupons();
                    couponAdapter.updateData(couponsList);
                }
                else
                {
                    Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loaCouponPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.latest_coupans(Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                if (response.body().getSuccess())
                {
                    couponsList = response.body().getCoupons();
                    couponAdapter.addNewData(couponsList);
                }
            }
            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {}
        });
    }

    private void loaCouponByCategory(String category_id)
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.coupon_by_category(Utils.getSimpleTextBody(category_id),Utils.getSimpleTextBody("0")).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    couponsList = response.body().getCoupons();
                    couponAdapter.updateData(couponsList);
                }
                else
                {
                    Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loaCouponByCategoryPagination(String category_id)
    {
        SOService soService = ApiUtils.getSOService();
        soService.coupon_by_category(Utils.getSimpleTextBody(category_id),Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    couponsList = response.body().getCoupons();
                    couponAdapter.addNewData(couponsList);
                }
            }
            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
            }
        });
    }

    private void loaCouponByBrand(String brand_id)
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.coupon_by_brand(Utils.getSimpleTextBody(brand_id),Utils.getSimpleTextBody("0")).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    couponsList = response.body().getCoupons();
                    couponAdapter.updateData(couponsList);
                }
                else
                {
                    Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loaCouponByBrandPagination(String brand_id)
    {
        SOService soService = ApiUtils.getSOService();
        soService.coupon_by_brand(Utils.getSimpleTextBody(brand_id),Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                if (response.body().getSuccess())
                {
                    couponsList = response.body().getCoupons();
                    couponAdapter.addNewData(couponsList);
                }
            }
            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
            }
        });
    }

    private void search(String s)
    {
        searchCouponsList = new ArrayList<>();
        if (searchCouponsList != null)
        {
            for (Coupons c : couponsList)
            {
                if (c != null)
                {
                    if (c.getTitle().toLowerCase().contains(s) || c.getTitle().toUpperCase().contains(s))
                    {
                        searchCouponsList.add(c);
                    }
                }
            }
        }
        couponAdapter.search(searchCouponsList);
    }

    private void setAdapter()
    {
        couponAdapter = new CouponListAdapter(getContext(),couponsList);
        rvRecycler.setAdapter(couponAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvRecycler.setLayoutManager(manager);
    }

}
