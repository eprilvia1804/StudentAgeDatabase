package com.example.studentagedatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "StudentDB.db", null, 1) {

    private val TABLE_STUDENT = "Student"
    private val COL_ID = "student_id"
    private val COL_NAME = "name"
    private val COL_AGE = "age"

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENT (
                $COL_ID TEXT PRIMARY KEY,
                $COL_NAME TEXT,
                $COL_AGE INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(db)
    }

    fun addStudent(studentId: String, name: String, age: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID, studentId)
            put(COL_NAME, name)
            put(COL_AGE, age)
        }
        val result = db.insert(TABLE_STUDENT, null, values)
        db.close()
        return result != -1L
    }

    fun getAllStudents(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_STUDENT, null, null, null, null, null, "$COL_NAME ASC")
    }

    fun updateStudent(studentId: String, name: String, age: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_AGE, age)
        }
        val rows = db.update(TABLE_STUDENT, values, "$COL_ID = ?", arrayOf(studentId))
        db.close()
        return rows
    }

    fun deleteStudent(studentId: String): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_STUDENT, "$COL_ID = ?", arrayOf(studentId))
        db.close()
        return rows
    }
}