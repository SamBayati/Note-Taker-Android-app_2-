package com.example.NoteApp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// this class is generally used for the fragments to be able to communicate with the ViewModel


// this class is for the ViewModel that will be used to store and manage the data for the fragments
// it extends the AndroidViewModel class and contains LiveData objects for the values and notes
// it also contains functions to add notes to the database and retrieve notes from the database
// it initializes the values for the ViewModel to default values and loads notes from the database on initialization
// it also contains functions to get filtered notes from the database and update the current tab position
// it also contains a function to refresh the notes


class FragViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = NotesDbHelper(application)
    private val MAX_NOTES = 4  // Maximum number of notes allowed

    // Original value for text exchange
    private val _value = MutableLiveData<String>()
    val value: MutableLiveData<String> get() = _value

    // New values for note-taking
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: MutableLiveData<String> get() = _selectedDate

    private val _notes = MutableLiveData<List<Pair<String, String>>>()
    val notes: MutableLiveData<List<Pair<String, String>>> get() = _notes

    // Tab position tracking
    private val _currentTab = MutableLiveData<Int>()
    val currentTab: LiveData<Int> = _currentTab

    // Initialize the values for the ViewModel to default values
    init {
        _value.value = "default"
        _selectedDate.value = ""
        loadAllNotes() // Load notes from database on initialization
    }

    // attaching images to the notes
//    data class NoteWithImage(
//        val date: String,
//        val content: String,
//        val imageUri: String?
//    )



    private fun loadAllNotes() {
        _notes.value = dbHelper.getAllNotes()
    }

    // Add note to both SQLite and LiveData with maximum limit check
    fun addNote(date: String, noteText: String) {
        val currentNotes = _notes.value ?: emptyList()
        if (currentNotes.size >= MAX_NOTES) {
            // Remove oldest note from database and memory if at capacity
            val oldestNote = currentNotes.first()
            dbHelper.deleteNote(oldestNote.first, oldestNote.second)

            // Add new note
            dbHelper.addNote(date, noteText)
        } else {
            // Add new note normally
            dbHelper.addNote(date, noteText)
        }
        loadAllNotes() // Reload notes from database
    }

    // Get filtered notes from database
    fun getFilteredNotes(month: String, year: String) {
        _notes.value = dbHelper.getFilteredNotes(month, year)
    }

    // Update current tab position
    fun setCurrentTab(position: Int) {
        _currentTab.value = position
    }

    // Function to get all notes (useful for refreshing the list)
    fun refreshNotes() {
        loadAllNotes()
    }

    // function to remove a note from the list on the third fragment when note opened
    fun removeNote(position: Int) {
        val currentNotes = _notes.value?.toMutableList() ?: mutableListOf()
        if (position in currentNotes.indices) {
            val noteToDelete = currentNotes[position]
            dbHelper.deleteNote(noteToDelete.first, noteToDelete.second)
            currentNotes.removeAt(position)
            _notes.value = currentNotes
        }
    }
}