package com.example.NoteTaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.fragmentgg1.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Page1Fragment : Fragment() {
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.page1_fragment, container, false)

        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        datePicker.calendarViewShown = false

        // Initialize with current date
        val calendar = Calendar.getInstance()
        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, day)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedCalendar.time)
            viewModel.value.setValue(formattedDate)
        }

        // Add navigation button logic
        view.findViewById<Button>(R.id.nextButton)?.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.pager)
            viewPager?.currentItem = 1  // Go to next page
        }

        return view
    }
}