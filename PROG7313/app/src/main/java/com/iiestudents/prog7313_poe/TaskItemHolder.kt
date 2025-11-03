package com.iiestudents.prog7313_poe

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//creating Expense holder (see To Do List App using Recycler View Android Studio Kotlin Example Tutorial, 2022):
class TaskItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtExpenseName: TextView = itemView.findViewById(R.id.txtExpenseName)
    val txtStartAndEndDate: TextView = itemView.findViewById(R.id.txtStartAndEndDate)
    val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
    val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
    val txtCategoryName: TextView = itemView.findViewById(R.id.txtCategoryName)
    val imgExpenseImage: ImageView = itemView.findViewById(R.id.imgExpenseImage)
}
//Reference List:

//To Do List App using Recycler View Android Studio Kotlin Example Tutorial. 2022. YoutTube video, added by code with cal. [Online]. Available at: https://www.youtube.com/watch?v=RfIR4oaSVfQ&t=485s&ab_channel=CodeWithCal [Accessed 1 May 2025].