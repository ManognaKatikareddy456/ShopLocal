package uk.ac.tees.mad.shoplocal.data.remote.yelpDto

data class YelpBusinessResponse(
    val businesses: List<Business>,
    val total: Int,
    val region: Region
)

data class Business(
    val id: String,
    val name: String,
    val rating: Double,
    val review_count: Int,
    val price: String?,
    val phone: String?,
    val url: String,
    val image_url: String,
    val location: Location,
    val coordinates: Coordinates
)

data class Location(
    val address1: String,
    val city: String,
    val state: String,
    val country: String
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Region(
    val center: Coordinates
)

