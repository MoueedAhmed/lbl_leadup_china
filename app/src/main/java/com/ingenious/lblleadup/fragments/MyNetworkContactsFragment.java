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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.Utils.Utils;
import com.ingenious.lblleadup.adapter.MyNetworkContactsAdapter;
import com.ingenious.lblleadup.api.ApiUtils;
import com.ingenious.lblleadup.api.SOService;
import com.ingenious.lblleadup.models.Lead;
import com.ingenious.lblleadup.models.MyNetworkContactClass;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyNetworkContactsFragment extends Fragment {

    private List<Lead> leadList = new ArrayList<>();
    private List<Lead> searchleadList = new ArrayList<>();
    private MyNetworkContactsAdapter adapter;
    private RecyclerView rvMyNetworkContact;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;
    private Boolean isScrolling = false;
    private int currentItems, totalItems = 0, scrollOutItems;
    private String[] categories = {"All" , "Family" , "Friends" , "Business" , "Other"};
    private Spinner spinner_category;
    private EditText et_search;

    public MyNetworkContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_network_contacts, container, false);

        rvMyNetworkContact = v.findViewById(R.id.rvMyNetworkContact);
        et_search = v.findViewById(R.id.et_search);
        spinner_category = v.findViewById(R.id.spinner_category);

        spinner_category.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,categories));

        if (Utils.isOnline(getContext()))
        {
            setAdapter();
            loadMyNetworkContactCategoryWise("All");
        }
        else
        {
            Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        rvMyNetworkContact.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadMyNetworkContactCategoryWisePagination(spinner_category.getSelectedItem().toString());
                    }
                }
            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (Utils.isOnline(getContext()))
                {
                    loadMyNetworkContactCategoryWise(spinner_category.getSelectedItem().toString());
                }
                else
                {
                    Toasty.error(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        return v;
    }

    private void loadMyNetworkContactCategoryWise(String categoryName)
    {
        dialogue.show();
        SOService soService = ApiUtils.getSOService();
        soService.myNetworkContactsCategoryWise(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(categoryName),
                Utils.getSimpleTextBody("0")).enqueue(new Callback<MyNetworkContactClass>() {
            @Override
            public void onResponse(Call<MyNetworkContactClass> call, Response<MyNetworkContactClass> response) {
                if (response.isSuccessful())
                {
                    dialogue.cancel();

                    if (response.body().getSuccess())
                    {
                        adapter.clearRecyclerView();
                        leadList = response.body().getLeads();
                        adapter.add_new_data(leadList);
                    }
                    else
                    {
                        adapter.clearRecyclerView();
                        Toasty.error(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<MyNetworkContactClass> call, Throwable t) {
                dialogue.cancel();
                startActivity(new Intent(getContext(), ServerErrorActivity.class));
            }
        });
    }

    private void loadMyNetworkContactCategoryWisePagination(String categoryName)
    {
        SOService soService = ApiUtils.getSOService();
        soService.myNetworkContactsCategoryWise(Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(categoryName),
                Utils.getSimpleTextBody(String.valueOf(totalItems))).enqueue(new Callback<MyNetworkContactClass>() {
            @Override
            public void onResponse(Call<MyNetworkContactClass> call, Response<MyNetworkContactClass> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        adapter.clearRecyclerView();
                        leadList = response.body().getLeads();
                        adapter.update_data(leadList);
                    }
                }
            }
            @Override
            public void onFailure(Call<MyNetworkContactClass> call, Throwable t) { }
        });
    }

    private void setAdapter()
    {
        adapter = new MyNetworkContactsAdapter(getContext(),leadList);
        rvMyNetworkContact.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvMyNetworkContact.setLayoutManager(manager);
    }

    private void search(String s)
    {
        searchleadList = new ArrayList<>();
        if (searchleadList != null)
        {
            for (Lead l : leadList)
            {
                if (l != null)
                {
                    if (l.getName().toLowerCase().contains(s) || l.getName().toUpperCase().contains(s))
                    {
                        searchleadList.add(l);
                    }
                }
            }
        }
        adapter.search(searchleadList);
    }

}
