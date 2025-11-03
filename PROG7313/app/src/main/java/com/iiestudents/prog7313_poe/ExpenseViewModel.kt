package com.iiestudents.prog7313_poe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpenseViewModel() : ViewModel() {
    //creating the functions for users (see Room Database Android Studio Kotlin Example Tutorial, 2022):
    suspend fun getAllExpense(): List<Expense> {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        val userID = user.uid
        Log.d("UserIDCheck", "UserID: $userID")
        //the instance
        val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Expenses").child(userID)
        try {
            //using await since we need to wait for the database to return the data (Firebase, 2025):
            val snapshot = firebase.get().await()
            val expenses = mutableListOf<Expense>()
            // getting the categories for the specific user (Firebase, 2025):
            for (child in snapshot.children) {
                val eid = child.child("eid").getValue(String::class.java)
                val cid = child.child("cid").getValue(String::class.java)
                val name = child.child("expenseName").getValue(String::class.java)
                val amount = child.child("amount").getValue(Double::class.java)
                val startDate = child.child("startDate").getValue(String::class.java)
                val endDate = child.child("endDate").getValue(String::class.java)
                val description = child.child("description").getValue(String::class.java)
                if (eid != null && cid != null && name != null && amount != null && startDate != null && endDate != null && description != null) {
                    val expense = Expense(eid, cid, name, amount, startDate, endDate, description)
                    expenses.add(expense)
                    Log.d("expenseAdded", "$expense")
                } else {
                    Log.w("expenseSkipped", "Missing fields in snapshot: ${child.key}")
                }
            }
            return expenses
        }
        catch (e: Exception) {
            return emptyList()
        }
    }

    suspend fun getMonthlyTotalExpense(start: String, end : String): Double {
        //using firebase auth to get current signed in user (Firebase, 2025):

        val user = FirebaseAuth.getInstance().currentUser ?: return 0.0
        val userID = user.uid
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var strDate = LocalDate.parse(start, dateFormat)
        var eDate = LocalDate.parse(end, dateFormat)
        Log.d("UserIDCheck", "UserID: $userID")
        //the instance
        val firebase =
            FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Expenses").child(userID)
        try {
            //using await since we need to wait for the database to return the data (Firebase, 2025):
            val snapshot = firebase.get().await()
            var expenses = 0.0
            // getting the categories for the specific user (Firebase, 2025):
            for (child in snapshot.children) {
                val startDate = LocalDate.parse(
                    child.child("startDate").getValue(String::class.java),
                    dateFormat
                )
                val endDate =
                    LocalDate.parse(child.child("endDate").getValue(String::class.java), dateFormat)

                val amount = child.child("amount").getValue(Double::class.java)
                if (amount != null && startDate != null && endDate != null) {
                    if (startDate.isAfter(strDate) && endDate.isBefore(eDate) || startDate.isEqual (strDate) || endDate.isEqual (eDate)) {
                        expenses = expenses + amount
                    }

                } else {
                    Log.w("expenseSkipped", "Missing fields in snapshot: ${child.key}")
                }
            }
            return expenses
        } catch (e: Exception) {
            return 0.0
        }
    }


    fun addExpense(expense: Expense)
    {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userID = user.uid
            //creating firebase database node for categories (Mohsen Mashkour, 2023):
            val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Expenses")
                //making the data stored under a specific user
                .child(userID)
            //creating unique key for each expense (Mohsen Mashkour, 2023):
            expense.eid = firebase.push().key!!
            firebase.child(expense.eid).setValue(expense)
        }
    }

    suspend fun getTotalCategoryExpense(start: String, end : String, categoryID: String): Float {
        //using firebase auth to get current signed in user (Firebase, 2025):

        val user = FirebaseAuth.getInstance().currentUser ?: return 0F
        val userID = user.uid
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var strDate = LocalDate.parse(start, dateFormat)
        var eDate = LocalDate.parse(end, dateFormat)
        Log.d("UserIDCheck", "UserID: $userID")
        //the instance
        val firebase = FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Expenses").child(userID)
        try {
            //using await since we need to wait for the database to return the data (Firebase, 2025):
            val snapshot = firebase.get().await()
            var expenses = 0F
            // getting the categories for the specific user (Firebase, 2025):
            for (child in snapshot.children) {
                val startDate = LocalDate.parse(child.child("startDate").getValue(String::class.java), dateFormat)
                val endDate = LocalDate.parse(child.child("endDate").getValue(String::class.java), dateFormat)
                val cid = child.child("cid").getValue(String::class.java)
                val amount = child.child("amount").getValue(Double::class.java)
                if (amount != null && startDate != null && endDate != null )
                {
                    if(startDate.isAfter(strDate) && endDate.isBefore(eDate) || startDate.isEqual(strDate) || endDate.isEqual(eDate)) {
                        if( cid == categoryID) {
                            Log.d(
                                "CategoryData",
                                "Child data: ${child.value}  child key: ${child.key}"
                            )
                            expenses = expenses + amount.toFloat()
                        }
                    }

                } else {
                    Log.w("expenseSkipped", "Missing fields in snapshot: ${child.key}")
                }
            }
            return expenses
        }
        catch (e: Exception) {
            return 0F
        }
    }

    suspend fun getCategory(cID: String) : List<Expense> {
        //using firebase auth to get current signed in user (Firebase, 2025):
        val user = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        val userID = user.uid
        //the instance
        val firebase =
            FirebaseDatabase.getInstance("https://ezbbudgetapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Expenses").child(userID)

        try {
            //using await since we need to wait for the database to return the data (Firebase, 2025):
            val snapshot = firebase.get().await()
            val expenses = mutableListOf<Expense>()
            // getting the categories for the specific user (Firebase, 2025):
            for (child in snapshot.children) {
                val eid = child.child("eid").getValue(String::class.java)
                val cid = child.child("cid").getValue(String::class.java)
                val name = child.child("expenseName").getValue(String::class.java)
                val amount = child.child("amount").getValue(Double::class.java)
                val startDate = child.child("startDate").getValue(String::class.java)
                val endDate = child.child("endDate").getValue(String::class.java)
                val description = child.child("description").getValue(String::class.java)
                if (eid != null && cid != null && name != null && amount != null && startDate != null && endDate != null && description != null && cid == cID) {
                    val expense = Expense(eid, cid, name, amount, startDate, endDate, description)
                    expenses.add(expense)
                    Log.d("expenseAdded", "$expense")
                } else {
                    Log.w("expenseSkipped", "Missing fields in snapshot: ${child.key}")
                }
            }
            return expenses
        } catch (e: Exception) {
            return emptyList()
            Log.d("expense error", "Expense error: $e")
        }
    }

//    suspend fun getImage(uri:String) : Expense?
//    {
//        return expenseRepository.getImage(uri)
//    }
}

//reference list:

//Mohsen Mashkour, 2023. How to send data to the Firebase Realtime database Android studio Kotlin. [video online]. Available at: https://www.youtube.com/watch?v=3XiZF1UBn50&list=PLEGrY4uRTu5ls7Mq7h6RcdKGFdQVqy0KZ&index=2&ab_channel=MohsenMashkour [Accessed 22 May 2025].

//Firebase. 2025. Read and Write Data on Android. [Online]. Available at: https://firebase.google.com/docs/database/android/read-and-write#kotlin_7 [Accessed 5 June 2025].

//Firebase. 2025. Authenticate with Firebase using Password-Based Accounts on Android. [Online]. Available at: https://firebase.google.com/docs/auth/android/password-auth [Accessed 23 May 2025].