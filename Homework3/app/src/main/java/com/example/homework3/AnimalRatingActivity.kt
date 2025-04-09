package com.example.homework3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AnimalRatingActivity : AppCompatActivity() {

    private val FILE_NAME = "CatRatings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animal_rate)

        //If data in first activity exists, make sure to get it.
        val name = intent.getStringExtra("cat_name")
        val image = intent.getStringExtra("cat_image")
        val rating = intent.getFloatExtra("cat_rating", 0f)

        val catNameView = findViewById<TextView>(R.id.cat_names)
        val catImageView = findViewById<ImageView>(R.id.image_view)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        val saveButton = findViewById<Button>(R.id.saveRatingButton)

        catNameView.text = name
        ratingBar.rating = rating
        Glide.with(this)
            .load(image)
            .into(catImageView)

        saveButton.setOnClickListener {
                val rating = ratingBar.rating
                val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putFloat(name, rating)
                editor.apply()

                Toast.makeText(this, "$name rated $rating", Toast.LENGTH_SHORT).show()
                finish()

        }
    }
}
