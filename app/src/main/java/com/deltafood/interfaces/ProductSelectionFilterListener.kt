package com.deltafood.interfaces

import com.deltafood.data.model.response.Products

/**
 * Created by Noushad N on 13-05-2022.
 */
interface ProductSelectionFilterListener {
    fun applyFilter(products: Products)
}