package uk.ac.tees.mad.shoplocal.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uk.ac.tees.mad.shoplocal.domain.reposiotry.YelpRepository

class YelpUseCase(private val yelpRepository: YelpRepository) {


    operator fun invoke(
        term: String,
        cityName: String,

//                        latitude: Double,
//                         longitude: Double,
    ) = flow {
        emit(
            value = yelpRepository.getBusinesses(
                term = term,
                cityName = cityName
//                latitude = latitude,
//                longitude = longitude,
            )
        )
    }.catch { error ->

        emit(Result.failure(error))

    }.flowOn(Dispatchers.IO)


}