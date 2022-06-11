package com.deltafood.data.model.response

/**
 * Created by Noushad N on 03-06-2022.
 */
data class StockByLot(
    var expDate: String? = "",
    var lot: String = "",
    var slot: String? = "",
) {
}