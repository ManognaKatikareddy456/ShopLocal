package uk.ac.tees.mad.shoplocal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("shop_table")
class ShopEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val rating: String,
    val price: String,
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