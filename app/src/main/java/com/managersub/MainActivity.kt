package com.managersub

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    // Массив для хранения данных в формате "название/дата"
    private val dataList = mutableListOf<String>()
    private lateinit var tvItemsList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val btn = findViewById<Button>(R.id.add)
        tvItemsList = findViewById(R.id.tvItemsList)

        btn.setOnClickListener {
            showCustomDialog()
        }

        updateItemsDisplay()
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_item)
        dialog.setCancelable(true)

        val etTitle = dialog.findViewById<EditText>(R.id.etTitle)
        val etDate = dialog.findViewById<EditText>(R.id.etDate)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAdd)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val date = etDate.text.toString().trim()

            if (title.isNotEmpty() && date.isNotEmpty()) {
                
                val item = "$title/$date"
                dataList.add(item)


                dialog.dismiss()
                updateItemsDisplay()
            } else {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateItemsDisplay() {
        if (dataList.isEmpty()) {
            tvItemsList.text = "Нет добавленных элементов"
        } else {
            val itemsText = StringBuilder()
            dataList.forEach { item ->

                val parts = item.split("/")
                val name = parts[0]
                val date = if (parts.size > 1) parts[1] else ""

                itemsText.append("$name $date\n")
            }
            tvItemsList.text = itemsText.toString()
        }
    }
}