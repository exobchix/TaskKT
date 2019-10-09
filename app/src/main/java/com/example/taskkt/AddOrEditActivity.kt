package com.example.taskkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.taskkt.db.DatabaseHandler
import com.example.taskkt.models.Tasks

import kotlinx.android.synthetic.main.activity_add_edit.*

class AddOrEditActivity : AppCompatActivity() {

    private var dbHandler: DatabaseHandler? = null
    private var isEditMode = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val tasks: Tasks = dbHandler!!.getTask(intent.getIntExtra("Id",0))
            input_name.setText(tasks.name)
            input_desc.setText(tasks.description)
            swt_completed.isChecked = tasks.completed == "Y"
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener {
            val success: Boolean
            if (!isEditMode) {
                val tasks = Tasks()
                tasks.name = input_name.text.toString()
                tasks.description = input_desc.text.toString()
                if (swt_completed.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.addTask(tasks) as Boolean
            } else {
                val tasks = Tasks()
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.name = input_name.text.toString()
                tasks.description = input_desc.text.toString()
                if (swt_completed.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.updateTask(tasks) as Boolean
            }

            if (success)
                finish()
        }

        btn_delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete the Task.")
                .setPositiveButton("YES") { dialog, _ ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
