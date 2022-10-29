package com.example.foodapp.firebase.entity;

import com.example.foodapp.viewmodel.utils.Gravatar;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String email;
    private boolean isFirstLogin;
    private String avatar;

    private UserPreference userPreference;

    public User(String email, String avatar, UserPreference userPreference) {
        this.email = email;
        isFirstLogin = true;

        if(avatar == null || avatar.trim().equals("")) {
            Gravatar gravatar = new Gravatar(email);
            this.avatar = gravatar.getAvatarUrl();
        } else {
            this.avatar = avatar;
        }

        if(userPreference == null) {
            this.userPreference = new UserPreference();
        }
    }

    public String getEmail() {
        return email;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public String getAvatar() {
        return avatar;
    }
}
