package com.example.studentagedatabase

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etId: EditText
    lateinit var seekBarAge: SeekBar
    lateinit var tvAgeValue: TextView
    lateinit var btnAdd: Button
    lateinit var btnEdit: Button
    lateinit var btnDelete: Button
    lateinit var btnView: Button
    lateinit var tvData: TextView
    lateinit var db: DatabaseHelper
    var selectedAge = 17

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etId = findViewById(R.id.etId)
        seekBarAge = findViewById(R.id.seekBarAge)
        tvAgeValue = findViewById(R.id.tvAgeValue)
        btnAdd = findViewById(R.id.btnAdd)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        btnView = findViewById(R.id.btnView)
        tvData = findViewById(R.id.tvData)

        db = DatabaseHelper(this)

        seekBarAge.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    selectedAge = progress
                    tvAgeValue.text = "Age: $selectedAge"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val id = etId.text.toString().trim()
            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val success = db.addStudent(id, name, selectedAge)
            if (success) {
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show()
                clearInputs()
                showAllData()
            } else {
                Toast.makeText(this, "Student ID already exists!", Toast.LENGTH_LONG).show()
            }
        }

        btnEdit.setOnClickListener {
            val name = etName.text.toString().trim()
            val id = etId.text.toString().trim()
            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Enter Student ID + new data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rows = db.updateStudent(id, name, selectedAge)
            if (rows > 0) {
                Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show()
                clearInputs()
                showAllData()
            } else {
                Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val id = etId.text.toString().trim()
            if (id.isEmpty()) {
                Toast.makeText(this, "Enter Student ID to delete!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rows = db.deleteStudent(id)
            if (rows > 0) {
                Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show()
                clearInputs()
                showAllData()
            } else {
                Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        btnView.setOnClickListener {
            showAllData()
        }

        showAllData()
    }

    private fun showAllData() {
        val cursor = db.getAllStudents()
        if (cursor.count == 0) {
            tvData.text = "No student data available."
            return
        }
        var displayText = "=== STUDENT LIST (${cursor.count}) ===\n\n"
        var number = 1
        while (cursor.moveToNext()) {
            val id = cursor.getString(0)
            val name = cursor.getString(1)
            val age = cursor.getInt(2)
            displayText += "$number. ID: $id\n   Name: $name\n   Age: $age\n\n"
            number++
        }
        tvData.text = displayText
        cursor.close()
    }

    private fun clearInputs() {
        etName.text.clear()
        etId.text.clear()
        seekBarAge.progress = 17
        selectedAge = 17
        tvAgeValue.text = "Age: 17"
    }
}