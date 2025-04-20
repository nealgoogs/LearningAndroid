package com.example.hw_4

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class FindTickets : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val BASE_URL = "https://app.ticketmaster.com/discovery/v2/"
    private val API_KEY = "l06xydruzGLAz9Fqs5VuebX0fFUcyA38"

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketAdapter: TicketAdapter
    private val ticketList = ArrayList<Ticket>()
    private lateinit var cityEditText: EditText
    private var selectedCategory: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.find_tickets)

        val categoryList = arrayListOf(
            "Choose an event category", "Music", "Sports", "Theater", "Family",
            "Arts & Theater", "Concerts", "Comedy", "Dance"
        )

        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this

        cityEditText = findViewById(R.id.cityView)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ticketAdapter = TicketAdapter(ticketList)
        recyclerView.adapter = ticketAdapter

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val city = cityEditText.text.toString()

            if (selectedCategory == "Choose an event category") {
                showDialog("Event category missing", "Event category cannot be empty. Please select an event category.")
            } else if (city.isEmpty()) {
                showDialog("Location missing", "City cannot be empty. Please enter a city.")
            } else {
                getTickets(selectedCategory, city)
            }

        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCategory = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        selectedCategory = ""
    }

    private fun showDialog(title: String, message: String) {
        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setIcon(android.R.drawable.ic_delete)
        dialogBuilder.setPositiveButton("OKAY") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }



    private fun formatDateTime(date: String, time: String?): String {
        var result = date
        try {
            if (time != null) {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val outputFormat = SimpleDateFormat("EEE, MMM d, h:mm a")
                val parsed = inputFormat.parse("$date $time")
                if (parsed != null) {
                    result = outputFormat.format(parsed)
                }
            } else {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                val outputFormat = SimpleDateFormat("EEE, MMM d")
                val parsed = inputFormat.parse(date)
                if (parsed != null) {
                    result = outputFormat.format(parsed)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun getTickets(category: String, city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RetrofitService::class.java)

        Log.d("FindTickets", "Fetching tickets for $category in $city")

        api.getEvents(API_KEY, category, city).enqueue(object : Callback<TicketmasterResponse> {
            override fun onResponse(call: Call<TicketmasterResponse>, response: Response<TicketmasterResponse>) {
                val body = response.body()
                val noResultsTextView = findViewById<TextView>(R.id.noResultsTextView)

                if (body == null || body.embedded == null || body.embedded.events.isEmpty()) {
                    Log.d("FindTickets", "No events found")
                    noResultsTextView.visibility = View.VISIBLE
                    ticketList.clear()
                    ticketAdapter.notifyDataSetChanged()
                    return
                }

                Log.d("FindTickets", "Found ${body.embedded.events.size} events")

                noResultsTextView.visibility = View.GONE
                ticketList.clear()

                for (event in body.embedded.events) {
                    var venueName = "Unknown venue"
                    var venueAddress = "Unknown address"
                    var price = "Price not available"
                    var imageUrl = ""

                    if (event.embedded.venues.isNotEmpty()) {
                        val venue = event.embedded.venues[0]
                        venueName = venue.name
                        venueAddress = "Address: ${venue.address.line1}, ${venue.city.name}, ${venue.state.stateCode}"
                        Log.d("FindTickets", "Venue: $venueName")
                    }

                    if (event.priceRanges != null && event.priceRanges.isNotEmpty()) {
                        val priceRange = event.priceRanges[0]
                        price = "$${priceRange.min} - $${priceRange.max}"
                    }

                    if (event.images.isNotEmpty()) {
                        val bestImage = event.images.maxByOrNull { it.width * it.height }
                        if (bestImage != null) {
                            imageUrl = bestImage.url
                        }
                    }

                    val ticket = Ticket(
                        eventName = event.name,
                        venueName = venueName,
                        venueAddress = venueAddress,
                        dateTime = formatDateTime(event.dates.start.localDate, event.dates.start.localTime),
                        priceRange = price,
                        imageUrl = imageUrl,
                        ticketUrl = event.url ?: ""
                    )

                    ticketList.add(ticket)
                }

                ticketAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<TicketmasterResponse>, t: Throwable) {
                Log.d("FindTickets", "Failed to fetch events: ${t.message}")
                Toast.makeText(this@FindTickets, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
