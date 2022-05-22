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
class InquiryRepositories(
    val appContext: Context,
    private val api: MySoapApi
) : SafeApiRequest() {
    suspend fun getProducts(
        body : XMLOrderProductRequest
    ) : XMLResponse{
        return apiRequest {
            api.getProducts(body)
        }

    }
    suspend fun searchOrders(
        body : XMLOrderSearchRequest
    ) : XMLResponse{
        return apiRequest {
            api.searchProducts(body)
        }

    }
    suspend fun getLocations(
        body : XMLLocationListRequest
    ) : XMLResponse{
        return apiRequest {
            api.getLocations(body)
        }

    }
    suspend fun searchLocations(
        body : XMLLocationSearchRequest
    ) : XMLResponse{
        return apiRequest {
            api.searchLocations(body)
        }

    }

    suspend fun getProductsBySite(
        body : XMLProductRequest
    ) : XMLResponse{
        return apiRequest {
            api.getProductsBySite(body)
        }

    }
    suspend fun searchProductsBySite(
        body : XMLProductSiteSearchRequest
    ) : XMLResponse{
        return apiRequest {
            api.searchProductsBySite(body)
        }

    }

    suspend fun getPurchaseOrderList(
        body : XMLPurchaseOrderListRequest
    ) : XMLResponse{
        return apiRequest {
            api.getPurchaseOrderList(body)
        }

    }

    suspend fun searchPOInquiries(
        body : XMLSearchPoInquiriesListRequest
    ) : ProductResponse{
        return apiRequest {
            api.searchPOInquiries(body)
        }

    }
}