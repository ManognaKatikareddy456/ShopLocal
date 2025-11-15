package uk.ac.tees.mad.shoplocal.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import uk.ac.tees.mad.shoplocal.Utils.APIKEY
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.YelpBusinessResponse

interface YelpApiService {

    @GET("businesses/search")
    suspend fun getLocalBusinesses(
        @Header("Authorization") authHeader: String = APIKEY,
        @Query("term") term: String,
//        @Query("latitude") latitude: Double,
//        @Query("longitude") longitude: Double,
        @Query("location") city: String,
        @Query("limit") limit: Int = 10
    ): Response<YelpBusinessResponse>





}