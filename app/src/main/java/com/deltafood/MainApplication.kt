package com.deltafood

import android.app.Application
import android.content.Context
import com.deltafood.data.network.MyApi
import com.deltafood.data.network.MySoapApi
import com.deltafood.data.network.NetworkConnectionInterceptor
import com.deltafood.data.repositories.InquiryRepositories
import com.deltafood.data.repositories.PurchaseRepositories
import com.deltafood.data.repositories.UserRepositories
import com.deltafood.database.AppDatabase
import com.deltafood.ui.inquiries.location_wise.LocationWiseViewModelFactory
import com.deltafood.ui.inquiries.po_inquiry.POInquiryViewModelFactory
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteViewModelFactory
import com.deltafood.ui.login.LoginViewModelFactory
import com.deltafood.ui.settings.SettingsViewModelFactory
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


/**
 *Created by Noushad on 02-05-22
**/

class MainApplication : Application(), KodeinAware {

    companion object {
        lateinit var instance: MainApplication
            private set
        val appContext: Context
            get() {
                return instance.applicationContext
            }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        /*PaymentConfiguration.init(
            appContext,
            "pk_test_51K0XjgAFS6gWh2ADCEZx6PJEsFpNabkjkm6CemIkoGR96Ue7TdPY8sFljJlW8RzdXMor4ktQmki53kzbUkvhMG3m001nZNnwwe"
        )*/
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))

        bind() from singleton { MyApi(instance()) }
        bind() from singleton { MySoapApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { UserRepositories(instance(), instance()) }
        bind() from singleton { InquiryRepositories(instance(), instance()) }
        bind() from singleton { PurchaseRepositories(instance(), instance(),instance()) }


        bind() from provider { LoginViewModelFactory(instance()) }
        bind() from provider { SettingsViewModelFactory(instance()) }
        bind() from provider { ProductSiteViewModelFactory(instance()) }
        bind() from provider { LocationWiseViewModelFactory(instance()) }
        bind() from provider { POInquiryViewModelFactory(instance()) }
        bind() from provider { PurchaseViewModelFactory(instance()) }
    }

}