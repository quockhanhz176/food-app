package com.example.foodapp.ui.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


public class Utils {

    static public SpannableString setColorString(String fullString, String partString, Context context, int color) {
        SpannableString spannableString = new SpannableString(fullString);
        int indexStart = fullString.indexOf(partString);
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(ContextCompat.getColor(context, color));
        spannableString.setSpan(foregroundSpan, indexStart, fullString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    static public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    static public void clearAllFragments(FragmentManager manager) {
        try {
            for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                manager.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


