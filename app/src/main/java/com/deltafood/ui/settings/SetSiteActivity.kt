package com.deltafood.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.deltafood.interfaces.SiteSelectListener
import com.deltafood.adapter.SitesAdapter
import com.deltafood.data.model.response.Sites
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivitySetSiteBinding
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SetSiteActivity : AppCompatActivity(), KodeinAware, NetworkListener, SiteSelectListener {
    override val kodein by kodein()
    private lateinit var viewModel: SettingsViewModel
    private val factory: SettingsViewModelFactory by instance()
    private lateinit var appPreferences : PrefManager
    var siteName = ""
    private lateinit var binding : ActivitySetSiteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        binding = ActivitySetSiteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.tvApply?.setOnClickListener {
            appPreferences?.setSite = siteName
            finish()
        }
        viewModel.getUserDetails("Bearer ${appPreferences.token}")
        
    }

    override fun onStarted() {
        binding?.progressCircular.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        var lastCheckedPosition = -1
        binding?.progressCircular.visibility = View.GONE
        viewModel?.alSites.forEachIndexed { index, s ->
            if (appPreferences.setSite?.equals(s)!!){

                lastCheckedPosition = index
            }
        }
        var adapter = SitesAdapter(this,viewModel.alSites,lastCheckedPosition,this)
        binding?.rvSites.adapter = adapter
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

    override fun onSiteClick(siteName: String) {
        this.siteName = siteName
    }

    override fun onSitesClick(siteName: Sites) {

    }
}