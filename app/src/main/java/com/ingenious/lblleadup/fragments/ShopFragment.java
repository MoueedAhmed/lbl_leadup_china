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
import com.ingenious.lblleadup.adapter.CategoryAdapter;
import com.ingenious.lblleadup.adapter.ProductAdapter;
import com.ingenious.lblleadup.adapter.ProductCategoryAdapter;
import com.ingenious.lblleadup.adapter.VendorAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Category;
import com.ingenious.lblleadup.models.Product;
import com.ingenious.lblleadup.models.ProductCategory;
import com.ingenious.lblleadup.models.ProductMainClass;
import com.ingenious.lblleadup.models.Vendor;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    private RecyclerView rvVendor , rvCategory, rvProduct;
    private TextView tv_vendors_viewAll , tv_category_viewAll , tv_products_viewAll;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;

    //Vendor
    private List<Vendor> vendorList = new ArrayList<>();
    private VendorAdapter vendorAdapter;

    //Category
    private List<ProductCategory> productCategoryList = new ArrayList<>();
    private ProductCategoryAdapter categoryAdapter;

    //Product
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        rvVendor = v.findViewById(R.id.rvVendor);
        rvCategory = v.findViewById(R.id.rvCategory);
        rvProduct = v.findViewById(R.id.rvProduct);
        tv_vendors_viewAll = v.findViewById(R.id.tv_vendors_viewAll);
        tv_category_viewAll = v.findViewById(R.id.tv_category_viewAll);
        tv_products_viewAll = v.findViewById(R.id.tv_products_viewAll);

        tv_category_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.loadFragment(getContext(),new ProductCategoriesFragment());
            }
        });

        tv_vendors_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("category" , false);
                Utils.loadFragmentWithMultipleData(getContext() , new ProductListFragment() , bundle);
            }
        });

        tv_products_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("category" , false);
                Utils.loadFragmentWithMultipleData(getContext() , new ProductListFragment() , bundle);
            }
        });

        if (Utils.isOnline(getContext()))
        {
            loadProduct();
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        return v;
    }

    private void loadProduct()
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.productHome().enqueue(new Callback<ProductMainClass>() {
            @Override
            public void onResponse(Call<ProductMainClass> call, Response<ProductMainClass> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();
                    if (response.body().getSuccess())
                    {
                        vendorList = response.body().getVendors();
                        productCategoryList = response.body().getProductCategories();
                        productList = response.body().getProducts();

                        setCategory();
                        setProduct();
                        setVendor();
                    }
                    else
                    {
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductMainClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void setVendor()
    {
        vendorAdapter = new VendorAdapter(getContext(),vendorList);
        rvVendor.setAdapter(vendorAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL); // GridLayoutManager.VERTICAL
        rvVendor.setLayoutManager(manager);
    }

    private void setCategory()
    {

        categoryAdapter = new ProductCategoryAdapter(getContext(),productCategoryList);
        rvCategory.setAdapter(categoryAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL); // GridLayoutManager.VERTICAL
        rvCategory.setLayoutManager(manager);
    }

    private void setProduct()
    {
        productAdapter = new ProductAdapter(getContext(),productList);
        rvProduct.setAdapter(productAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL); // GridLayoutManager.VERTICAL
        rvProduct.setLayoutManager(manager);
    }

}
