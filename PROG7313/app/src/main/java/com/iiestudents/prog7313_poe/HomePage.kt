package com.iiestudents.prog7313_poe

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HomePage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Dates storing:
        var startDate: String = ""
        var endDate: String = ""

        //Date buttons:
        val btnStartDate : Button = findViewById(R.id.btnStartDate)
        val btnEndDate : Button = findViewById(R.id.btnEndDate)

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



        //Add Expense button:
        val btnAddExpense : Button = findViewById(R.id.btnAddExpense)

        btnAddExpense.setOnClickListener(){
            val intent : Intent = Intent (this, CreateExpense::class.java)
            startActivity(intent)
        }

        //Add Income button:
        //val btnAddIncome : Button = findViewById(R.id.btnAddIncome)


        //Income Buttons
        val btnIncome1 : Button = findViewById(R.id.btnIncome1)

        btnIncome1.setOnClickListener()
        {

            //btnIncome1.text = selectedDate

            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }

        val btnIncome2 : Button = findViewById(R.id.btnIncome2)

        btnIncome2.setOnClickListener()
        {
            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }

        val btnIncome3 : Button = findViewById(R.id.btnIncome3)

        btnIncome3.setOnClickListener()
        {
            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }

        //Back and Forwards Buttons Will be coded by John and Josh, these should change the month and show data from those months
        //Bottom Row Buttons
        val btnMyBudget : Button = findViewById(R.id.btnMyBudget)

        btnMyBudget.setOnClickListener()
        {
            val intent : Intent = Intent (this, BudgetMenu::class.java)
            startActivity(intent)
        }
        val btnAccount : Button = findViewById(R.id.btnAccount)

        btnAccount.setOnClickListener()
        {
            val intent : Intent = Intent (this, MyAccountPage::class.java)
            startActivity(intent)
        }
        val btnSharedBudget : Button = findViewById(R.id.btnSharedBudget)

        btnSharedBudget.setOnClickListener()
        {
            val intent : Intent = Intent (this, SharedBudgetPage::class.java)
            startActivity(intent)
        }

        val btnExpenses1 : Button = findViewById(R.id.btnExpenses1)

        btnExpenses1.setOnClickListener()
        {
            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }
        val btnExpenses2 : Button = findViewById(R.id.btnExpenses2)

        btnExpenses2.setOnClickListener()
        {
            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }
        val btnExpenses3 : Button = findViewById(R.id.btnExpenses3)

        btnExpenses3.setOnClickListener()
        {
            val intent : Intent = Intent (this, DetailedTrackingPage::class.java)
            startActivity(intent)
        }

        val btnGoBack: Button = findViewById(R.id.btnLogOut)

        btnGoBack.setOnClickListener{
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val categoryViewModel = CategoryViewModel()
        val expenseViewModel = ExpenseViewModel()
        var txtExpenses1 : TextView = findViewById(R.id.txtExpenses1)
        var txtExpenses2 : TextView = findViewById(R.id.txtExpenses2)
        var txtExpenses3 : TextView = findViewById(R.id.txtExpenses3)

        var count1 : Int = 0
        var limit1 : Int = 1
        var categoryID1 : String? = ""
        var categoryTotalAmount1 : Double = 0.0

        var count2 : Int = 0
        var limit2 : Int = 2
        var categoryID2 : String? = ""
        var categoryTotalAmount2 : Double = 0.0

        var count3 : Int = 0
        var limit3 : Int = 3
        var categoryID3 : String? = ""
        var categoryTotalAmount3 : Double = 0.0

        //Calculates and assigns each total category amount to the corresponding txtExpenses:


        @SuppressLint("NewApi")
        fun totalCategoryAmount(){
            try {
                //creating a lifecyclescope to help handle the live data (Developers, 2025):
                lifecycleScope.launch {
                    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    //Displaying to txtExpenses1:
                    if (categoryID1!!.isNotEmpty()){
                        val expenses1 : List<Expense> = expenseViewModel.getCategory(categoryID1.toString())
                        Log.d("Expense category", "expense = $expenses1")
                        if (expenses1.isNotEmpty()){
                            var doesExist = false
                            for (expense1 in expenses1){
                                if (startDate.isNotEmpty() && endDate.isNotEmpty()){
                                    val expenseDate = LocalDate.parse(expense1.startDate, dateFormat)
                                    if (expenseDate.isAfter(LocalDate.parse(startDate, dateFormat)) && expenseDate.isBefore(LocalDate.parse(endDate, dateFormat)) || expenseDate.isEqual(LocalDate.parse(startDate, dateFormat)) || expenseDate.isEqual(LocalDate.parse(endDate, dateFormat))){
                                        //Adds to total amount:
                                        categoryTotalAmount1 += expense1.amount
                                        //Displays total amount:
                                        txtExpenses1.text =  "R" + categoryTotalAmount1.toString() //+ categoryID1.toString()
                                        doesExist = true
                                    }
                                }
                                else{
                                    txtExpenses1.text = "R0"
                                }
                            }
                            if (!doesExist){
                                txtExpenses1.text = "R0"
                            }
                            categoryTotalAmount1 = 0.0
                        }
                        else{
                            txtExpenses1.text = "R0"
                        }
                    }
                    else{
                        //Resets txtExpenses1 if no category assigned to btnExpenses1:
                        txtExpenses1.text = "R0"
                    }

                    //Displaying to txtExpenses2:
                    if (categoryID2!!.isNotEmpty()){
                        val expenses2 : List<Expense> = expenseViewModel.getCategory(categoryID2.toString())//Stores expenses by the category on btnExpenses2
                        if (expenses2.isNotEmpty()){
                            var doesExist = false
                            for (expense2 in expenses2){
                                if (startDate.isNotEmpty() && endDate.isNotEmpty()){
                                    val expenseDate = LocalDate.parse(expense2.startDate, dateFormat)
                                    if (expenseDate.isAfter(LocalDate.parse(startDate, dateFormat)) && expenseDate.isBefore(LocalDate.parse(endDate, dateFormat)) || expenseDate.isEqual(LocalDate.parse(startDate, dateFormat)) || expenseDate.isEqual(LocalDate.parse(endDate, dateFormat))){
                                        //Adds to total amount:
                                        categoryTotalAmount2 += expense2.amount
                                        //Displays total amount:
                                        txtExpenses2.text = "R" + categoryTotalAmount2.toString() //+ categoryID2.toString()
                                        doesExist = true
                                    }

                                }
                                else{
                                    txtExpenses2.text = "R0"
                                }
                            }
                            if (!doesExist){
                                txtExpenses2.text = "R0"
                            }
                            categoryTotalAmount2 = 0.0
                        }
                        else{
                            txtExpenses2.text = "R0"
                        }
                    }
                    else{
                        //Resets txtExpenses2 if no category assigned to btnExpenses2:
                        txtExpenses2.text = "R0"
                    }

                    //Displaying to txtExpenses3:
                    if (categoryID3!!.isNotEmpty()){
                        val expenses3 : List<Expense> = expenseViewModel.getCategory(categoryID3.toString())//Stores expenses by the category on btnExpenses3
                        if (expenses3.isNotEmpty()){
                            var doesExist = false
                            for (expense3 in expenses3){
                                if (startDate.isNotEmpty() && endDate.isNotEmpty()){
                                    val expenseDate = LocalDate.parse(expense3.startDate, dateFormat)

                                    if (expenseDate.isAfter(LocalDate.parse(startDate, dateFormat)) && expenseDate.isBefore(LocalDate.parse(endDate, dateFormat)) || expenseDate.isEqual(LocalDate.parse(startDate, dateFormat)) || expenseDate.isEqual(LocalDate.parse(endDate, dateFormat))) {
                                        //Adds to total amount:
                                        categoryTotalAmount3 += expense3.amount
                                        //Displays total amount:
                                        txtExpenses3.text = "R" + categoryTotalAmount3.toString()
                                        doesExist = true
                                    }
                                    else{
                                        txtExpenses3.text = "R0"
                                    }
                                }
                                else{
                                    txtExpenses2.text = "R0"
                                }

                            }
                            if (!doesExist){
                                txtExpenses3.text = "R0"
                            }
                        }
                        else{
                            txtExpenses3.text = "R0"
                        }
                        categoryTotalAmount3 = 0.0
                    }
                    else{
                        //Resets txtExpenses3 if no category assigned to btnExpenses3:
                        txtExpenses3.text = "R0"
                    }
                }
            }
            catch(e:Exception)
            {
                Log.d("error", e.message.toString())
            }
        }


        //Sets each category to its corresponding btnExpenses:
        fun setCategories(){
            try{
                //creating a lifecyclescope to help handle the live data (Developers, 2025):
                lifecycleScope.launch {
                    val categories : List<Category> = categoryViewModel.allCategories()
                    if (categories.isNotEmpty()){
                        //Resets all values and btnExpenses/txtExpenses positions:
                        count1 = 0
                        count2 = 0
                        count3 = 0
                        categoryID1 = ""
                        categoryID2 = ""
                        categoryID3 = ""

                        //Displaying to btnExpenses1:
                        if (categories.size >= limit1){
                            btnExpenses1.visibility = View.VISIBLE
                            for (category in categories){
                                if (count1 < limit1){//Checks if the correct category has be reached
                                    btnExpenses1.text = category.categoryName
                                    categoryID1 = category.cID
                                    count1++
                                }
                            }

                        }
                        else{
                            btnExpenses1.visibility = View.INVISIBLE
                        }

                        //Displaying to btnExpenses2:
                        if (categories.size >= limit2){
                            btnExpenses2.visibility = View.VISIBLE
                            for (category in categories){
                                if (count2 < limit2){//Checks if the correct category has be reached
                                    btnExpenses2.text = category.categoryName
                                    categoryID2 = category.cID
                                    count2++
                                }
                            }
                        }
                        else{
                            btnExpenses2.visibility = View.INVISIBLE
                        }

                        //Displaying to btnExpenses3:
                        if (categories.size >= limit3){
                            btnExpenses3.visibility = View.VISIBLE
                            for (category in categories){//Checks if the correct category has be reached
                                if (count3 < limit3){
                                    btnExpenses3.text = category.categoryName
                                    categoryID3 = category.cID
                                    count3++
                                }
                            }
                        }
                        else{
                            btnExpenses3.visibility = View.INVISIBLE
                        }

                        totalCategoryAmount()//Needs to be here!
                    }

                }
            }
            catch(e:Exception)
            {
                Log.d("error", e.message.toString())
            }
        }

        setCategories()

        val btnNextExpenses : Button = findViewById(R.id.btnNextExpenses)

        //Move to the next category + total amount when btnNextExpenses is click:
        btnNextExpenses.setOnClickListener{
            limit1++
            limit2++
            limit3++

            setCategories()
        }

        val btnBackExpenses : Button = findViewById(R.id.btnBackExpenses)

        //Moves back 1 category + total amount when btnBackExpenses is click:
        btnBackExpenses.setOnClickListener {
            if (limit1 > 1){
                limit1--
                limit2--
                limit3--

                setCategories()

            }
        }

        //val txtExpenses : TextView = findViewById(R.id.txtExpenses)

        //txtExpenses.text = categoryID1.toString()

        //Start Button:
        btnStartDate.setOnClickListener {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker1()
        }

        //End Button:
        btnEndDate.setOnClickListener {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker2()
        }

        val btnSetDate : Button = findViewById(R.id.btnSetDate)

        btnSetDate.setOnClickListener {
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                setCategories()
            }
        }

    }
}

//Reference list:
//
//Chan, D. 2023. Date Picker Using Kotlin in Android Studio | DatePickerDialog — Android Studio Tutorial | Kotlin, Medium, 3 August 2023. [Blog]. Available at: https://devendrac706.medium.com/date-picker-using-kotlin-in-android-studio-datepickerdialog-android-studio-tutorial-kotlin-3bbc606585a [Accessed 29 April 2025]

//Developers. 2025. Use Kotlin coroutines with lifecycle-aware components, 10 February 2025. [Online]. Available at: https://developer.android.com/topic/libraries/architecture/coroutines [Accessed 28 April 2025].