package com.iiestudents.prog7313_poe

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddPersonalBudget : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_personal_budget)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnToBudgetMenu : Button = findViewById(R.id.btnToBudgetMenu)

        btnToBudgetMenu.setOnClickListener()
        {
            val intent : Intent = Intent (this, BudgetMenu::class.java)
            startActivity(intent)
        }

        //Dates storing:
        var dueDate: String = ""

        //Date buttons:
        val btnDueDate : Button = findViewById(R.id.btnDueDate)

        //Chan (2023) Calendars:
        val calendar = Calendar.getInstance()

        //Chan (2023) DatepickerDialog1 configure:
        fun showDatePicker1(){
            val datePickerDialog = DatePickerDialog(
                this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    //Chan (2023) Create a new Calendar instance to hold the selected date
                    val selectedDate = Calendar.getInstance()
                    //Chan (2023) Set the selected date using the values received from the DatePicker dialog
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    //Chan (2023) Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    //Chan (2023) Format the selected date into a string
                    val formattedDate = dateFormat.format(selectedDate.time)
                    //Chan (2023) Update the startDate for storage:
                    dueDate = formattedDate
                },
                //using calendar1
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }



    }

}