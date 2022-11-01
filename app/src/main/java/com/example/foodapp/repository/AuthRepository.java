package com.example.foodapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.firebase.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final Application application;

    private final FirebaseAuth firebaseAuth;
    private final FirebaseRepository firebaseRepository;

    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthRepository(Application application) {
        this.application = application;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRepository = FirebaseRepository.getRealtimeDatabaseInstance();

        userMutableLiveData = new MutableLiveData<>();
    }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseRepository.saveNewUser(email, () -> userMutableLiveData.postValue(firebaseAuth.getCurrentUser()));
            } else {
                Toast.makeText(application, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            } else {
                Toast.makeText(application, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout() {
        firebaseAuth.signOut();
        userMutableLiveData.postValue(null);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
