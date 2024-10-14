package com.example.project;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private EditText editTextGroupName;
    private RecyclerView recyclerViewMembers;
    private MemberAdapter memberAdapter;
    private List<Member> memberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        // Initialize views
        editTextGroupName = findViewById(R.id.editTextGroupName);
        recyclerViewMembers = findViewById(R.id.recyclerView);  // Ensure the ID matches your layout
        FloatingActionButton fab = findViewById(R.id.fab);

        // Initialize member list
        memberList = new ArrayList<>();

        // Sample data (you can replace this with actual data)
        memberList.add(new Member("John Doe", "Hi there!", R.drawable.ic_person, false));
        memberList.add(new Member("Jane Smith", "Hello!", R.drawable.ic_person, false));
        memberList.add(new Member("Emily Davis", "How are you?", R.drawable.ic_person, false));

        // Set up RecyclerView
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MemberAdapter(memberList);
        recyclerViewMembers.setAdapter(memberAdapter);

        // Handle Floating Action Button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = editTextGroupName.getText().toString();
                List<Member> selectedMembers = memberAdapter.getSelectedMembers();

                // Ensure you pass groupName and selectedMembers to the GroupChatActivity
                Intent intent = new Intent(GroupActivity.this, GroupChatActivity.class);
                intent.putExtra("GROUP_NAME", groupName);
                intent.putParcelableArrayListExtra("SELECTED_MEMBERS", (ArrayList<? extends Parcelable>) selectedMembers);
                startActivity(intent); // This will navigate to GroupChatActivity
            }
        });

    }
}