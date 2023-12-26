package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import autodispose2.androidx.lifecycle.scope
import autodispose2.autoDispose
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentLoginBinding
import com.example.foodapp.ui.util.Utils
import com.example.foodapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@AndroidEntryPoint
class LoginFragment : Fragment() {

    var onLoginSuccess: Runnable? = null
    var showSignUp: Runnable? = null

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels({ requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        viewModel.userSubject.observeOn(AndroidSchedulers.mainThread())
            .autoDispose(viewLifecycleOwner.scope())
            .subscribe { firebaseUser: FirebaseUser? ->
                if (firebaseUser != null) {
                    if (onLoginSuccess != null && activity != null) {
                        onLoginSuccess
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        val fullString = getString(R.string.dont_have_an_account_sign_up)
        val partString = getString(R.string.sign_up)
        val spannableString =
            Utils.setColorString(fullString, partString, requireContext(), R.color.orange)
        binding.tvSignUp.text = spannableString
        binding.tvSignUp.setOnClickListener {
            if (showSignUp != null) {
                showSignUp!!.run()
            }
        }
        binding.btLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim { it <= ' ' }
            val password = binding.edtPassword.text.toString().trim { it <= ' ' }
            if (validateEditText()) {
                viewModel.login(email, password)
            }
        }
    }

    private fun validateEditText(): Boolean {
        val email = binding.edtEmail.text.toString().trim { it <= ' ' }
        val password = binding.edtPassword.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
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