package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import autodispose2.androidx.lifecycle.scope
import autodispose2.autoDispose
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentSignUpBinding
import com.example.foodapp.ui.util.Utils
import com.example.foodapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        setupView()

        viewModel.userSubject.observeOn(AndroidSchedulers.mainThread())
            .autoDispose(viewLifecycleOwner.scope())
            .subscribe { firebaseUser: FirebaseUser? ->
                if (firebaseUser != null && activity != null) {
                    findNavController().popBackStack(R.id.main, false)
                    Toast.makeText(
                        context,
                        "Thank you for signing up. Have fun!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

        return binding.root
    }

    private fun setupView() {
        val fullString = getString(R.string.already_have_an_account_login)
        val partString = getString(R.string.login)
        val spannableString =
            Utils.setColorString(fullString, partString, requireContext(), R.color.orange)
        binding.tvLogin.text = spannableString
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignupToLogin())
        }
        binding.cvBack.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignupToLogin())
        }
        binding.btSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim { it <= ' ' }
            val password = binding.edtPassword.text.toString().trim { it <= ' ' }
            if (validateEditText()) {
                viewModel.register(email, password)
            }
        }
    }

    private fun validateEditText(): Boolean {
        val name = binding.edtName.text.toString().trim { it <= ' ' }
        val email = binding.edtEmail.text.toString().trim { it <= ' ' }
        val password = binding.edtPassword.text.toString().trim { it <= ' ' }
        if (name.isEmpty()) {
            binding.edtName.error = "Enter name"
            return false
        } else if (email.isEmpty()) {
            binding.edtEmail.error = "Enter email"
            return false
        } else if (!Utils.isValidEmail(email)) {
            binding.edtEmail.error = "Incorrect format email"
            return false
        } else if (password.isEmpty()) {
            binding.edtPassword.error = "Enter password"
            return false
        }
        return true
    }
}