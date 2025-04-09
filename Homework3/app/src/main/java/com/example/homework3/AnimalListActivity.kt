package com.example.homework3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnimalListActivity : AppCompatActivity() {

    //allows me to use these in both onCreate() and onRestart() and onResume()
    private lateinit var recyclerView: RecyclerView
    private lateinit var animalAdapter: AnimalListRecycler
    private val FILE_NAME = "CatRatings"
    private val TAG = "MAIN PAGE!!!"
    private val numberOfAnimals = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.animal_recycler)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        animalAdapter = AnimalListRecycler(getSortedCats())
        recyclerView.adapter = animalAdapter

        val clearButton = findViewById<Button>(R.id.clear_button)
        clearButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(this, "All ratings cleared!", Toast.LENGTH_SHORT).show()
            animalAdapter.updateData(getSortedCats())
        }
    }

    override fun onRestart() {
        super.onRestart()
        animalAdapter.updateData(getSortedCats())
        Log.i(TAG, "onRestart was called")
    }

    override fun onResume() {
        super.onResume()
        animalAdapter.updateData(getSortedCats())
        Log.i(TAG, "onRestart was called")
    }

    private fun generateCats(size: Int): ArrayList<Cats> {
        //loadData method to retrieve data
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val cats = ArrayList<Cats>()
        for (i in 1..size) {
            val name = "Cat-$i"
            //this loads the ratings from the second activity
            val rating = sharedPreferences.getFloat(name, 0f)
            val image = "https://cataas.com/cat?cache=$i"
            cats.add(Cats(name, rating, image))
        }
        return cats
    }

    private fun getSortedCats(): ArrayList<Cats> {
        val catList = generateCats(numberOfAnimals)
        val sortedList = catList.sortedWith(compareByDescending { it.rating })
        return ArrayList(sortedList)
    }
}
