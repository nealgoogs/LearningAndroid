package com.example.homework3

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimalListRecycler(private val cats: ArrayList<Cats>) : RecyclerView.Adapter<AnimalListRecycler.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catPicture = itemView.findViewById<ImageView>(R.id.image_view)
        val catRating = itemView.findViewById<RatingBar>(R.id.rating_bar)
        val catName = itemView.findViewById<TextView>(R.id.cat_names)
        val rateButton = itemView.findViewById<Button>(R.id.rate_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.animal_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = cats[position]

        holder.catName.text = currentItem.name
        holder.catRating.rating = currentItem.rating

        Glide.with(holder.itemView.context)
            .load(currentItem.catImage)
            .into(holder.catPicture)

        holder.rateButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AnimalRatingActivity::class.java)
            intent.putExtra("cat_name", currentItem.name)
            intent.putExtra("cat_image", currentItem.catImage)
            intent.putExtra("cat_rating", currentItem.rating)
            context.startActivity(intent)
        }
    }

    fun updateData(newAnimalList: ArrayList<Cats>) {
        cats.clear()
        cats.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cats.size
    }


}
