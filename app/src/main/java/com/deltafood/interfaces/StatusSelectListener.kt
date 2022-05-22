package com.deltafood.interfaces

import com.deltafood.data.model.response.Status

/**
 * Created by Noushad N on 07-05-2022.
 */
interface StatusSelectListener {
    fun onStatusClick(status: Status)
}