package com.example.NoteTaker

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentgg1.R
import android.widget.Button

import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView

import android.widget.*
import androidx.appcompat.widget.SearchView




class Page3Fragment : Fragment() {
    private lateinit var viewModel: MyViewModel
    private var currentSearchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.page3_fragment, container, false)
        val notesContainer = view.findViewById<LinearLayout>(R.id.notesContainer)
        val monthSpinner = view.findViewById<Spinner>(R.id.monthSpinner)
        val yearSpinner = view.findViewById<Spinner>(R.id.yearSpinner)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        // Set up month spinner
        val months = listOf("All Months", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter

        // Set up year spinner
        val years = mutableListOf("All Years")
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        // Set up SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentSearchQuery = newText ?: ""
                filterNotes(
                    monthSpinner.selectedItem.toString(),
                    yearSpinner.selectedItem.toString(),
                    notesContainer
                )
                return true
            }
        })

        // Handle month selection
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterNotes(monthSpinner.selectedItem.toString(),
                    yearSpinner.selectedItem.toString(),
                    notesContainer)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Handle year selection
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterNotes(monthSpinner.selectedItem.toString(),
                    yearSpinner.selectedItem.toString(),
                    notesContainer)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Observe notes changes
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            // Update available years
            val availableYears = notes.map { it.first.split("/").last() }.distinct().sorted()
            years.clear()
            years.add("All Years")
            years.addAll(availableYears)
            yearAdapter.notifyDataSetChanged()

            // Update notes display
            filterNotes(monthSpinner.selectedItem.toString(),
                yearSpinner.selectedItem.toString(),
                notesContainer)
        }

        return view
    }

    private fun filterNotes(selectedMonth: String, selectedYear: String, container: LinearLayout) {
        container.removeAllViews()
        val notes = viewModel.notes.value ?: emptyList()

        val filteredNotes = notes.filter { (date, note) ->
            val dateParts = date.split("/")
            val noteMonth = when(dateParts[1].toInt()) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> ""
            }
            val noteYear = dateParts[2]

            // Apply both date filters and search query
            val matchesDateFilter = (selectedMonth == "All Months" || selectedMonth == noteMonth) &&
                    (selectedYear == "All Years" || selectedYear == noteYear)

            val matchesSearch = if (currentSearchQuery.isEmpty()) {
                true
            } else {
                note.contains(currentSearchQuery, ignoreCase = true) ||
                        date.contains(currentSearchQuery, ignoreCase = true)
            }

            matchesDateFilter && matchesSearch
        }

        // Display filtered notes
        filteredNotes.forEach { (date, note) ->
            val noteView = layoutInflater.inflate(R.layout.note_item, container, false)
            noteView.findViewById<TextView>(R.id.dateText).text = date
            noteView.findViewById<TextView>(R.id.noteText).text = note

            noteView.setOnClickListener {
                showNoteDialog(date, note)
            }

            container.addView(noteView)
        }
    }

    private fun showNoteDialog(date: String, noteContent: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_note_detail)

        val window = dialog.window
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.9).toInt() // 90% of screen width

        window?.setLayout(
            width,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.CENTER)

        dialog.findViewById<TextView>(R.id.dialogDateText).text = "Date created: $date"
        dialog.findViewById<TextView>(R.id.dialogNoteText).text = noteContent
        dialog.findViewById<Button>(R.id.closeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }}