package com.iiestudents.prog7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Firebase.auth.signOut()
        val btnPlayAgain : Button = findViewById(R.id.btnLogin)

        btnPlayAgain.setOnClickListener()
        {
            val intent : Intent = Intent (this, LoginPage::class.java)
            startActivity(intent)
        }

        val btnPlayAgain2 : Button = findViewById(R.id.btnSignUp)

        btnPlayAgain2.setOnClickListener()
        {
            val intent : Intent = Intent (this, SignUpPage::class.java)
            startActivity(intent)
        }
    }
}