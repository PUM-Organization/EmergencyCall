package com.pomorganization.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pomorganization.Models.RowBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel on 6/10/2015.
 * Class maked for storing contacts in shared preferences
 */
public class SharedPreference {
    public static final String PREFS_NAME = "EMERGENCY_APP";
    public static final String CONTACTS = "Contacts";
    private Context context;
    public SharedPreference(Context context)
    {
        this.context = context;
    }

    /**
     * method to save all contacts in shared preferences
     * @param rowBeans list of contacts
     * @return void
     */
    public void storeContact(List<RowBean> rowBeans)
    {
        SharedPreferences contactStore = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = contactStore.edit();
        Gson gson = new Gson();

        String jsonRowBeans = gson.toJson(rowBeans);
        editor.putString(CONTACTS,jsonRowBeans);
        editor.commit();
    }

    /**
     * Load contacts from memory
     * @return ArrayList list witch contacts from shared preferences
     */
    public ArrayList loadContacts(){
        List contacts;
        SharedPreferences contactStore = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(contactStore.contains(CONTACTS))
        {
            String jsonContacts = contactStore.getString(CONTACTS,null);
            Gson gson = new Gson();
            RowBean[] rowBeanItems = gson.fromJson(jsonContacts,RowBean[].class);
            contacts = Arrays.asList(rowBeanItems);
            contacts = new ArrayList(contacts);

        }
        else
            return null;

        return (ArrayList) contacts;
    }

    /**
     * Add contact to memory
     * @param beanSampleList add contact to memory
     */
    public void addContact(RowBean beanSampleList) {
        List contacts = loadContacts();
        if (contacts == null)
            contacts = new ArrayList();
        contacts.add(beanSampleList);
        storeContact(contacts);
    }
}
