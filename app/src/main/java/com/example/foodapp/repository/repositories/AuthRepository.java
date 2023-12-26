package com.example.foodapp.repository.repositories;

import com.example.foodapp.repository.firebase.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseRepository firebaseRepository;
    private final BehaviorSubject<FirebaseUser> userSubject = BehaviorSubject.create();

    @Inject
    public AuthRepository(FirebaseRepository firebaseRepository, FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseRepository = firebaseRepository;
    }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            firebaseRepository.saveNewUser(email, saveUserResult -> {
                if (saveUserResult.isSuccessful()) {
                    userSubject.onNext(firebaseAuth.getCurrentUser());
                }
            });
        });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            userSubject.onNext(firebaseAuth.getCurrentUser());
        });
    }

    public void logout() {
        firebaseAuth.signOut();
        userSubject.onNext(null);
    }

    public BehaviorSubject<FirebaseUser> getUserSubject() {
        return userSubject;
    }

}
