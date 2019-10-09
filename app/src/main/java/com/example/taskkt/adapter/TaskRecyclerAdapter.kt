package com.example.taskkt.adapter

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.M
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskkt.AddOrEditActivity
import com.example.taskkt.R
import com.example.taskkt.models.Tasks
import java.util.*

class TaskRecyclerAdapter(tasksList: List<Tasks>, internal var context: Context) : RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    private var tasksList: List<Tasks> = ArrayList()
    init {
        this.tasksList = tasksList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_tasks, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(M)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = tasksList[position]
        holder.name.text = tasks.name
        holder.desc.text = tasks.description
        if (tasks.completed == "Y")
            holder.listitem.background = ContextCompat.getDrawable(context, R.color.colorSuccess)
        else
            holder.listitem.background = ContextCompat.getDrawable(context, R.color.colorUnSuccess)

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", tasks.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName) as TextView
        var desc: TextView = view.findViewById(R.id.tvDesc) as TextView
        var listitem: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
    }

}
