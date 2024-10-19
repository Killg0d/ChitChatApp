package com.example.project;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AccountSetting extends BaseSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Accounts");
        actionbar.setDisplayHomeAsUpEnabled(true);
        ListView optionView = findViewById(R.id.option_view);
        ArrayList<SettingOption> items = new ArrayList<>();
        items.add(new SettingOption("Email Address",R.mipmap.ic_email));
        items.add(new SettingOption("Change Password",R.mipmap.ic_lock));
        items.add(new SettingOption("Delete Account",R.mipmap.ic_bin));
        SettingOptionAdapter aa = new SettingOptionAdapter(this,items);
        optionView.setAdapter(aa);
        optionView.setOnItemClickListener((adapterView, view, i, l) -> {
            SettingOption item = (SettingOption) optionView.getItemAtPosition(i);
            if(item.getName().equals("Email Address"))
            {
                Dialog dialog = new Dialog(AccountSetting.this);
                dialog.setContentView(R.layout.custom_edit_box);

                // Find the close button and input fields
                ImageView closeButton = dialog.findViewById(R.id.close_button);
                EditText oldEmailInput = dialog.findViewById(R.id.old_input);
                EditText newEmailInput = dialog.findViewById(R.id.new_input);
                Button confirm = dialog.findViewById(R.id.confirm_button);
                // Close button listener
                closeButton.setOnClickListener(v -> {
                    //Code to change email will come here

                    dialog.dismiss();  // Close the dialog
                });
                String oldEmail = oldEmailInput.getText().toString();
                String newEmail = newEmailInput.getText().toString();
                // Confirm button Listner
                confirm.setOnClickListener(view1 -> {
                    dialog.dismiss();
                });

                // Show the dialog
                dialog.show();
            }
            else if(item.getName().equals("Change Password"))
            {
                Dialog dialog = new Dialog(AccountSetting.this);
                dialog.setContentView(R.layout.custom_edit_box);

                // Find the close button and input fields
                ImageView closeButton = dialog.findViewById(R.id.close_button);
                EditText oldPasswordInput = dialog.findViewById(R.id.old_input);
                EditText newPasswordInput = dialog.findViewById(R.id.new_input);
                TextView oldText = dialog.findViewById(R.id.textview_1);
                TextView newText = dialog.findViewById(R.id.textview2);
                Button confirm = dialog.findViewById(R.id.confirm_button);
                // Close button listener
                closeButton.setOnClickListener(v -> {
                    //Code to change email will come here

                    dialog.dismiss();  // Close the dialog
                });
                oldText.setText(R.string.old_password);
                newText.setText(R.string.new_password);
                oldPasswordInput.setHint("Enter old password");
                newPasswordInput.setHint("Enter new password");
                // Close button listener
                closeButton.setOnClickListener(v -> {
                    dialog.dismiss();  // Close the dialog
                });
                String oldEmail = oldPasswordInput.getText().toString();
                String newEmail = newPasswordInput.getText().toString();

                // Show the dialog
                dialog.show();
            }
            else if(item.getName().equals("Delete Account"))
            {
                Toast.makeText(AccountSetting.this, "Working", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountSetting.this);
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete the account?");
                builder.setPositiveButton("Yes", (dialogInterface, i1) -> {

                });
                builder.setNegativeButton("Cancel", (dialogInterface, i12) -> dialogInterface.dismiss());
                AlertDialog dialog = builder.create();

// Modify the button colors when the dialog is shown
                dialog.setOnShowListener(dialogInterface -> {
                    // Get the buttons
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    // Set custom colors for the buttons
                    positiveButton.setTextColor(getResources().getColor(android.R.color.holo_red_light)); // Red color for "Yes"
                    negativeButton.setTextColor(getResources().getColor(android.R.color.holo_blue_light)); // Blue color for "Cancel"
                });

                dialog.show();
            }
        });
    }
}