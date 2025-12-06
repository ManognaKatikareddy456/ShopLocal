package uk.ac.tees.mad.shoplocal.data.repository

import kotlinx.coroutines.delay
import retrofit2.Response
import uk.ac.tees.mad.shoplocal.data.remote.api.YelpApiService
import uk.ac.tees.mad.shoplocal.data.remote.api.YelpApiServiceSlow
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Business
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.YelpBusinessResponse
import uk.ac.tees.mad.shoplocal.domain.reposiotry.YelpRepository
import javax.inject.Inject

class YelpRepositoryImpl @Inject constructor(
    private val api: YelpApiService,
    private val apiSlow: YelpApiServiceSlow
) : YelpRepository {

    override suspend fun getBusinesses(
        term: String,
        cityName: String
//        latitude: Double,
//        longitude: Double,
    ): Result<YelpBusinessResponse> {
        return try {
            val response = api.getLocalBusinesses(
                term = term,
                city =cityName
//                latitude =latitude ,
//                longitude = longitude
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchPagedBusinesses(
        term: String,
        location: String,
        totalNeeded: Int
    ): Result<List<Business>> {
        return try {
            val allBusinesses = mutableListOf<Business>()
            var offset = 0

            while (allBusinesses.size < totalNeeded) {
                val response = apiSlow.getLocalBusinesses2(
                    term = term,
                    city = location,
                    limit = 20,
                    offset = offset
                )

                if (response.isSuccessful) {
                    val batch = response.body()?.businesses ?: emptyList()
                    allBusinesses.addAll(batch)

                    if (batch.isEmpty()) break
                } else {

                    val errorMsg = response.errorBody()?.string() ?: "API request failed"
                    return Result.failure(Exception("API Error: $errorMsg"))
                }

                offset += 20
                delay(1000)
            }

            Result.success(allBusinesses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}