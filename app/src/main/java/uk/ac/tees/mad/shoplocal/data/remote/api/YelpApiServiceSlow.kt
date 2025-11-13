package uk.ac.tees.mad.shoplocal.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Header
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.YelpBusinessResponse
import retrofit2.Response

import retrofit2.http.Query
import uk.ac.tees.mad.shoplocal.Utils.APIKEY

interface YelpApiServiceSlow {

    @GET("businesses/search")
    suspend fun getLocalBusinesses2(
        @Header("Authorization") authHeader: String = "Bearer bcMnllPl1AmZViP4BY-MXIGoBolZMS8BfYrPiK3BrlZoUFKGblMx9IQ5rbLx58O3sc6rNN255w9j3yh9JMv586_CNDOmn75cYaME3_sIFYQGVCIQZJ7hc-uY7_4CaXYx",
        @Query("term") term: String,
        @Query("location") city: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<YelpBusinessResponse>


}