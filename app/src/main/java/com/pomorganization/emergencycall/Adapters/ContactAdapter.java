package com.pomorganization.emergencycall.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pomorganization.Models.RowBean;
import com.pomorganization.emergencycall.R;

import java.util.ArrayList;

/**
 * Created by Daniel on 6/10/2015.
 * Adapter to show list witch contacts
 */
public class ContactAdapter extends ArrayAdapter<RowBean> {
    public ContactAdapter(Context context, ArrayList<RowBean> users) {
        super(context, 0, users);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RowBean user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_row, parent, false);
        }
        // Lookup view for data population
        TextView Name = (TextView) convertView.findViewById(R.id.listContacNameEditText);
        TextView PhoneNumber = (TextView) convertView.findViewById(R.id.listContacPhoneNumberEditText);
        // Populate the data into the template view using the data object
        Name.setText(user.getName());
        PhoneNumber.setText(user.getPhoneNumber());
        // Return the completed view to render on screen
        return convertView;
    }
}
