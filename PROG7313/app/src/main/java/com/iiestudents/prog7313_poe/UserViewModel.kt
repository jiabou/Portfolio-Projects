package com.iiestudents.prog7313_poe

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import kotlinx.coroutines.tasks.await


class UserViewModel() : ViewModel() {
    private lateinit var auth: FirebaseAuth

    //adding function to create a user (Firebase, 2025):
    suspend fun addUser(email: String, password: String): Boolean {
        auth = FirebaseAuth.getInstance()
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("Create acc", "created user successfully")
            true
        } catch (e: Exception) {
            Log.d("Create acc", "create user fail: ${e.message}")
            false
        }
    }

    //added function to log user in through firebase auth (Firebase, 2025):
    suspend fun login(email: String, password: String): Boolean {
        try {
            auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password).await()
            return true
        } catch (e: Exception) {
            return false
            // Toast.makeText(this, "Sign up failed: ${it.message}", Toast.LENGTH_SHORT).show()
            Log.d("Login Fail", "$e")
        }
    }
}


//reference list:

//Firebase. 2025. Authenticate with Firebase using Password-Based Accounts on Android. [Online]. Available at: https://firebase.google.com/docs/auth/android/password-auth [Accessed 23 May 2025].