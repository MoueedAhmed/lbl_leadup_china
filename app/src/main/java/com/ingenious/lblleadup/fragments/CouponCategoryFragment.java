package com.ingenious.lblleadup.fragments;


import android.os.Bundle;
import android.content.Intent;
import com.ingenious.lblleadup.activity.ServerErrorActivity;
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
import com.ingenious.lblleadup.adapter.CategoryAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Category;
import com.ingenious.lblleadup.models.CouponMainClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponCategoryFragment extends Fragment {

    //Category
    private GridLayoutManager manager;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> searchCategoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private CustomProgressDialogue dialogue;
    private EditText et_search;
    private RecyclerView rvRecycler;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    public CouponCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_coupon_category, container, false);

        et_search = v.findViewById(R.id.et_search);
        rvRecycler = v.findViewById(R.id.rvRecycler);

        if (Utils.isOnline(getContext()))
        {
            setAdapter();
            loaCategory();
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
                        loaCategoryPagination();
                    }
                }
            }
        });

        return v;
    }

    private void loaCategory()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.getCategory(Utils.getSimpleTextBody("0")).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    categoryList = response.body().getCategories();
                    categoryAdapter.updateData(categoryList);
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

    private void loaCategoryPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.getCategory(Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<CouponMainClass>() {
            @Override
            public void onResponse(Call<CouponMainClass> call, Response<CouponMainClass> response) {
                if (response.body().getSuccess())
                {
                    categoryList = response.body().getCategories();
                    categoryAdapter.addNewData(categoryList);
                }
            }
            @Override
            public void onFailure(Call<CouponMainClass> call, Throwable t) {
            }
        });
    }

    private void search(String s)
    {
        searchCategoryList = new ArrayList<>();
        if (searchCategoryList != null)
        {
            for (Category c : categoryList)
            {
                if (c != null)
                {
                    if (c.getName().toLowerCase().contains(s) || c.getName().toUpperCase().contains(s))
                    {
                        searchCategoryList.add(c);
                    }
                }
            }
        }
        categoryAdapter.search(searchCategoryList);
    }

    private void setAdapter()
    {
        categoryAdapter = new CategoryAdapter(getContext(),categoryList);
        rvRecycler.setAdapter(categoryAdapter);
        manager = new GridLayoutManager(getContext(),3);
        manager.setOrientation(GridLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvRecycler.setLayoutManager(manager);
    }

}
