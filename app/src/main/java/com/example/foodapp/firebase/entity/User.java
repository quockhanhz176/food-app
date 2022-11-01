package com.example.foodapp.firebase.entity;

import com.example.foodapp.viewmodel.utils.Gravatar;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private final String email;
    private final boolean isFirstLogin;
    private final String avatar;

    private UserPreference userPreference;

    public User() {
        email = "";
        isFirstLogin = true;
        avatar = "";
        userPreference = new UserPreference();
    }

    public User(String email, String avatar, UserPreference userPreference) {
        this.email = email;
        isFirstLogin = true;

        if (avatar == null || avatar.trim().equals("")) {
            Gravatar gravatar = new Gravatar(email);
            this.avatar = gravatar.getAvatarUrl();
        } else {
            this.avatar = avatar;
        }

        if (userPreference == null) {
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

    public UserPreference getUserPreference() {
        return userPreference;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }


}
