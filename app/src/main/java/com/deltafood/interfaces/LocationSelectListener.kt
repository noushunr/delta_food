package com.deltafood.interfaces

import com.deltafood.data.model.response.Locations

/**
 * Created by Noushad N on 07-05-2022.
 */
interface LocationSelectListener {
    fun onLocationClick(locations : Locations)
}