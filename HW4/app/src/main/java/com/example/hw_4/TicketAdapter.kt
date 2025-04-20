package com.example.hw_4

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TicketAdapter(private val ticketList: List<Ticket>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eventImage: ImageView = itemView.findViewById(R.id.eventImage)
        var eventName: TextView = itemView.findViewById(R.id.eventName)
        var venueName: TextView = itemView.findViewById(R.id.venueName)
        var venueAddress: TextView = itemView.findViewById(R.id.venueAddress)
        var eventDateTime: TextView = itemView.findViewById(R.id.eventDateTime)
        var priceRange: TextView = itemView.findViewById(R.id.priceRange)
        var seeTicketsButton: Button = itemView.findViewById(R.id.seeTicketsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cards, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]

        holder.eventName.text = ticket.eventName
        holder.venueName.text = ticket.venueName
        holder.venueAddress.text = ticket.venueAddress
        holder.eventDateTime.text = ticket.dateTime
        holder.priceRange.text = ticket.priceRange

        Log.d("TicketAdapter", "Ticket should be binded: ${ticket.eventName}")

        Glide.with(holder.itemView.context)
            .load(ticket.imageUrl)
            .into(holder.eventImage)

        holder.seeTicketsButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(ticket.ticketUrl))
            holder.itemView.context.startActivity(browserIntent)
        }
    }


    override fun getItemCount(): Int {
        return ticketList.size
    }
}
