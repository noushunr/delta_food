package com.deltafood.ui.inquiries.location_wise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.deltafood.R
import com.deltafood.adapter.LocationWiseAdapter
import com.deltafood.adapter.OrdersReceiveAdapter
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityLocationWiseBinding
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LocationWiseActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: LocationWiseViewModel
    private val factory: LocationWiseViewModelFactory by instance()
    private lateinit var appPreferences : PrefManager
    private lateinit var binding: ActivityLocationWiseBinding
    var locName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationWiseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        appPreferences = PrefManager(this)
        viewModel = ViewModelProvider(this, factory).get(LocationWiseViewModel::class.java)
        viewModel?.listener = this
        var adapter = LocationWiseAdapter(this, listOf())
        binding?.rvLocation.adapter = adapter
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.rlLocation?.setOnClickListener {
            val intent = Intent(this,LocationSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.editLocation?.setOnClickListener {
            val intent = Intent(this,LocationSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.btnSearch?.setOnClickListener {
            if (locName.isNullOrEmpty()){
                Toast.makeText(this,"Please select a location", Toast.LENGTH_LONG).show()
            }else{
                var adapter = LocationWiseAdapter(this, listOf())
                binding?.rvLocation.adapter = adapter
                searchLocations()
            }
        }
        viewModel?.liveSearchedList?.observe(this) {
            binding?.tvResults.text = "${it.size} Result"
            var adapter = LocationWiseAdapter(this, it)
            binding?.rvLocation.adapter = adapter
        }
    }
    private fun searchLocations(){
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel.searchLocations(mString[0],locName)
    }
    private fun checkSite(){
        if (appPreferences?.setSite?.isNullOrEmpty() == true){
            val builder = AlertDialog.Builder(this)
            //set message for alert dialog
            builder.setMessage(R.string.dialogMessage)
            //performing positive action
            builder.setPositiveButton("Ok"){dialogInterface, which ->
                var intent = Intent(this, SetSiteActivity::class.java)
                startActivity(intent)
                dialogInterface?.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }else{
            binding?.tvSiteName.text = appPreferences?.setSite
        }
    }

    override fun onResume() {
        super.onResume()
        checkSite()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){
            if (resultCode == RESULT_OK){
                var args = data?.extras
                locName = args?.getString("loc_name")!!
                var locCat = args?.getString("loc_cat")
                binding?.editLocation.setText(locName!!)
                binding?.editCat.setText(locCat!!)
                searchLocations()
            }
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
}