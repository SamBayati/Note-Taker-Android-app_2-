package com.example.NoteTaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.fragmentgg1.R
class Page2Fragment : Fragment() {
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.page2_fragment, container, false)

        val selectedDateText = view.findViewById<TextView>(R.id.selectedDateText)
        val noteInput = view.findViewById<EditText>(R.id.editValue)
        val saveButton = view.findViewById<Button>(R.id.vButton)
        val clearButton = view.findViewById<Button>(R.id.clearButton)

        viewModel.value.observe(viewLifecycleOwner) { date ->
            selectedDateText.text = "Selected Date: $date"
        }

        saveButton.setOnClickListener {
            val noteText = noteInput.text.toString()
            if (noteText.isNotEmpty() && viewModel.value.value != null) {
                viewModel.addNote(viewModel.value.value!!, noteText)
                noteInput.text.clear()
            }
        }

        clearButton.setOnClickListener {
            noteInput.text.clear()
        }

        // Add navigation buttons logic
        view.findViewById<Button>(R.id.prevButton)?.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.pager)
            viewPager?.currentItem = 0
        }

        view.findViewById<Button>(R.id.nextButton)?.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.pager)
            viewPager?.currentItem = 2
        }

        return view
    }
}