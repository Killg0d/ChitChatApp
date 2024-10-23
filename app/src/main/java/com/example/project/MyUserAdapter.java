package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class MyUserAdapter extends ArrayAdapter<User> {
    public MyUserAdapter(Context context, List<User> user)
    {
        super(context,0,user);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        User user=getItem(position);
        TextView name=convertView.findViewById(R.id.name);
        TextView description=convertView.findViewById(R.id.message);
        name.setText(user.getFullName());
        description.setText(user.getDescription());
        ImageView img = convertView.findViewById(R.id.profile_picture);
        String profilepath=user.getProfileurl();
        if(profilepath!=null)
        {
            Glide.with(this.getContext())
                    .load(profilepath)
                    .placeholder(R.drawable.person)
                    .error(R.drawable.person)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img);
        }
        else
        {
            img.setImageResource(R.drawable.person);
        }
        return convertView;
    }
}
