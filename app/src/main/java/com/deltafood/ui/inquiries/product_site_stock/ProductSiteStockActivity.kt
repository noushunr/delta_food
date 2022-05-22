package com.deltafood.ui.inquiries.product_site_stock

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
import com.deltafood.databinding.ActivityOrdersReceiveBinding
import com.deltafood.databinding.ActivityProductSiteStockBinding
import com.deltafood.ui.inquiries.orders_receive.ProductSelectionActivity
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductSiteStockActivity : AppCompatActivity() , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: ProductSiteStockViewModel
    private val factory: ProductSiteViewModelFactory by instance()
    var productId :String?= null
    private lateinit var binding : ActivityProductSiteStockBinding
    private lateinit var appPreferences : PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSiteStockBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, factory).get(ProductSiteStockViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        
        var adapter = LocationWiseAdapter(this, listOf())
        binding?.rvLocation.adapter = adapter
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.rlProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionSiteActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.editProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionSiteActivity::class.java)
            startActivityForResult(intent,100)
        }

        binding?.btnSearch?.setOnClickListener {
            if (productId.isNullOrEmpty()){
                Toast.makeText(this,"Please select a product", Toast.LENGTH_LONG).show()
            }else{
                var adapter = LocationWiseAdapter(this, listOf())
                binding?.rvLocation.adapter = adapter
                searchProducts()
            }
        }

        viewModel?.liveSearchedList?.observe(this) {
            binding?.tvResults.text = "${it.size} Results"
            var adapter = LocationWiseAdapter(this, it)
            binding?.rvLocation.adapter = adapter
        }
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
                var productName = args?.getString("product_name")
                productId = args?.getString("product_id")
                binding?.editProduct.setText(productName!!)
                searchProducts()
            }
        }
    }

    private fun searchProducts(){
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel.searchProducts(mString[0],productId!!)
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