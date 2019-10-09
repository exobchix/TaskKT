package com.example.taskkt.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.taskkt.models.Tasks
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $DESCRIPTION TEXT, $COMPLETED TEXT);"
        db.execSQL(createTABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(dropTABLE)
        onCreate(db)
    }

    fun addTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESCRIPTION, tasks.description)
        values.put(COMPLETED, tasks.completed)
        val stmSuccess = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$stmSuccess")
        return (Integer.parseInt("$stmSuccess") != -1)
    }

    fun getTask(_id: Int): Tasks {
        val tasks = Tasks()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
        tasks.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION))
        tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        cursor.close()
        return tasks
    }

    fun task(): List<Tasks> {
        val taskList = ArrayList<Tasks>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tasks = Tasks()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
                    tasks.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION))
                    tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
                    taskList.add(tasks)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    fun updateTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESCRIPTION, tasks.description)
        values.put(COMPLETED, tasks.completed)
        val stmSuccess = db.update(TABLE_NAME, values, "$ID=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$stmSuccess") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val stmSuccess = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$stmSuccess") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val stmSuccess = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$stmSuccess") != -1
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "MyTasks"
        private const val TABLE_NAME = "Tasks"
        private const val ID = "Id"
        private const val NAME = "Name"
        private const val DESCRIPTION = "Description"
        private const val COMPLETED = "Completed"
    }
}