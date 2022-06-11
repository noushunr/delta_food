package com.deltafood.interfaces

import com.deltafood.data.model.response.Sites

/**
 * Created by Noushad N on 07-05-2022.
 */
interface SiteSelectListener {
    fun onSiteClick(siteName : String)
    fun onSitesClick(sites : Sites)
}