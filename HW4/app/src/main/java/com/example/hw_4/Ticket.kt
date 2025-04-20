package com.example.hw_4

import com.google.gson.annotations.SerializedName

data class TicketmasterResponse(
    @SerializedName("_embedded")
    val embedded: EmbeddedEvents?
)

data class EmbeddedEvents(
    val events: List<Event>
)

data class Event(
    val name: String,
    val dates: Dates,
    @SerializedName("_embedded")
    val embedded: EventVenues,
    val priceRanges: List<PriceRange>?,
    val images: List<Image>,
    val url: String?
)

data class Dates(
    val start: Start
)

data class Start(
    val localDate: String,
    val localTime: String?
)

data class EventVenues(
    val venues: List<Venue>
)

data class Venue(
    val name: String,
    val address: Address,
    val city: City,
    val state: State
)

data class Address(
    val line1: String
)

data class City(
    val name: String
)

data class State(
    val stateCode: String
)

// Couldn't get this to work???
data class PriceRange(
    val min: Double,
    val max: Double
)

data class Image(
    val url: String,
    val width: Int,
    val height: Int
)


// RecyclerView info
data class Ticket(
    val eventName: String,
    val venueName: String,
    val venueAddress: String,
    val dateTime: String,
    val priceRange: String,
    val imageUrl: String,
    val ticketUrl: String
)
