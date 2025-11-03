package com.iiestudents.prog7313_poe
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
//creating adapter for expenses - wrong class name (see To Do List App using Recycler View Android Studio Kotlin Example Tutorial, 2022):
class TaskItemAdapter(private val taskItems: List<Expense>):  RecyclerView.Adapter<TaskItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemHolder {
        val from = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_recyclerview_items, parent, false)
        return TaskItemHolder(from)
    }
    override fun getItemCount(): Int {
        return taskItems.size
    }

    //creating an object on the recycle view (see To Do List App using Recycler View Android Studio Kotlin Example Tutorial, 2022):
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskItemHolder, position: Int) {
        val taskItem = taskItems[position]

        holder.txtExpenseName.text = taskItem.expenseName
        holder.txtStartAndEndDate.text = taskItem.startDate + " - " + taskItem.endDate
        holder.txtDescription.text = taskItem.description
        holder.txtAmount.text = taskItem.amount.toString()
        //holder.txtCategoryName.text = taskItem.categoryName

//        //Image:
//        if (taskItem.imageUri != null) {
//            holder.imgExpenseImage.setImageURI(taskItem.imageUri!!.toUri())
//        }
    }
}
//Reference List:

//To Do List App using Recycler View Android Studio Kotlin Example Tutorial. 2022. YoutTube video, added by code with cal. [Online]. Available at: https://www.youtube.com/watch?v=RfIR4oaSVfQ&t=485s&ab_channel=CodeWithCal [Accessed 1 May 2025].


