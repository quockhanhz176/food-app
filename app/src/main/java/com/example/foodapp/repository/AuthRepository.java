package com.example.foodapp.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.firebase.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseRepository firebaseRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRepository = FirebaseRepository.getRealtimeDatabaseInstance();
        userMutableLiveData = new MutableLiveData<>();
    }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            firebaseRepository.saveNewUser(email,
                    saveUserResult -> {
                        if (saveUserResult.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                    });
        });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
        });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        userMutableLiveData.postValue(null);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
