package com.example.project;

public class SettingOption {
    private String name;

    private int iconResId;

    public SettingOption(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}
