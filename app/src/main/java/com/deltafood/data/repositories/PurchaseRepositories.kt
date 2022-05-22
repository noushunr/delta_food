package com.deltafood.data.repositories

import ProductResponse
import XMLResponse
import android.content.Context
import com.deltafood.data.model.request.*
import com.deltafood.data.network.MySoapApi
import com.deltafood.data.network.SafeApiRequest

/**
 * Created by Noushad N on 02-05-2022.
 */
class PurchaseRepositories(
    val appContext: Context,
    private val api: MySoapApi
) : SafeApiRequest() {

    suspend fun getTransactions(
        body : XMLTransactionTypeRequest
    ) : XMLResponse{
        return apiRequest {
            api.getTransactions(body)
        }

    }

    suspend fun getUnits(
        body : XMLUnitProuctsRequest
    ) : XMLResponse{
        return apiRequest {
            api.getUnits(body)
        }

    }

    suspend fun getStatus(
        body : XMLStatusRequest
    ) : XMLResponse{
        return apiRequest {
            api.getStatus(body)
        }

    }

}