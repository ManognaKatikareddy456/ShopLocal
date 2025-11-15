package uk.ac.tees.mad.shoplocal.presentation.Viewmodels

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.strictmode.SetRetainInstanceUsageViolation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Business
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.YelpBusinessResponse
import uk.ac.tees.mad.shoplocal.domain.usecase.YelpUseCase
import uk.ac.tees.mad.shoplocal.domain.usecase.YelpUseCaseSlowUseCase
import java.util.Locale

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val yelpUseCase: YelpUseCase,
    private val yelpUseCaseSlow: YelpUseCaseSlowUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(YelpScreenData.UiState())
    val uiState: StateFlow<YelpScreenData.UiState> = _uiState

    private val _listOfBusiness = MutableStateFlow(YelpScreenData.ListOfBusiness())
    val listOfBusiness = _listOfBusiness.asStateFlow()


    init {

//        fetchYelpData(
//            term = "shop",
//            latitude = 19.0760,
//            longitude = 72.8777,
//        )
//        fetchYelpData2(
//            term = "restaurant",
//            cityName = "New York"
//        )
    }

    fun fetchYelpData(
        term: String,
        cityName: String
//        latitude: Double,
//        longitude: Double,
    ) {

        viewModelScope.launch {
            yelpUseCase.invoke(
                term = term,
                cityName = cityName
//                latitude = latitude,
//                longitude = longitude,
            ).onStart {
                _uiState.update {
                    YelpScreenData.UiState(isLoading = true)

                }
                Log.d("yelp", "Fetching shop data...")
            }.collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        YelpScreenData.UiState(data = data, isLoading = false)
                    }

                    Log.d("yelp2", "$data")
                }.onFailure { error ->
                    _uiState.update {
                        YelpScreenData.UiState(error = error.message.toString())
                    }

                    Log.e("yelp2", " Error fetching shop data: ${error.message}")
                }
            }
        }

    }

    fun fetchYelpData2(
        term: String, cityName: String,
    ) {

        viewModelScope.launch {
            yelpUseCaseSlow.invoke(
                term = term, cityName = cityName
            ).onStart {
                _listOfBusiness.update {
                    YelpScreenData.ListOfBusiness(isLoading = true)
                }
                Log.d("yelp33", "Fetching shop data...")
            }.collect { result ->
                result.onSuccess { data ->
                    _listOfBusiness.update {
                        YelpScreenData.ListOfBusiness(data = data)

                    }
                    Log.d("yelp33", "$data")

                }.onFailure { error ->

                    _listOfBusiness.update {

                        YelpScreenData.ListOfBusiness(error = error.message.toString())

                    }
                    Log.e("yelp33", "${error.message.toString()}")
                }
            }

        }

    }
}


data object YelpScreenData {

    data class UiState(
        val isLoading: Boolean = false,
        val error: String = "",
        val data: YelpBusinessResponse? = null,

        )

    data class ListOfBusiness(
        val isLoading: Boolean = false,
        val error: String = "",
        val data: List<Business>? = null,
    )


}