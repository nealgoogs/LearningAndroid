package com.example.hw_4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(private val ticketList: List<Ticket>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage: ImageView = itemView.findViewById(R.id.eventImage)
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val venueName: TextView = itemView.findViewById(R.id.venueName)
        val venueAddress: TextView = itemView.findViewById(R.id.venueAddress)
        val eventDateTime: TextView = itemView.findViewById(R.id.eventDateTime)
        val priceRange: TextView = itemView.findViewById(R.id.priceRange)
        val seeTicketsButton: Button = itemView.findViewById(R.id.seeTicketsButton)
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


        // You can also set an onClickListener for the "See Tickets" button if needed
        holder.seeTicketsButton.setOnClickListener {
            // TODO: Handle button click
        }
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }
}
