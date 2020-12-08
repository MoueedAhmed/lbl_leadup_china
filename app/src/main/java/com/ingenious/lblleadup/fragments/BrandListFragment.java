package com.ingenious.lblleadup.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ingenious.lblleadup.activity.ServerErrorActivity;
import com.ingenious.lblleadup.adapter.BrandAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Brands;
import com.ingenious.lblleadup.models.CouponMainClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandListFragment extends Fragment {

    //Brands
    private GridLayoutManager manager;
    private List<Brands> brandsList = new ArrayList<>();
    private List<Brands> searchBrandsList = new ArrayList<>();
    private BrandAdapter brandAdapter;
    private CustomProgressDialogue dialogue;
    private EditText et_search;
    private RecyclerView rvBrand;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    public BrandListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brand_list, container, false);

        et_search = v.findViewById(R.id.et_search);
        rvBrand = v.findViewById(R.id.rvBrand);

        if (Utils.isOnline(getContext()))
        {
            setBrand();
            loadBrand();
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

        rvBrand.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadBrandPagination();
                    }
                }
            }
        });

        return v;
    }

    private void loadBrand()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.getBrands(Utils.getSimpleTextBody("0")).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    brandsList = response.body().getStores();
                    brandAdapter.updateData(brandsList);
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

    private void loadBrandPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.getBrands(Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                if (response.body().getSuccess())
                {
                    brandsList = response.body().getStores();
                    brandAdapter.addNewData(brandsList);
                }
            }
            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
            }
        });
    }

    private void search(String s)
    {
        searchBrandsList = new ArrayList<>();
        if (searchBrandsList != null)
        {
            for (Brands b : brandsList)
            {
                if (b != null)
                {
                    if (b.getName().toLowerCase().contains(s) || b.getName().toUpperCase().contains(s))
                    {
                        searchBrandsList.add(b);
                    }
                }
            }
        }
        brandAdapter.search(searchBrandsList);
    }

    private void setBrand()
    {
        brandAdapter = new BrandAdapter(getContext(),brandsList);
        rvBrand.setAdapter(brandAdapter);
        manager = new GridLayoutManager(getContext(),3);
        manager.setOrientation(GridLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvBrand.setLayoutManager(manager);
    }

}
