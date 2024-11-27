package com.example.NoteTaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentgg1.R

class Page1Fragment : Fragment() {
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val view = inflater.inflate(R.layout.page1_fragment, container, false)
        val but = view.findViewById<Button>(R.id.vButton)
        val value = view.findViewById<EditText>(R.id.editValue)
        but.setOnClickListener {
            viewModel.value.setValue(value.getText().toString())
        }
        return view
    }
}