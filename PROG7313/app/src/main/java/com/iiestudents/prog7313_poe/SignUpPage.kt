package com.iiestudents.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class SignUpPage : AppCompatActivity() {
    //create var for firebase auth (Firebase, 2025):
    private lateinit var  auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userViewModel = UserViewModel()
        val btnBack: Button = findViewById(R.id.btnRegBack)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val txtEmail: EditText = findViewById(R.id.txtEmail)
        val txtPassword: EditText = findViewById(R.id.txtPassword)
        val txtConfirmPassword: EditText = findViewById(R.id.txtConfirmPassword)
        val txtErrorDisplay: TextView = findViewById(R.id.txtErrorDisplay)
        //btn to go back
        btnBack.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //btn to create account
        btnRegister.setOnClickListener()
        {
            try {
                //data validation - making sure all fields are entered (Firebase, 2025):
                if(txtPassword.text.isNotEmpty() && txtEmail.text.isNotEmpty() && txtEmail.text.isNotEmpty() && txtConfirmPassword.text.isNotEmpty()  &&txtEmail.text.contains("@"))
                {
                    //making sure passwords match
                    if (txtPassword.text.toString() == txtConfirmPassword.text.toString())
                    {
                        var password = txtPassword.text.toString()
                        var email = txtEmail.text.toString()
                        var flag : Boolean = false
                        val intent: Intent = Intent(this, HomePage::class.java)
                        //creating account using addUser method in userViewModel class
                        lifecycleScope.launch {
                            flag = userViewModel.addUser(email, password)

                            if (flag == true) {
                                startActivity(intent)
                                txtErrorDisplay.visibility = View.VISIBLE
                                txtErrorDisplay.text = "successfully created"
                            } else {
                                txtErrorDisplay.visibility = View.VISIBLE
                                txtErrorDisplay.text = "Password must be 6 characters long including 1 number"
                            }
                        }

                    }
                    else
                    {
                        txtErrorDisplay.visibility = View.VISIBLE
                        txtErrorDisplay.text = "Passwords are not the same"
                    }
                }
                else
                {
                    txtErrorDisplay.visibility = View.VISIBLE
                    txtErrorDisplay.text = "please enter a value for all fields. Make sure to use a valid email please"
                }
            }
            catch(e : Exception)
            {
                println(e.message)
            }
        }
    }
}

//reference list:

//Firebase. 2025. Authenticate with Firebase using Password-Based Accounts on Android. [Online]. Available at: https://firebase.google.com/docs/auth/android/password-auth [Accessed 22 May 2025].