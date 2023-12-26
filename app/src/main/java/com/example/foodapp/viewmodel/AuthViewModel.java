package com.example.foodapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.foodapp.repository.repositories.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final BehaviorSubject<FirebaseUser> userSubject;

    @Inject
    public AuthViewModel(AuthRepository authRepository) {

        this.authRepository = authRepository;
        userSubject = authRepository.getUserSubject();
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

    public BehaviorSubject<FirebaseUser> getUserSubject() {
        return userSubject;
    }

}
