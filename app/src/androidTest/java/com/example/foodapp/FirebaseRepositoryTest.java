package com.example.foodapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.foodapp.repository.firebase.FirebaseRepository;
import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirebaseRepositoryTest {

    private FirebaseRepository firebaseRepository;

    @Test
    public void testConnection() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);

        this.firebaseRepository = FirebaseRepository.getRealtimeDatabaseInstance();

        assertNotEquals(firebaseRepository, null);
    }

    @Test
    public void testSaveNewUser() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);

        this.firebaseRepository = FirebaseRepository.getRealtimeDatabaseInstance();

        String email = "testing@test.com";
        firebaseRepository.saveNewUser(email, (task) -> assertTrue(task.isSuccessful()));
    }

    @Test public void testFetchUser() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);

        this.firebaseRepository = FirebaseRepository.getRealtimeDatabaseInstance();

        String email = "testing@test.com";
        firebaseRepository.fetchUser(email, (user -> {
            assertNotEquals(user, null);
            assertEquals(user.getEmail(), email);
        }));
    }
}