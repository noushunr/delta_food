package com.deltafood.interfaces

import com.deltafood.data.model.response.Unit

/**
 * Created by Noushad N on 07-05-2022.
 */
interface UnitSelectListener {
    fun onSiteClick(unit: Unit?)
}