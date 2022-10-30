package com.example.foodapp.viewmodel.utils;

import java.io.*;
import java.security.*;

// References: http://en.gravatar.com/site/implement/images/java/
public class Gravatar {
    public static final String GRAVATAR_BASE_URL = "https://www.gravatar" +
            ".com/avatar/";

    public static final String GRAVATAR_DEFAULT_CONFIG = "?s=64&d=identicon";

    private String email;

    public Gravatar(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return GRAVATAR_BASE_URL + MD5Util.md5Hex(this.email) + GRAVATAR_DEFAULT_CONFIG;
    }
}