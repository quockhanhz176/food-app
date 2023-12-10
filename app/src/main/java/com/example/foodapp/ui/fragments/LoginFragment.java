package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentLoginBinding;
import com.example.foodapp.ui.util.Utils;
import com.example.foodapp.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel viewModel;
    private Runnable onLoginSuccess;

    public void setShowSignUp(Runnable showSignUp) {
        this.showSignUp = showSignUp;
    }

    Runnable showSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(null);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                if (onLoginSuccess != null && getActivity() != null) {
                    getActivity().runOnUiThread(onLoginSuccess);
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        String fullString = getString(R.string.dont_have_an_account_sign_up);
        String partString = getString(R.string.sign_up);
        SpannableString spannableString = Utils.setColorString(fullString, partString,
                requireContext(), R.color.orange);
        binding.tvSignUp.setText(spannableString);
        binding.tvSignUp.setOnClickListener(view -> {
            if (showSignUp != null) {
                showSignUp.run();
            }
        });
        binding.btLogin.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            if (validateEditText()) {
                viewModel.login(email, password);
            }
        });
    }

    private boolean validateEditText() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            binding.edtEmail.setError("Enter email");
            return false;
        } else if (!Utils.isValidEmail(email)) {
            binding.edtEmail.setError("Incorrect format email");
            return false;
        } else if (password.isEmpty()) {
            binding.edtPassword.setError("Enter password");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Runnable getOnLoginSuccess() {
        return onLoginSuccess;
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
}