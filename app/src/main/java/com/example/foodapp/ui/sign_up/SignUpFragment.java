package com.example.foodapp.ui.sign_up;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentSignUpBinding;
import com.example.foodapp.ui.home.HomeFragment;
import com.example.foodapp.ui.login.LoginFragment;
import com.example.foodapp.util.Utils;
import com.example.foodapp.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private AuthViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(requireContext(), "Create account successfully", Toast.LENGTH_SHORT).show();
                showHomeFragment();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        String fullString = getString(R.string.already_have_an_account_login);
        String partString = getString(R.string.login);
        SpannableString spannableString = new Utils().setColorString(fullString, partString, requireContext(), R.color.orange);
        binding.tvLogin.setText(spannableString);
        binding.tvLogin.setOnClickListener(view -> {
            showLoginFragment();
        });

        binding.btSignUp.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            if (validateEditText()) {
                viewModel.register(email, password);
            }
        });
    }

    private boolean validateEditText() {
        String name = binding.edtName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (name.isEmpty()) {
            binding.edtName.setError("Enter name");
            return false;
        } else if (email.isEmpty()) {
            binding.edtEmail.setError("Enter email");
            return false;
        } else if (!new Utils().isValidEmail(email)) {
            binding.edtEmail.setError("Incorrect format email");
            return false;
        } else if (password.isEmpty()) {
            binding.edtPassword.setError("Enter password");
            return false;
        }
        return true;
    }


    private void showLoginFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new LoginFragment(), SignUpFragment.class.getCanonicalName()).addToBackStack(SignUpFragment.class.getCanonicalName()).commit();
    }

    private void showHomeFragment() {
        new Utils().clearAllFragment(requireActivity());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment(), HomeFragment.class.getCanonicalName()).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
