package com.ingenious.lblleadup.fragments;


import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ingenious.lblleadup.R;
import com.ingenious.lblleadup.Utils.CustomProgressDialogue;
import com.ingenious.lblleadup.adapter.PhoneContactAdapter;
import com.ingenious.lblleadup.models.PhoneContact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment
{
    public static final int REQUEST_READ_CONTACTS = 79;
    private List<PhoneContact> phoneContacts;
    private List<PhoneContact> searchPhoneContacts;
    private PhoneContactAdapter contactAdapter;
    private RecyclerView rvContact;
    private LinearLayoutManager manager;
    private CustomProgressDialogue dialogue;
    private EditText et_search;
    private String permission = Manifest.permission.READ_CONTACTS;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dialogue = new CustomProgressDialogue(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        rvContact = v.findViewById(R.id.rvContact);
        et_search = v.findViewById(R.id.et_search);

        //load Contacts
        laodContact();

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

    public ArrayList<PhoneContact> getAllPhoneContacts()
    {
        dialogue.show();
        Log.d("START","Getting all Contacts");
        ArrayList<PhoneContact> arrContacts = new ArrayList<PhoneContact>();
        PhoneContact phoneContactInfo=null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContext().getContentResolver().query(uri, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


            phoneContactInfo = new PhoneContact();
            phoneContactInfo.setId(phoneContactID);
            phoneContactInfo.setName(contactName);
            phoneContactInfo.setMobileNumber(contactNumber);
            if (phoneContactInfo != null)
            {
                arrContacts.add(phoneContactInfo);
            }
            phoneContactInfo = null;
            cursor.moveToNext();
        }
        dialogue.cancel();
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        return arrContacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(123)
    private void laodContact()
    {
        if (EasyPermissions.hasPermissions(getContext(), permission))
        {
            setPhoneContactAdapter();
        }
        else
        {
            EasyPermissions.requestPermissions(this, "We need permissions", 123, permission);
        }
    }

    private void setPhoneContactAdapter()
    {
        phoneContacts = getAllPhoneContacts();
        contactAdapter = new PhoneContactAdapter(getContext(),phoneContacts);
        rvContact.setAdapter(contactAdapter);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // GridLayoutManager.VERTICAL
        rvContact.setLayoutManager(manager);
    }

    private void search(String s)
    {
        searchPhoneContacts = new ArrayList<>();
        if (searchPhoneContacts != null)
        {
            for (PhoneContact l : phoneContacts)
            {
                if (l != null)
                {
                    if (l.getName().toLowerCase().contains(s) || l.getName().toUpperCase().contains(s))
                    {
                        searchPhoneContacts.add(l);
                    }
                }
            }
        }
        contactAdapter.search(searchPhoneContacts);
    }

}
