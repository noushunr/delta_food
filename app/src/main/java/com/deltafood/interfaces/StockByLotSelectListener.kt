package com.deltafood.interfaces

import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.Stock
import com.deltafood.data.model.response.StockByLot
import com.deltafood.data.model.response.Transactions

/**
 * Created by Noushad N on 07-05-2022.
 */
interface StockByLotSelectListener {
    fun onStockClick(stock: StockByLot)
}