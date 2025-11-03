package com.iiestudents.prog7313_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BudgetMenu : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBackToHome : Button = findViewById(R.id.btnBackToHome)

        btnBackToHome.setOnClickListener()
        {
            val intent : Intent = Intent (this, HomePage::class.java)
            startActivity(intent)
        }

        val btnGraphPage : Button = findViewById(R.id.btnGraphPage)

        btnGraphPage.setOnClickListener()
        {
            val intent : Intent = Intent (this, GraphPage::class.java)
            startActivity(intent)
        }

        val btnMyBudgetPage : Button = findViewById(R.id.btnMyBudgetPage)

        btnMyBudgetPage.setOnClickListener()
        {
            val intent : Intent = Intent (this, MyBudgetPage::class.java)
            startActivity(intent)
        }

        val btnViewBudgetGoals : Button = findViewById(R.id.btnViewBudgetGoals)

        btnViewBudgetGoals.setOnClickListener()
        {
            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }



    }
}