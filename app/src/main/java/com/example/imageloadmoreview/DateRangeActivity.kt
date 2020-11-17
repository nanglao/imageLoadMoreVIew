package com.example.imageloadmoreview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DateRangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.date_range_activity)

        val builder = MaterialDatePicker.Builder.dateRangePicker()

        val now = Calendar.getInstance()
        builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

        val picker = builder.build()
        picker.show(supportFragmentManager!!, picker.toString())


//        picker.addOnNegativeButtonClickListener { dismiss() }
        picker.addOnPositiveButtonClickListener { selection->

//            Toast.makeText(applicationContext, "The selected date range is ${it.first} - ${it.second}", Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, picker.getHeaderText(), Toast.LENGTH_LONG).show()
        }
        picker.addOnPositiveButtonClickListener {

        }


    }
}