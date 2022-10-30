package com.example.foodapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> isLogoutLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
        isLogoutLiveData = authRepository.getIsLogoutLiveData();
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

    public MutableLiveData<Boolean> getIsLogoutLiveData() {
        return isLogoutLiveData;
    }
}
