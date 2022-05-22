package com.deltafood.ui.inquiries.product_site_stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deltafood.data.repositories.InquiryRepositories
import com.deltafood.data.repositories.UserRepositories


/*
 *Created by Adithya T Raj on 25-06-2021
*/

@Suppress("UNCHECKED_CAST")
class ProductSiteViewModelFactory(
    private val repository: InquiryRepositories
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductSiteStockViewModel(repository) as T
    }

}