package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodapp.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserSettingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_setting, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
