package com.managersub
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private val dataList = mutableListOf<String>() //для диалогового окна
    private lateinit var textViewList: TextView
    private lateinit var pref: SharedPreferences
    // Spinner (выпадающий список)

    val subs = arrayOf("Банк", "Музыка", "Кино", "Аптеки", "Другое")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)


        val btn = findViewById<Button>(R.id.add)
        textViewList = findViewById(R.id.tvItemsList)


        loadData()


        updateItemsDisplay()

        btn.setOnClickListener {
            showCustomDialog()
        }
    }

    override fun onPause() {
        super.onPause()

        saveData()
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_item)
        dialog.setCancelable(true)

        val etTitle = dialog.findViewById<EditText>(R.id.etTitle)
        val etDate = dialog.findViewById<EditText>(R.id.etDate)
        val etCost = dialog.findViewById<EditText>(R.id.etCost)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAdd)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val spinner: Spinner = dialog.findViewById<Spinner>(R.id.spCategory)
        val arrayAdp = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subs)
        spinner.adapter = arrayAdp

        etDate.isFocusable = false
        etDate.isClickable = true

        etDate.setOnClickListener {
            showDatePicker(etDate)
        }

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val date = etDate.text.toString().trim()
            val cost = etCost.text.toString().trim()
            val category = spinner.selectedItem.toString()

            if (title.isNotEmpty() && date.isNotEmpty() && cost.isNotEmpty()) {
                val item = "$title/$date/$cost/$category"
                dataList.add(item)
                updateItemsDisplay()
                saveData()

                dialog.dismiss()
            } else {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%02d.%02d.%04d",
                    selectedDay, selectedMonth + 1, selectedYear
                )
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePicker.show()
    }
    private fun updateItemsDisplay() {
        if (dataList.isEmpty()) {
            textViewList.text = "Нет добавленных элементов"
        }
        else {
            val itemsText = StringBuilder()

            dataList.forEach { item ->
                val parts = item.split("/")
                val name = parts[0]
                val cost = parts[2]
                val date = if (parts.size > 1) parts[1] else ""
                val category = parts[3]


                itemsText.append("$name $date   $cost          $category\n")
            }
            textViewList.text = itemsText.toString()
        }
    }

    private fun saveData() {

        val dataString = dataList.joinToString("|||")

        val editor = pref.edit()
        editor.putString("saved_items", dataString)
        editor.apply()
    }

    private fun loadData() {
        val dataString = pref.getString("saved_items", null)
        if (dataString != null && dataString.isNotEmpty()) {
            dataList.clear()
            dataList.addAll(dataString.split("|||").filter { it.isNotEmpty() })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }
}