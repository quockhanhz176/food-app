package com.example.foodapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    @Inject
    public AuthViewModel(AuthRepository authRepository) {

        this.authRepository = authRepository;
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public void register(String email, String password) {
        authRepository.register(email, password);
    }

    public void login(String email, String password) {
        authRepository.login(email, password);
    }

    public void logout() {
        authRepository.logout();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
