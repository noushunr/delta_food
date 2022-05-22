package com.deltafood.interfaces

import com.deltafood.data.model.response.POInquiry
import com.deltafood.data.model.response.Products

/**
 * Created by Noushad N on 18-05-2022.
 */
interface PoInquiryProductSelectListener {
    fun onProductClick(products : POInquiry)
}