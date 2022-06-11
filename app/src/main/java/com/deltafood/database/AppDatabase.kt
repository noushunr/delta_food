package com.deltafood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.deltafood.database.dao.InterSiteDAO
import com.deltafood.database.dao.LineProductsDAO
import com.deltafood.database.dao.StockChangeDAO
import com.deltafood.database.entities.InterSiteStock
import com.deltafood.database.entities.MiscProducts
import com.deltafood.database.entities.Products
import com.deltafood.database.entities.StockChange

@Database(
    entities = [Products::class,MiscProducts::class,StockChange::class,InterSiteStock::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProductsDao(): LineProductsDAO

    abstract fun getStockChangeDao():StockChangeDAO

    abstract fun getInterSiteDao():InterSiteDAO
    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "dfi_hht.db"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build()

    }



}