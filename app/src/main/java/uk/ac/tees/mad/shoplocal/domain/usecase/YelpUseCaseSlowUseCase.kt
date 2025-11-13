package uk.ac.tees.mad.shoplocal.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uk.ac.tees.mad.shoplocal.domain.reposiotry.YelpRepository

class YelpUseCaseSlowUseCase(private val yelpRepository: YelpRepository) {

    operator fun invoke(term: String,cityName: String) = flow {
        emit(
            value = yelpRepository.fetchPagedBusinesses(
                term = term,
                location = cityName,

            )
        )
    }.catch { error ->

        emit(Result.failure(error))

    }.flowOn(Dispatchers.IO)
}