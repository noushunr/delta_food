package com.deltafood.data.repositories

import ProductResponse
import XMLCreateResponse
import XMLResponse
import XMLStockCreateResponse
import android.content.Context
import com.deltafood.data.model.request.*
import com.deltafood.data.network.MySoapApi
import com.deltafood.data.network.SafeApiRequest
import com.deltafood.database.AppDatabase
import com.deltafood.database.entities.InterSiteStock
import com.deltafood.database.entities.MiscProducts
import com.deltafood.database.entities.Products
import com.deltafood.database.entities.StockChange

/**
 * Created by Noushad N on 02-05-2022.
 */
class PurchaseRepositories(
    val appContext: Context,
    private val api: MySoapApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getTransactions(
        body: XMLTransactionTypeRequest
    ): XMLResponse {
        return apiRequest {
            api.getTransactions(body)
        }

    }

    suspend fun getUnits(
        body: XMLUnitProuctsRequest
    ): XMLResponse {
        return apiRequest {
            api.getUnits(body)
        }

    }

    suspend fun getStatus(
        body: XMLStatusRequest
    ): XMLResponse {
        return apiRequest {
            api.getStatus(body)
        }

    }

    suspend fun createPurchaseReceipt(
        body: XMLCreatePurchaseReceiptRequest
    ): XMLCreateResponse {
        return apiRequest {
            api.createPurchaseReceipt(body)
        }

    }

    suspend fun createMiscReceipt(
        body: XMLCreateMiscReceiptRequest
    ): XMLCreateResponse {
        return apiRequest {
            api.createMiscReceipt(body)
        }

    }

    suspend fun getTransaction(
        body: XMLTransactionRequest
    ): XMLResponse {
        return apiRequest {
            api.getTransaction(body)
        }

    }

    suspend fun getStockLocations(
        body: XMLStockLocationsRequest
    ): XMLResponse {
        return apiRequest {
            api.getStockLocations(body)
        }

    }

    suspend fun getStock(
        body: XMLStockListRequest
    ): XMLResponse {
        return apiRequest {
            api.getStock(body)
        }

    }

    suspend fun getSites(
        body: XMLSitesRequest
    ): XMLResponse {
        return apiRequest {
            api.getSites(body)
        }

    }

    suspend fun checkProductManaged(
        body: XMLCheckProductSiteRequest
    ): XMLResponse {
        return apiRequest {
            api.checkProductManaged(body)
        }

    }

    suspend fun getProductQuantity(
        body: XMLProductQuantityRequest
    ): ProductResponse {
        return apiRequest {
            api.getProductQuantity(body)
        }

    }

    suspend fun getStockByLot(
        body: XMLLotRequest
    ): XMLResponse {
        return apiRequest {
            api.getStockByLot(body)
        }

    }

    suspend fun createStockChange(
        body: XMLCreateStockChangeRequest
    ): XMLStockCreateResponse {
        return apiRequest {
            api.createStockChange(body)
        }

    }

    fun saveProduct(products: Products) = db?.getProductsDao()?.insert(products)

    fun getAllProducts() = db?.getProductsDao()?.getAllProducts()

    fun deleteAllProducts() = db?.getProductsDao()?.deleteAllProducts()

    fun getProducts() = db?.getProductsDao()?.getProducts()

    fun saveMiscProduct(products: MiscProducts) = db?.getProductsDao()?.insertMisc(products)

    fun getAllMiscProducts() = db?.getProductsDao()?.getAllMiscProducts()

    fun deleteAllMiscProducts() = db?.getProductsDao()?.deleteAllMiscProducts()

    fun getMiscProducts() = db?.getProductsDao()?.getMicProducts()

    fun saveStockProduct(products: StockChange) = db?.getStockChangeDao()?.insert(products)

    fun deleteAllStockProducts() = db?.getStockChangeDao()?.deleteAllProducts()

    fun getStockProducts() = db?.getStockChangeDao()?.getProducts()

    fun saveInterSite(products: InterSiteStock) = db?.getInterSiteDao()?.insert(products)

    fun deleteAllInterSite() = db?.getInterSiteDao()?.deleteAllProducts()

    fun getInterSiteProducts() = db?.getInterSiteDao()?.getProducts()
}