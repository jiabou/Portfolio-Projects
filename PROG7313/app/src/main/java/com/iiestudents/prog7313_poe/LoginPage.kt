package com.iiestudents.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         val btnBack: Button = findViewById(R.id.btnLogBack)
        val btnSignIn: Button = findViewById(R.id.btnSignIn)
        val txtLogUserName: EditText = findViewById(R.id.txtLogUsername)
        val txtPassword: EditText = findViewById(R.id.txtLogPassword)
        val txtLoginFail: TextView = findViewById(R.id.txtLoginFail)
        val userViewModel = UserViewModel()
        //back btn
        btnBack.setOnClickListener{
            val intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener()
        {
            val username: String = txtLogUserName.text.toString()
            val password: String = txtPassword.text.toString()
            val intent: Intent = Intent(this, HomePage::class.java)
            // Perform the login check
            try {
                lifecycleScope.launch {
                    val loginSuccess = userViewModel.login(username, password)

                    if (loginSuccess) {
                        startActivity(intent)
                    } else {
                        txtLoginFail.text = "Incorrect username or password"
                    }
                }
            }
            catch(e:Exception)
            {
                Log.d("error", e.message.toString())
            }
        }
    }
}