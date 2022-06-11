package com.deltafood.ui.inquiries.product_site_stock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deltafood.adapter.ProductSelectionAdapter
import com.deltafood.data.model.response.Products
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityProductSelectionSiteBinding
import com.deltafood.fragments.ProductSiteFilterFragment
import com.deltafood.interfaces.ProductSelectListener
import com.deltafood.utils.NetworkListener
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

class ProductSelectionSiteActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: ProductSiteStockViewModel
    private val factory: ProductSiteViewModelFactory by instance()
    private lateinit var binding : ActivityProductSelectionSiteBinding
    private lateinit var appPreferences : PrefManager
    private lateinit var adapter: ProductSiteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSelectionSiteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, factory).get(ProductSiteStockViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        lifecycleScope.launch {
            viewModel?.getProductsBySite(mString[0])
        }

        adapter = ProductSiteAdapter(this, mutableListOf(),object : ProductSelectListener {
            override fun onProductClick(products: Products) {
                var args = Bundle()
                args.putString("product_name",products.productName)
                args.putString("product_id",products.productId)
                args.putString("lot_management",products.locManagement)
                args.putString("sno_management",products.serialNo)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()

            }

        })
        viewModel?.liveProducts?.observe(this) {
            if (it!=null){
                adapter?.submitList(it)
            }
        }
        binding?.rvProducts.adapter = adapter
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, ProductSiteFilterFragment.newInstance("",""), "")
            transaction.addToBackStack(null)
            transaction.commit()
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