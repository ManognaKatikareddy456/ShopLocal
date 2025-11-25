package uk.ac.tees.mad.shoplocal.presentation.navigation

import kotlinx.serialization.Serializable
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Coordinates

sealed class Routes {

    @Serializable
    data object AuthScreen

    @Serializable
    data object SingInScreen

    @Serializable
    data object LogInScreen

    @Serializable
    data object HomeScreen

    @Serializable
    data object ProfileScreen

    @Serializable
    data object MyShops

    @Serializable
    data class ShopDetailArgs(
        val id: String,
        val name: String,
        val rating: String,
        val price:String,
        val reviewCount: String,
        val phone: String,
        val url: String,
        val imageUrl: String,
        val address1: String,
        val city: String,
        val state: String,
        val country: String,
        val latitude: String,
        val longitude: String,
    )
}