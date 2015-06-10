package com.pomorganization.emergencycall;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.pomorganization.Models.RowBean;
import com.pomorganization.emergencycall.Adapters.ContactAdapter;
import com.pomorganization.services.SharedPreference;


import java.util.ArrayList;

/**
 *   Activity to add and show Contacts
 */

public class PhoneNumbersActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "EMERGENCY_APP";
    public static final String CONTACTS = "Contacts";
    private ListView listView1;
    private ArrayList<RowBean> rowBeans = new ArrayList<RowBean>();
    private ContactAdapter adapter;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers);

        sharedPreference = new SharedPreference(getApplicationContext());
        rowBeans = sharedPreference.loadContacts();

        if(rowBeans == null)
            rowBeans = new ArrayList<>();
        listView1 = (ListView)findViewById(R.id.Lista);

        adapter = new ContactAdapter(this,rowBeans);
        listView1.setAdapter(adapter);

    }



    public void onSaveButtonClick(View view)
    {
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText phoneNumberEditText = (EditText) findViewById(R.id.phoneNumEditText);

        RowBean rowBean = new RowBean(nameEditText.getText().toString(),phoneNumberEditText.getText().toString());
        sharedPreference.addContact(rowBean);

        rowBeans.add(rowBean);
        adapter.notifyDataSetChanged();

    }

}
