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
import com.ingenious.lblleadup.adapter.ProductListAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Product;
import com.ingenious.lblleadup.models.ProductMainClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment {

    private CustomProgressDialogue dialogue;
    private EditText et_search;
    private RecyclerView rvRecycler;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;
    private boolean is_cat ;
    private Bundle arguments;
    //Product
    private List<Product> productList = new ArrayList<>();
    private List<Product> searchProductList = new ArrayList<>();
    private ProductListAdapter productAdapter;

    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());
        arguments = getArguments();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);
        et_search = v.findViewById(R.id.et_search);
        rvRecycler = v.findViewById(R.id.rvRecycler);

        is_cat = arguments.getBoolean("category" , false);

        if (Utils.isOnline(getContext()))
        {
            setAdapter();
            if (is_cat)
            {
                loadCatrgoryProduct(arguments.getString("category_id"));
            }
            else
            {
                loadProduct();
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
                            loadCatrgoryProductPagination(arguments.getString("category_id"));
                        }
                        else
                        {
                            loadProductPagination();
                        }
                    }
                }
            }
        });

        return v;
    }

    private void loadProduct()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.latest_products(Utils.getSimpleTextBody("0")).enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    productList = response.body().getProducts();
                    productAdapter.updateData(productList);
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

    private void loadProductPagination()
    {
        SOService soService = ApiUtils.getSOService();
        soService.latest_products(Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    productList = response.body().getProducts();
                    productAdapter.addNewData(productList);
                }
            }
            @Override
            public void onFailure(Call<ProductMainClass> call, Throwable t) { }
        });
    }

    private void loadCatrgoryProduct(String cat_id)
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.category_products(Utils.getSimpleTextBody("0"),
                Utils.getSimpleTextBody(cat_id)).enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    productList = response.body().getProducts();
                    productAdapter.updateData(productList);
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

    private void loadCatrgoryProductPagination(String cat_id)
    {
        SOService soService = ApiUtils.getSOService();
        soService.category_products(Utils.getSimpleTextBody(String.valueOf(totalItems)),
                Utils.getSimpleTextBody(cat_id)).enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                dialogue.cancel();
                if (response.body().getSuccess())
                {
                    productList = response.body().getProducts();
                    productAdapter.addNewData(productList);
                }
            }
            @Override
            public void onFailure(Call<ProductMainClass> call, Throwable t) {
            }
        });
    }

    private void search(String s)
    {
        searchProductList = new ArrayList<>();
        if (searchProductList != null)
        {
            for (Product c : productList)
            {
                if (c != null)
                {
                    if (c.getTitle().toLowerCase().contains(s) || c.getTitle().toUpperCase().contains(s))
                    {
                        searchProductList.add(c);
                    }
                }
            }
        }
        productAdapter.search(searchProductList);
    }

    private void setAdapter()
    {
        productAdapter = new ProductListAdapter(getContext(),productList);
        rvRecycler.setAdapter(productAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvRecycler.setLayoutManager(manager);
    }

}
