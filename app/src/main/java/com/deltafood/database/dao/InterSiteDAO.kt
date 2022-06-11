package com.deltafood.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deltafood.database.entities.InterSiteStock

/**
 * Created by Noushad N on 29-05-2022.
 */
@Dao
interface InterSiteDAO {
    @Query("SELECT * FROM stockchange")
    fun getAllProducts(): LiveData<List<InterSiteStock?>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: InterSiteStock?)

    @Query("DELETE FROM intersitestock")
    fun deleteAllProducts()

    @Query("SELECT * FROM intersitestock")
    fun getProducts(): List<InterSiteStock?>?

}