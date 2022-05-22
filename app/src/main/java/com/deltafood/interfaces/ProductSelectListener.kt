package com.deltafood.interfaces

import com.deltafood.data.model.response.Products

/**
 * Created by Noushad N on 07-05-2022.
 */
interface ProductSelectListener {
    fun onProductClick(products : Products)
}