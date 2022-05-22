package com.deltafood.ui.inquiries.location_wise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.deltafood.adapter.LocationSelectionAdapter
import com.deltafood.data.model.response.Locations
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityLocationSelectionBinding
import com.deltafood.fragments.LocationSelectionFilterFragment
import com.deltafood.interfaces.LocationSelectListener
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteViewModelFactory
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LocationSelectionActivity : AppCompatActivity(), KodeinAware, NetworkListener,LocationSelectListener
{
    override val kodein by kodein()
    private lateinit var viewModel: LocationWiseViewModel
    private val factory: LocationWiseViewModelFactory by instance()
    private lateinit var binding : ActivityLocationSelectionBinding
    private lateinit var appPreferences : PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationSelectionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        appPreferences = PrefManager(this)
        viewModel = ViewModelProvider(this, factory).get(LocationWiseViewModel::class.java)
        viewModel?.listener = this
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel?.getLocations(mString[0])
        var adapter = LocationSelectionAdapter(this, mutableListOf(),this)
        binding?.rvProducts.adapter = adapter
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, LocationSelectionFilterFragment.newInstance("",""), "")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        viewModel?.liveLocations?.observe(this) {
            adapter?.submitList(it)
        }
    }

    override fun onStarted() {
        binding?.progressCircular.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding?.progressCircular.visibility = View.GONE
    }

    override fun onFailure() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onError() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onNoNetwork() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onLocationClick(locations: Locations) {
        var args = Bundle()
        args.putString("loc_name",locations.locationName)
        args.putString("loc_cat",locations.locCategory)
        var intent = Intent()
        intent.putExtras(args)
        setResult(RESULT_OK,intent)
        finish()
    }
}