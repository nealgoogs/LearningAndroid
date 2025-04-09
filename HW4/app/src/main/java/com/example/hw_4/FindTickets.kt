package com.example.hw_4

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FindTickets : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.find_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //SPINNER
        val eventCategory = listOf("Choose an event category", "Music", "Sports", "Theater", "Family", "Arts & Theater", "Concerts", "Comedy", "Dance")
        val eventAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, eventCategory)
        val eventSpinner = findViewById<Spinner>(R.id.spinner)
        eventSpinner.adapter = eventAdapter
        eventSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position == 0) return
        val selectedItem = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this, selectedItem, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected!", Toast.LENGTH_SHORT).show()
    }


}