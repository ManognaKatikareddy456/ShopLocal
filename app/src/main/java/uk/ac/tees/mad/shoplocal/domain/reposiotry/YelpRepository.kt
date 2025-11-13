package uk.ac.tees.mad.shoplocal.domain.reposiotry

import retrofit2.Response
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Business
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.YelpBusinessResponse

interface YelpRepository {

    suspend fun getBusinesses(
        term: String, latitude: Double,
        longitude: Double,
    ): Result<YelpBusinessResponse>

    suspend fun fetchPagedBusinesses(
        term: String,
        location: String,
        totalNeeded: Int = 100,
    ): Result<List<Business>>
}