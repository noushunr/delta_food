package com.deltafood.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deltafood.database.entities.MiscProducts
import com.deltafood.database.entities.Products
import com.deltafood.database.entities.StockChange

/**
 * Created by Noushad N on 29-05-2022.
 */
@Dao
interface StockChangeDAO {
    @Query("SELECT * FROM stockchange")
    fun getAllProducts(): LiveData<List<StockChange?>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: StockChange?)

    @Query("DELETE FROM stockchange")
    fun deleteAllProducts()

    @Query("SELECT * FROM stockchange")
    fun getProducts(): List<StockChange?>?

}