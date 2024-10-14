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

    private List<Member> memberList;

    public MemberAdapter(List<Member> memberList) {
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
        Member member = memberList.get(position);
        holder.nameTextView.setText(member.getName());
        holder.statusTextView.setText(member.getStatus());
        holder.profileImageView.setImageResource(member.getProfileImageResId());
        holder.checkBox.setChecked(member.isSelected());

        // Set the checkbox change listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            member.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
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

    // Method to get the selected members
    public List<Member> getSelectedMembers() {
        List<Member> selectedMembers = new ArrayList<>();
        for (Member member : memberList) {
            if (member.isSelected()) {
                selectedMembers.add(member);
            }
        }
        return selectedMembers;
    }
}
