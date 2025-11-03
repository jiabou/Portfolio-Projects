package com.iiestudents.prog7313_poe


import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_expense)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack : Button = findViewById(R.id.btnBack)

        btnBack.setOnClickListener(){
            val intent : Intent = Intent (this, HomePage::class.java)
            startActivity(intent)
        }
        val txtError: TextView = findViewById(R.id.txtExpenseError)
        //image storing var:
        var selectedImageUri: Uri? = null

        //Dates storing:
        var startDate: String = ""
        var endDate: String  = ""

        //Date buttons:
        val btnAddStartDate : Button = findViewById(R.id.btnAddStartDate)
        val btnAddEndDate : Button = findViewById(R.id.btnAddEndDate)

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
                    startDate = formattedDate+""
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
                    endDate = formattedDate+""
                },
                //using calendar2
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        //Start Button:
        btnAddStartDate.setOnClickListener {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker1()
        }

        //End Button:
        btnAddEndDate.setOnClickListener {
            //Chan (2023) Show the DatePicker dialog
            showDatePicker2()
        }

        //Populate Expense into database:
        //Create new Expense category:
        val btnAddCategory : Button = findViewById(R.id.btnAddCategory)

        val btnCreateCategory : Button = findViewById(R.id.btnCreateCategory)
        val edNewCategory : EditText = findViewById(R.id.edNewCategory)

        //Display and hiding components:
        btnAddCategory.setOnClickListener {
            btnCreateCategory.isEnabled = true
            btnCreateCategory.visibility = View.VISIBLE

            edNewCategory.isEnabled = true
            edNewCategory.visibility = View.VISIBLE
        }

        btnCreateCategory.setOnClickListener {
            btnCreateCategory.isEnabled = false
            btnCreateCategory.visibility = View.INVISIBLE

            edNewCategory.isEnabled = false
            edNewCategory.visibility = View.INVISIBLE
            edNewCategory.text.clear()
        }

        //Create New Expense:
        val btnAddExpense : Button = findViewById(R.id.btnAddExpense)

        val edExpenseName: EditText = findViewById(R.id.edExpenseName)
        val edExpenseAmount: EditText = findViewById(R.id.edExpenseAmount)
        val edDescription: EditText = findViewById(R.id.edDescription)
        val spCategory: Spinner = findViewById(R.id.spCategory)

        val expenseViewModel = ExpenseViewModel()
        val categoryViewModel = CategoryViewModel()

        val arrCategory = arrayListOf<Category>()
        var selectedItem :String? = ""
        var categoryID : String? = ""

        //Populate Spinner(spCategory):
        fun populateSpinner() {
            lifecycleScope.launch {
                var categories: List<Category> = mutableListOf<Category>()
                try {

                    arrCategory.clear()
                    categories = categoryViewModel.allCategories()

                    Log.d("CategoryList", "Fetched categories: $categories")
                    val listOfCategories = mutableListOf<String>()
                    if (categories.isNotEmpty()) {
                        btnAddExpense.isEnabled = true
                        for (category in categories) {

                            arrCategory.add(category)
                            Log.d(
                                "CategoryItem",
                                "CategoryName to be added: $category.categoryName.toString()"
                            )
                            listOfCategories.add(category.categoryName.toString())
                        }
                        //creating spinner adapter (see How to Implement Spinner in Android, 2023):
                        val adapter = ArrayAdapter(
                            this@CreateExpense,
                            android.R.layout.simple_spinner_item,
                            listOfCategories
                        )
                        Log.d("CategoryList", "Fetched ListOFCategories: $listOfCategories")
                        //pupulating the spinner (see How to Implement Spinner in Android, 2023):
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spCategory.adapter = adapter
                        spCategory.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?, position: Int, id: Long
                                ) {
                                    selectedItem = arrCategory[position].categoryName
                                    categoryID = arrCategory[position].cID
                                    btnAddExpense.isEnabled = true
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    selectedItem = ""
                                    btnAddExpense.isEnabled = false
                                }
                            }
                    } else {
                        // Handle empty list (optional)
                        btnAddExpense.isEnabled = false
                    }

                } catch (e: Exception) {
                    Log.d("error", e.message.toString())
                }
            }
        }
        populateSpinner()
        //Create new Category:
        btnCreateCategory.setOnClickListener {
            btnAddExpense.isEnabled = true
            try {

                var categoryName = edNewCategory.text.toString()
                categoryViewModel.insertCategory(categoryName)

                populateSpinner()

                btnCreateCategory.isEnabled = false
                btnCreateCategory.visibility = View.INVISIBLE
                edNewCategory.isEnabled = false
                edNewCategory.visibility = View.INVISIBLE
            }
            catch(e:Exception)
            {
                Log.d("error", e.message.toString())
            }
        }

        //Create new Expense:
        btnAddExpense.setOnClickListener {
            try {
                val amount = edExpenseAmount.text.toString()
                if (categoryID!!.isNotEmpty()  && selectedItem!!.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() &&
                    edExpenseAmount.text.isNotEmpty() && edExpenseName.text.isNotEmpty() && edDescription.text.isNotEmpty()) {
                    var expense = Expense(
                        eid = "",
                        cid = (categoryID.toString()),
                        expenseName = edExpenseName.text.toString(),
                        amount = amount.toDouble(),
                        startDate = startDate,
                        endDate = endDate,
                        description = edDescription.text.toString()
                        //imageUri = selectedImageUri?.toString()
                    )
                    expenseViewModel.addExpense(expense)
                    val intent : Intent = Intent (this, HomePage::class.java)
                    startActivity(intent)
                }
                else{
                    txtError.visibility = View.VISIBLE
                    txtError.text = "All Fields:\n are required except for image."
                }
            }
            catch(e:Exception)
            {
                Log.d("error", e.message.toString())
            }
        }
        val btnAddImage: Button = findViewById(R.id.btnAddImage)
        //create photo picker

//        fun persistUri(uri: Uri) {
//            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//            try {
//                applicationContext.contentResolver.takePersistableUriPermission(uri, takeFlags)
//                selectedImageUri = uri
//            } catch (e: SecurityException) {
//                e.printStackTrace()
//            }
//        }

//        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
//                persistUri(uri)
            }
        }
//
        btnAddImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            if (selectedImageUri != null){
//                this.contentResolver.takePersistableUriPermission(selectedImageUri!!, flag)
            }
        }
    }
}

//Reference list:
//
//Chan, D. 2023. Date Picker Using Kotlin in Android Studio | DatePickerDialog — Android Studio Tutorial | Kotlin, Medium, 3 August 2023. [Blog]. Available at: https://devendrac706.medium.com/date-picker-using-kotlin-in-android-studio-datepickerdialog-android-studio-tutorial-kotlin-3bbc606585a [Accessed 29 April 2025].

//Developers. 2025. Use Kotlin coroutines with lifecycle-aware components, 10 February 2025. [Online]. Available at: https://developer.android.com/topic/libraries/architecture/coroutines [Accessed 28 April 2025].

//How to Implement Spinner in Android. 2023. YouTube video, added by Codes Easy. [Online]. Available at: https://www.youtube.com/watch?v=4ogzfAipGS8 [Accessed 30 April 2025].