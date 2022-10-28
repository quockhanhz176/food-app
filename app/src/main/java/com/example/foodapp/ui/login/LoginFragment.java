package com.example.foodapp.ui.login;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentLoginBinding;
import com.example.foodapp.util.UiUtils;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        String fullString = getString(R.string.dont_have_an_account_sign_up);
        String partString = getString(R.string.sign_up);
        SpannableString spannableString = new UiUtils().setColorString(fullString, partString, requireContext(), R.color.orange);
        binding.tvSignUp.setText(spannableString);
        binding.cvBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}