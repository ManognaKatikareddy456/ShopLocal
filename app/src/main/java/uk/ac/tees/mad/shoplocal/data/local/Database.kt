package uk.ac.tees.mad.shoplocal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShopEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {


    abstract fun plantDao(): ShopDao


}