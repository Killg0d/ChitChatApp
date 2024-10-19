package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageList> {
    private static final int LAYOUT_TYPE_1 = 0; // Layout with TextView for message
    private static final int LAYOUT_TYPE_2 = 1; // Another layout with TextView for message

    private int layoutType = LAYOUT_TYPE_1;

    public MessageAdapter(Context context, List<MessageList> items) {
        super(context, 0, items);
    }

    public MessageAdapter(Context context, List<MessageList> items, int layoutType) {
        super(context, 0, items);
        this.layoutType = layoutType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MessageList item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if (layoutType == LAYOUT_TYPE_1) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item, parent, false);
            } else if (layoutType == LAYOUT_TYPE_2) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.listitem2, parent, false);
            }
        }

        // Lookup view for data population
        ImageView profilePicture = convertView.findViewById(R.id.profile_picture);
        TextView name = convertView.findViewById(R.id.name);
        TextView message = convertView.findViewById(R.id.message); // Using TextView for both layout types

        // Populate the data into the template view using the data object
        profilePicture.setImageResource(item.getProfilePictureResId());
        name.setText(item.getName());
        message.setText(item.getMessage());

        // Return the completed view to render on screen
        return convertView;
    }
}
