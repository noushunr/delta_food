package com.deltafood.ui.inquiries.orders_receive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.deltafood.R
import com.deltafood.adapter.OrderLineSelectionAdapter
import com.deltafood.adapter.ProductSelectionAdapter
import com.deltafood.data.model.response.Products
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityProductSelectionBinding
import com.deltafood.fragments.OrderLineSelectionFilterFragment
import com.deltafood.fragments.ProductSelectioinFilterFragment
import com.deltafood.interfaces.ProductSelectListener
import com.deltafood.interfaces.ProductSelectionFilterListener
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteViewModelFactory
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductSelectionActivity : AppCompatActivity(), KodeinAware, NetworkListener,
    ProductSelectionFilterListener {
    override val kodein by kodein()
    private lateinit var viewModel: ProductSiteStockViewModel
    private val factory: ProductSiteViewModelFactory by instance()
    private lateinit var binding : ActivityProductSelectionBinding
    private lateinit var appPreferences : PrefManager
    private lateinit var adapter: ProductSelectionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSelectionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        appPreferences = PrefManager(this)
        viewModel = ViewModelProvider(this, factory).get(ProductSiteStockViewModel::class.java)
        viewModel?.listener = this
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel?.getProducts(mString[0])
        adapter = ProductSelectionAdapter(this, mutableListOf(),object :ProductSelectListener{
            override fun onProductClick(products: Products) {
                var args = Bundle()
                args.putString("product_name",products.productName)
                args.putString("product_id",products.productId)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()

            }

        })
        binding?.rvProducts.adapter = adapter
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        viewModel?.liveProducts?.observe(this) {
            if (it!=null){
                adapter?.submitList(it)
                if (it?.size == 0){
                    binding?.tvEmpty.visibility = View.VISIBLE
                }else{
                    binding?.tvEmpty.visibility = View.GONE
                }
            }
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, ProductSelectioinFilterFragment.newInstance("",""), "")
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
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }

    override fun onNoNetwork() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }

    override fun applyFilter(products: Products) {

    }
}