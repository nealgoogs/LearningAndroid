package com.example.hw_4

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FindTickets"

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

        val categoryList = listOf(
            "Choose an event category", "Music", "Sports", "Theater", "Family",
            "Arts & Theater", "Concerts", "Comedy", "Dance"
        )
        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this

        cityEditText = findViewById(R.id.cityView)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ticketAdapter = TicketAdapter(ticketList)
        recyclerView.adapter = ticketAdapter

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val city = cityEditText.text.toString().trim()
            if (selectedCategory != "Choose an event category" && city.isNotEmpty()) {
                fetchEvents(selectedCategory, city)
            } else {
                Toast.makeText(this, "Please select a category and enter a city.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
        selectedCategory = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        selectedCategory = ""
    }

    private fun fetchEvents(category: String, city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RetrofitService::class.java)

        api.getEvents(API_KEY, category, city).enqueue(object : Callback<TicketmasterResponse> {
            override fun onResponse(call: Call<TicketmasterResponse>, response: Response<TicketmasterResponse>) {
                Log.d(TAG, "onResponse: $response")
                val body = response.body()
                if (body?.embedded?.events.isNullOrEmpty()) {
                    Toast.makeText(this@FindTickets, "No events found.", Toast.LENGTH_SHORT).show()
                    return
                }

                val events = body!!.embedded!!.events
                ticketList.clear()

                for (event in events) {
                    val venue = event.embedded.venues.firstOrNull()
                    val price = event.priceRanges?.firstOrNull()

                    ticketList.add(
                        Ticket(
                            eventName = event.name,
                            venueName = venue?.name ?: "Unknown venue",
                            venueAddress = venue?.address?.line1 ?: "Unknown address",
                            dateTime = "${event.dates.start.localDate} ${event.dates.start.localTime ?: ""}",
                            priceRange = if (price != null) "$${price.min} - $${price.max}" else "N/A"
                        )
                    )
                }

                ticketAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<TicketmasterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@FindTickets, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
