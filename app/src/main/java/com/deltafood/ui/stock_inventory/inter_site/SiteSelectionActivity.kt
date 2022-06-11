package com.deltafood.ui.stock_inventory.inter_site

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deltafood.R
import com.deltafood.adapter.SiteListAdapter
import com.deltafood.adapter.StockListAdapter
import com.deltafood.data.model.response.Sites
import com.deltafood.data.model.response.Stock
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivitySiteSelectionBinding
import com.deltafood.databinding.ActivityStockSelectionBinding
import com.deltafood.fragments.SiteFilterFragment
import com.deltafood.fragments.TransactionFilterFragment
import com.deltafood.interfaces.SiteSelectListener
import com.deltafood.interfaces.StockSelectListener
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModelFactory
import com.deltafood.utils.NetworkListener
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SiteSelectionActivity : AppCompatActivity() , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding : ActivitySiteSelectionBinding
    private lateinit var appPreferences : PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiteSelectionBinding.inflate(layoutInflater)
        appPreferences = PrefManager(this)
        setContentView(binding?.root)
        var args = intent.extras

        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        lifecycleScope.launch {
            viewModel?.getSites()
        }
        var adapter = SiteListAdapter(mutableListOf(),object : SiteSelectListener {

            override fun onSiteClick(siteName: String) {

            }

            override fun onSitesClick(sites: Sites) {
                var args = Bundle()
                args.putString("site",sites.site)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        binding?.rvSites?.adapter = adapter
        viewModel?.sitesList?.observe(this){
            adapter?.submitList(it)
            if (it.isNullOrEmpty()){
                binding?.tvEmpty.visibility = View.VISIBLE
                binding?.rvSites.visibility = View.GONE
            }else{
                binding?.tvEmpty.visibility = View.GONE
                binding?.rvSites.visibility = View.VISIBLE
            }
        }
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, SiteFilterFragment.newInstance("",""), "")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        supportActionBar?.hide()

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