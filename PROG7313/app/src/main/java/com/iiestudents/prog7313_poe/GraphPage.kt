package com.iiestudents.prog7313_poe

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import android.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.snapshot.Index
import kotlinx.coroutines.launch

class GraphPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graph_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val btnBackToBudgetMenu : Button = findViewById(R.id.btnBackToBudgetMenu)

        btnBackToBudgetMenu.setOnClickListener()
        {
            val intent : Intent = Intent (this, BudgetMenu::class.java)
            startActivity(intent)
        }

        //Dates storing:
        var startDate: String = ""
        var endDate: String = ""

        //Date buttons:
        val btnStartSpendingDate : Button = findViewById(R.id.btnStartSpendingDate)
        val btnEndSpendingDate : Button = findViewById(R.id.btnEndSpendingDate)

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

        //Start Button:
        btnStartSpendingDate.setOnClickListener {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker1()
        }

        //End Button:
        btnEndSpendingDate.setOnClickListener()
        {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker2()
        }

        val btnConfrimSpendingDate : Button = findViewById(R.id.btnConfrimSpendingDate)

        //Easy One Coder (2023):
        fun setupPieChart(pieChart: PieChart) {
            val expenseViewModel = ExpenseViewModel()
            val categoryViewModel = CategoryViewModel()
            val budgetGoalViewModel = BudetGoalViewModel()

            lifecycleScope.launch {
                val allExpense = categoryViewModel.allCategories()
                var entries: MutableList<PieEntry> = mutableListOf()
                val budgetGoal = budgetGoalViewModel.getBudgetGoals()
                var minGoal = 0F
                var maxGoal = 0F

                if (budgetGoal.isNotEmpty()) {
                    minGoal = budgetGoal[0].minGoal.toFloat()
                    maxGoal = budgetGoal[0].maxGoal.toFloat()
                }
                entries.add(PieEntry(minGoal.toFloat(), "Minimum Budget Goal"))
                entries.add(PieEntry(maxGoal.toFloat(), "Maximum Budget Goal"))
                entries.add(PieEntry(3000f, "Income"))

                //Easy One Coder (2023) adding pie entries:
                if(startDate.isNotEmpty() && endDate.isNotEmpty()){
                    for (category in allExpense) {
                        var totalCategory = expenseViewModel.getTotalCategoryExpense(startDate, endDate, category.cID.toString())
                        if(totalCategory > 0){
                            entries.add(PieEntry(totalCategory, category.categoryName))
                        }

                    }

                }

                //Easy One Coder (2023) adding pie chart layout:
                val dataSet = PieDataSet(entries, "Income, expenses, and goals")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextColor = Color.WHITE
                dataSet.valueTextSize = 16f

                val pieData = PieData(dataSet)

                pieChart.data = pieData
                pieChart.setUsePercentValues(false)
                pieChart.setDrawHoleEnabled(true)
                pieChart.setHoleColor(Color.TRANSPARENT)
                pieChart.setEntryLabelColor(Color.BLACK)
                pieChart.centerText = "Monthly Budget"
                pieChart.setCenterTextSize(18f)
                pieChart.description.isEnabled = false
                pieChart.legend.isEnabled = true

                pieChart.invalidate() // Refreshs the pie chart
            }
        }

        btnConfrimSpendingDate.setOnClickListener()
        {

            //Easy One Coder (2023):
            val pieChart = findViewById<PieChart>(R.id.pieChart)
            setupPieChart(pieChart)
        }



    }
}

//Reference list:
//Chan, D. 2023. Date Picker Using Kotlin in Android Studio | DatePickerDialog — Android Studio Tutorial | Kotlin, Medium, 3 August 2023. [Blog]. Available at: https://devendrac706.medium.com/date-picker-using-kotlin-in-android-studio-datepickerdialog-android-studio-tutorial-kotlin-3bbc606585a [Accessed 29 April 2025].

//Easy One Coder, 2023. how to create pie chart | MP Android Chart | Android Studio 2024. [video online]. Available at: https://www.youtube.com/watch?v=fsVdzURuo_Y [Accessed 7 June 2025].