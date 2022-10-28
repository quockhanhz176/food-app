package com.example.foodapp.ui.sign_up;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentSignUpBinding;
import com.example.foodapp.ui.login.LoginFragment;
import com.example.foodapp.util.UiUtils;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        String fullString = getString(R.string.already_have_an_account_login);
        String partString = getString(R.string.login);
        SpannableString spannableString = new UiUtils().setColorString(fullString, partString, requireContext(), R.color.orange);
        binding.tvLogin.setText(spannableString);
        binding.tvLogin.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LoginFragment(), SignUpFragment.class.getCanonicalName()).addToBackStack(SignUpFragment.class.getCanonicalName()).commit();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}