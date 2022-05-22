package com.deltafood.ui.stock_inventory.purchase_receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deltafood.data.repositories.InquiryRepositories
import com.deltafood.data.repositories.PurchaseRepositories
import com.deltafood.data.repositories.UserRepositories


/*
 *Created by Adithya T Raj on 25-06-2021
*/

@Suppress("UNCHECKED_CAST")
class PurchaseViewModelFactory(
    private val repository: PurchaseRepositories
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PurchaseViewModel(repository) as T
    }

}