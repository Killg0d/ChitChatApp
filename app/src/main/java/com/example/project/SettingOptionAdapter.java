package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SettingOptionAdapter extends ArrayAdapter<SettingOption> {
    public SettingOptionAdapter(Context context, List<SettingOption> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SettingOption item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_options, parent, false);
        }

        // Lookup view for data population
        ImageView icon = convertView.findViewById(R.id.icon);
        TextView name = convertView.findViewById(R.id.name);

        // Populate the data into the template view using the data object
        icon.setImageResource(item.getIconResId());
        name.setText(item.getName());

        // Return the completed view to render on screen
        return convertView;
    }
}
