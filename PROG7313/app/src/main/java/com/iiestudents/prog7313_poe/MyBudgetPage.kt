package com.iiestudents.prog7313_poe

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MyBudgetPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_budget_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edMin: EditText = findViewById(R.id.edMinGoal)
        val edMax: EditText = findViewById(R.id.edMaxGoal)
        val txtMin: TextView = findViewById(R.id.txtMingoal)
        val txtMax: TextView = findViewById(R.id.txtMaxGoal)
        var existingGoal: Boolean = false

        val expenseViewModel = ExpenseViewModel()
        val categoryViewModel = CategoryViewModel()
        val budgetGoalViewModel = BudetGoalViewModel()

        var budgetGoalID = ""

        lifecycleScope.launch {
            val budgetGoal = budgetGoalViewModel.getBudgetGoals()
            if (budgetGoal.isNotEmpty()) {
                //hardcoded index for now due to only saving 1 month for now
                txtMax.text = "Max Goal: " + budgetGoal[0].maxGoal
                txtMin.text = "Min Goal: " + budgetGoal[0].minGoal
                existingGoal = true
                budgetGoalID = budgetGoal[0].bID
            }
        }
        var recyclerView = findViewById<RecyclerView>(R.id.rvExpenses)

        val btnPlayAgain : Button = findViewById(R.id.btnReturnFromPersonalBudget)

        btnPlayAgain.setOnClickListener()
        {
            val intent : Intent = Intent (this, BudgetMenu::class.java)
            startActivity(intent)
        }
//        val btnAddPersonalBudget : Button = findViewById(R.id.btnAddPersonalBudget)
//
//        btnAddPersonalBudget.setOnClickListener()
//        {
//            val intent: Intent = Intent (this, AddPersonalBudget::class.java)
//            startActivity(intent)
//        }

        val btnMinMax: Button = findViewById(R.id.btnChangeMinMax)


        btnMinMax.setOnClickListener{
            try
            {
                if(existingGoal)
                {
                    var min = edMin.text.toString().toDouble()
                    var max = edMax.text.toString().toDouble()
                    val budget = BudgetGoal(budgetGoalID,min,max)
                    lifecycleScope.launch{
                        budgetGoalViewModel.updateBudgetGoal(budget)
                    }
                    txtMax.text = "Max Goal: " + max
                    txtMin.text = "Min Goal: " + min
                }
                else
                {
                    var min = edMin.text.toString()
                    var max = edMax.text.toString()
                    if (min.isNotEmpty() && max.isNotEmpty())
                    {
                        var budgetGoal = BudgetGoal(
                            bID = "",
                            minGoal = min.toDouble(),
                            maxGoal = max.toDouble()
                        )
                        budgetGoalID = budgetGoalViewModel.addGoal(budgetGoal)
                        txtMax.text = "Max Goal: " + max
                        txtMin.text = "Min Goal: " + min
                        existingGoal = true
                    }
                    else
                    {

                    }
                }
            }
            catch (e:Exception)
            {
                println(e.message)
            }

        }
        //Dates storing:
        var startDate: String = ""
        var endDate: String = ""

        //Date buttons:
        val btnStartDate : Button = findViewById(R.id.btnStart)
        val btnEndDate : Button = findViewById(R.id.btnEnd)

        //Chan (2023) Calendars:
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()

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
                    startDate = formattedDate
                },
                //using calendar1
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        //Chan (2023) DatepickerDialog2 configure:
        fun showDatePicker2(){
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
                    //Chan (2023) Update the endDate for storage:
                    endDate = formattedDate
                },
                //using calendar2
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

//        fun persistUriPermission(uri: Uri) {
//            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
//                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//
//            try {
//                applicationContext.contentResolver.takePersistableUriPermission(uri, takeFlags)
//            } catch (e: SecurityException) {
//                e.printStackTrace()
//            }
//        }

        fun listExpenses() {
            try {
                lifecycleScope.launch {
                    val expenses: List<Expense> = expenseViewModel.getAllExpense()
                    Log.d("AllExpenses","$expenses")
                    var tempList = mutableListOf<Expense>()
                    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    recyclerView.removeAllViews()
                    for (expense in expenses) {
                        Log.d("DisplayExpenses","$expense")
                        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                            val expenseDate = LocalDate.parse(expense.startDate, dateFormat)
                            if (expenseDate.isAfter(
                                    LocalDate.parse(
                                        startDate,
                                        dateFormat
                                    )
                                ) && expenseDate.isBefore(
                                    LocalDate.parse(
                                        endDate,
                                        dateFormat
                                    )
                                ) || expenseDate.isEqual(
                                    LocalDate.parse(
                                        startDate,
                                        dateFormat
                                    )
                                ) || expenseDate.isEqual(LocalDate.parse(endDate, dateFormat))
                            ) {
                                tempList.add(expense)
                            }
                        }

                    }


                    recyclerView.layoutManager = LinearLayoutManager(this@MyBudgetPage)

                    val adapter = TaskItemAdapter(tempList)

                    recyclerView.adapter = adapter


                }
            }
            catch (e: Exception) {
                Log.d("error", e.message.toString())
            }
        }

        btnStartDate.setOnClickListener {
            showDatePicker1()

        }

        btnEndDate.setOnClickListener {
            showDatePicker2()

        }

        val btnConfrimDate : Button = findViewById(R.id.btnConfrimDate)

        btnConfrimDate.setOnClickListener {
            listExpenses()
        }
    }
}