package com.example.foodapp.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.repository.firebase.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseRepository firebaseRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    @Inject
    public AuthRepository(FirebaseRepository firebaseRepository, FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseRepository = firebaseRepository;
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
        firebaseAuth.signOut();
        userMutableLiveData.postValue(null);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
