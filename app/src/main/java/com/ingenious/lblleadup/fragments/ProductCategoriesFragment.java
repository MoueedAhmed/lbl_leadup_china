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
import com.ingenious.lblleadup.adapter.ProductCategoryAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.ProductCategory;
import com.ingenious.lblleadup.models.ProductMainClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCategoriesFragment extends Fragment {

    //Category
    private GridLayoutManager manager;
    private List<ProductCategory> productCategoryList = new ArrayList<>();
    private List<ProductCategory> searchProductCategoryList = new ArrayList<>();
    private ProductCategoryAdapter categoryAdapter;
    private CustomProgressDialogue dialogue;
    private EditText et_search;
    private RecyclerView rvRecycler;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;

    public ProductCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_categories, container, false);

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
        soService.product_category_list(Utils.getSimpleTextBody("0")).enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    productCategoryList = response.body().getProductCategories();
                    categoryAdapter.updateData(productCategoryList);
                }
                else
                {
                    Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProductMainClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loaCategoryPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.product_category_list(Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                if (response.body().getSuccess())
                {
                    productCategoryList = response.body().getProductCategories();
                    categoryAdapter.addNewData(productCategoryList);
                }
            }
            @Override
            public void onFailure(Call<ProductMainClass> call, Throwable t) {
            }
        });
    }

    private void search(String s)
    {
        if (searchProductCategoryList != null)
        {
            for (ProductCategory c : productCategoryList)
            {
                if (c != null)
                {
                    if (c.getName().toLowerCase().contains(s) || c.getName().toUpperCase().contains(s))
                    {
                        searchProductCategoryList.add(c);
                    }
                }
            }
        }
        categoryAdapter.search(searchProductCategoryList);
    }

    private void setAdapter()
    {
        categoryAdapter = new ProductCategoryAdapter(getContext(),productCategoryList);
        rvRecycler.setAdapter(categoryAdapter);
        manager = new GridLayoutManager(getContext(),3);
        manager.setOrientation(GridLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvRecycler.setLayoutManager(manager);
    }

}
