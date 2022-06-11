package com.deltafood.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deltafood.database.entities.MiscProducts
import com.deltafood.database.entities.Products

/**
 * Created by Noushad N on 29-05-2022.
 */
@Dao
interface LineProductsDAO {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Products?>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: Products?)

    @Query("DELETE FROM products")
    fun deleteAllProducts()

    @Query("SELECT * FROM products")
    fun getProducts(): List<Products?>?

    @Query("SELECT * FROM miscproducts")
    fun getAllMiscProducts(): LiveData<List<MiscProducts?>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMisc(products: MiscProducts?)

    @Query("DELETE FROM miscproducts")
    fun deleteAllMiscProducts()

    @Query("SELECT * FROM miscproducts")
    fun getMicProducts(): List<MiscProducts?>?
}