package uk.ac.tees.mad.shoplocal.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    @Upsert()
    suspend fun insert(shopEntity: ShopEntity)

    @Query("DELETE FROM shop_table WHERE Id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM shop_table WHERE Id IN (:ids) ORDER BY name ASC")
    fun getPlantsByIds(ids: List<String>): Flow<List<ShopEntity>>


}