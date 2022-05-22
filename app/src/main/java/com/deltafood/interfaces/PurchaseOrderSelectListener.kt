package com.deltafood.interfaces

import com.deltafood.data.model.response.PurchaseOrder

/**
 * Created by Noushad N on 07-05-2022.
 */
interface PurchaseOrderSelectListener {
    fun onOrderClick(purchaseOrder: PurchaseOrder)
}