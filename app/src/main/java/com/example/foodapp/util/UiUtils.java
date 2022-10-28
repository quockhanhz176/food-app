package com.example.foodapp.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;


public class UiUtils {

    public SpannableString setColorString(String fullString, String partString, Context context, int color) {
        SpannableString spannableString = new SpannableString(fullString);
        int indexStart = fullString.indexOf(partString);
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(ContextCompat.getColor(context, color));
        spannableString.setSpan(foregroundSpan, indexStart, fullString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}


