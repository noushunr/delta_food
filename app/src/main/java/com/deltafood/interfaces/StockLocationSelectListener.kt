package com.deltafood.interfaces

import com.deltafood.data.model.response.StockLocations
import com.deltafood.data.model.response.Unit

/**
 * Created by Noushad N on 07-05-2022.
 */
interface StockLocationSelectListener {
    fun onLocationClick(stockLocations: StockLocations)
}