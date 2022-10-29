package com.example.foodapp.viewmodel.utils;

import java.io.*;
import java.security.*;

// References: http://en.gravatar.com/site/implement/images/java/
class MD5Util {

    public static final String GRAVATAR_BASE_URL = "https://www.gravatar" +
            ".com/avatar/";

    public static final String GRAVATAR_DEFAULT_CONFIG = "?s=64&d=identicon";

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }
}

public class Gravatar {
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
        return MD5Util.GRAVATAR_BASE_URL + MD5Util.md5Hex(this.email) + MD5Util.GRAVATAR_DEFAULT_CONFIG;
    }
}