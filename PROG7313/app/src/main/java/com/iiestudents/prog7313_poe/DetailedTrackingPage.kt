package com.iiestudents.prog7313_poe

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailedTrackingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detailed_tracking_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtOverSpent : TextView = findViewById(R.id.txtOverSpent)
        val txtlimit : TextView = findViewById(R.id.txtlimit)

        val btnDetailsBack : Button = findViewById(R.id.btnDetailsBack)

        btnDetailsBack.setOnClickListener()
        {
            val intent : Intent = Intent (this, BudgetMenu::class.java)
            startActivity(intent)
        }

        //Dates storing:
        var startDate: String = ""
        var endDate: String = ""

        //Date buttons:
        val btnStartGoalDate : Button = findViewById(R.id.btnStartGoalDate)
        val btnEndBudgetDate : Button = findViewById(R.id.btnEndGoalDate)

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

        //Easy One Coder (2023) setup:
        fun setupHorizontalBarChart(chart: HorizontalBarChart) {
            val expenseViewModel = ExpenseViewModel()
            val budgetGoalViewModel = BudetGoalViewModel()

            lifecycleScope.launch {
                var totalExpenses = 0.0

                if(startDate.isNotEmpty() && endDate.isNotEmpty()){
                    totalExpenses = expenseViewModel.getMonthlyTotalExpense(startDate,endDate)
                }

                val budgetGoal = budgetGoalViewModel.getBudgetGoals()

                var minGoal = 0F
                var maxGoal = 0F

                if (budgetGoal.isNotEmpty()) {
                    minGoal = budgetGoal[0].minGoal.toFloat()
                    maxGoal = budgetGoal[0].maxGoal.toFloat()
                }

                if (totalExpenses == null){
                    totalExpenses = 0.0

                }

                Log.d("barGraph", "Goals: $minGoal , $maxGoal" + " totalExpenses: $totalExpenses")

                //Easy One Coder (2023) adding bar entries:
                val entries = listOf(
                    BarEntry(0f, minGoal),
                    BarEntry(1f, maxGoal),
                    BarEntry(2f, totalExpenses.toFloat()),
                    BarEntry(3f, 3000f)
                )

                val labels = listOf("Min Expense", "Max Expense", "Actual Expense", "Actual Income")

                //Easy One Coder (2023) adding bar graph layout:
                val dataSet = BarDataSet(entries, "Budget Overview")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextSize = 14f

                val barData = BarData(dataSet)
                chart.data = barData

                chart.setExtraOffsets(60f, 10f, 10f, 10f)

                //Easy One Coder (2023) Customize X Axis
                val xAxis = chart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f
                xAxis.labelCount = labels.size
                xAxis.labelRotationAngle = 0f
                xAxis.textSize = 12f
                xAxis.labelRotationAngle = 0f

                //Easy One Coder (2023) Customize Y Axis
                chart.axisLeft.isEnabled = false
                chart.axisRight.axisMinimum = 0f

                chart.description.isEnabled = false
                chart.setFitBars(true)
                chart.animateY(1000)
                chart.invalidate()

                val limit = maxGoal - totalExpenses

                txtlimit.text = "Limit left: R " + limit.toString()

                if(limit > 0){
                    txtOverSpent.text = "You have not over spent."
                }
                else{
                    txtOverSpent.text = "You have over spent."
                }
            }
        }

        //Start Button:
        btnStartGoalDate.setOnClickListener {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker1()
        }

        //End Button:
        btnEndBudgetDate.setOnClickListener()
        {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker2()
        }

        val btnConfirmBudgetDate : Button  = findViewById(R.id.btnConfirmBudgetDate)

        btnConfirmBudgetDate.setOnClickListener()
        {
            //Easy One Coder (2023) Calling Graph:
            val chart = findViewById<HorizontalBarChart>(R.id.horizontalBarChart)
            setupHorizontalBarChart(chart)
        }


    }
}

//Reference list:
//Easy One Coder, 2023. how to create bar chart | MP Android Chart | Android Studio 2024. [video online]. Available at: https://www.youtube.com/watch?v=WdsmQ3Zyn84 [Accessed 7 June 2025].