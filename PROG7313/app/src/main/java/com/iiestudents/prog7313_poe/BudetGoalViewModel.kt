package com.iiestudents.prog7313_poe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BudetGoalViewModel() : ViewModel() {

    //adding functions that work with the repository functions (see Room Database Android Studio Kotlin Example Tutorial, 2022):
    fun addGoal(budgetGoal: BudgetGoal) : String
    {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
        {
            val userID = user.uid
            //creating firebase database node for categories (Mohsen Mashkour, 2023):
            val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("BudgetGoals")
                //making the data stored under a specific user (Mohsen Mashkour, 2023):
                .child(userID)
            //creating unique key for each category
            val budgetID = firebase.push().key!!
            budgetGoal.bID = budgetID
            //saving to firebase (Mohsen Mashkour, 2023):
            firebase.child(budgetID).setValue(budgetGoal)
            return budgetID
        }
        return ""
    }

    suspend fun getBudgetGoals(): List<BudgetGoal>
    {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        val userID = user.uid
        //the instance
        val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("BudgetGoals").child(userID)
        try {
            //using await since we need to wait for the database to return the data (Firebase, 2025):
            val snapshot = firebase.get().await()
            val budgets = mutableListOf<BudgetGoal>()
            // getting the categories for the specific user (Firebase, 2025):
            for (child in snapshot.children) {
                Log.d("TotalMonthlyCategory", "Child data: ${child.value}  child key: ${child.key}")
                val minGoal = child.child("minGoal").getValue(Double::class.java)
                val maxGoal = child.child("maxGoal").getValue(Double::class.java)
                val bid = child.child("bid").getValue(String::class.java)
                if (minGoal != null && bid != null && maxGoal != null) {
                    val budget = BudgetGoal(bid,minGoal,maxGoal)
                    budgets.add(budget)
                }
            }
            return budgets
        }
        catch (e: Exception)
        {
            Log.d("Error","BudgetGoal: $e")
            return emptyList()
        }
    }

    suspend fun updateBudgetGoal(budget: BudgetGoal): Boolean {
        val user = FirebaseAuth.getInstance().currentUser ?: return false
        val userID = user.uid
        val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("BudgetGoals").child(userID).child(budget.bID)

        return try {
            firebase.setValue(budget).await()
            true
        } catch (e: Exception) {
            // Log or handle the exception
            false
        }
    }
}


//reference list:

//Mohsen Mashkour, 2023. How to send data to the Firebase Realtime database Android studio Kotlin. [video online]. Available at: https://www.youtube.com/watch?v=3XiZF1UBn50&list=PLEGrY4uRTu5ls7Mq7h6RcdKGFdQVqy0KZ&index=2&ab_channel=MohsenMashkour [Accessed 22 May 2025].

//Firebase. 2025. Authenticate with Firebase using Password-Based Accounts on Android. [Online]. Available at: https://firebase.google.com/docs/auth/android/password-auth [Accessed 23 May 2025].