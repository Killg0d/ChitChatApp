package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<User> memberList;

    public MemberAdapter(List<User> memberList) {
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User member = memberList.get(position);
        holder.nameTextView.setText(member.getFullName());
        holder.statusTextView.setText(member.getDescription());

        // Here you would load the profile image with an image loading library if necessary
        holder.profileImageView.setImageResource(R.drawable.ic_person);

        // Set the checkbox state based on the user's selection state
        holder.checkBox.setChecked(member.isSelected()); // Set the checkbox to reflect the user's selection

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            member.setSelected(isChecked); // Set selection status if needed
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public List<User> getSelectedMembers() {
        List<User> selectedMembers = new ArrayList<>();
        for (User member : memberList) {
            if (member.isSelected()) { // Assuming `User` has `isSelected()` method
                selectedMembers.add(member);
            }
        }
        return selectedMembers;
    }

    // ViewHolder class
    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView;
        ImageView profileImageView;
        CheckBox checkBox;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
