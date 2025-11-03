package com.iiestudents.prog7313_poe

import android.content.Intent
import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class CategoryViewModel () : ViewModel() {

    suspend fun allCategories(): List<Category>  {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        val userID = user.uid
        //the instance
        val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Categories").child(userID)
        try {
            //using await since we need to wait for the database to return the data (Firebase, 2025):
            val snapshot = firebase.get().await()
            val categories = mutableListOf<Category>()
            // getting the categories for the specific user (Firebase, 2025):
            for (child in snapshot.children) {
                Log.d("CategoryData", "Child data: ${child.value}")
                val category = child.getValue(Category::class.java)
                if (category != null) {
                    categories.add(category)
                }
            }
            return categories
        }
        catch (e: Exception)
        {
            Log.d("Error","Spinner failed : $e")
            return emptyList()
        }
    }

    fun insertCategory(categoryName: String) {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userID = user.uid
            //creating firebase database node for categories (Mohsen Mashkour, 2023):
            val firebase =
                FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Categories")
                    //making the data stored under a specific user (Mohsen Mashkour, 2023):
                    .child(userID)
            //creating unique key for each category
            val categoryID = firebase.push().key!!
            val category = Category(categoryID, categoryName)
            //saving to firebase (Mohsen Mashkour, 2023):
            firebase.child(categoryID).setValue(category)
        }
    }
}


//reference list:

//Mohsen Mashkour, 2023. How to send data to the Firebase Realtime database Android studio Kotlin. [video online]. Available at: https://www.youtube.com/watch?v=3XiZF1UBn50&list=PLEGrY4uRTu5ls7Mq7h6RcdKGFdQVqy0KZ&index=2&ab_channel=MohsenMashkour [Accessed 22 May 2025].

//Firebase. 2025. Authenticate with Firebase using Password-Based Accounts on Android. [Online]. Available at: https://firebase.google.com/docs/auth/android/password-auth [Accessed 23 May 2025].

//Firebase. 2025. Read and Write Data on Android.[Online]. Available at: https://firebase.google.com/docs/database/android/read-and-write [Accessed 23 May 2025].