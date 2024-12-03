package com.example.NoteApp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// this class is simplly used for the creation of the ViewModel
// it is used to create the ViewModel

class MyViewModel2(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}