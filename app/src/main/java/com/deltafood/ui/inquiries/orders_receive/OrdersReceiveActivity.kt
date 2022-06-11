package com.deltafood.ui.inquiries.orders_receive

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.deltafood.R
import com.deltafood.adapter.OrdersReceiveAdapter
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityOrdersReceiveBinding
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteViewModelFactory
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.utils.NetworkListener
import com.deltafood.utils.currentDate
import com.deltafood.utils.formattedCurrentDate
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class OrdersReceiveActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: ProductSiteStockViewModel
    private val factory: ProductSiteViewModelFactory by instance()
    private lateinit var binding : ActivityOrdersReceiveBinding
    private lateinit var appPreferences : PrefManager
    var productId :String?= null
    var date :String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersReceiveBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this, factory).get(ProductSiteStockViewModel::class.java)
        viewModel?.listener = this
        supportActionBar?.hide()
        appPreferences = PrefManager(this)
        binding?.editExpire.setText(currentDate())
        date = formattedCurrentDate()

        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.rlProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.editProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.editExpire.setOnClickListener {
            calendarShow()
        }
        binding?.rlExpiry.setOnClickListener {
            calendarShow()
        }
        binding?.btnSearch?.setOnClickListener {
            if (productId==null){
                Toast.makeText(this,"Please select a product",Toast.LENGTH_LONG).show()
            }else{
                var adapter = OrdersReceiveAdapter(this, listOf())
                binding?.rvOrders.adapter = adapter
               searchOrders()
            }
        }
        viewModel?.liveOrders?.observe(this) {
            binding?.tvResults.text = "${it.size} Result"
            var adapter = OrdersReceiveAdapter(this, it)
            binding?.rvOrders.adapter = adapter
            if (it?.size == 0){
                binding?.tvEmpty.visibility = View.VISIBLE
            }else{
                binding?.tvEmpty.visibility = View.GONE
            }

        }

    }
    private fun searchOrders(){
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel.searchOrders(mString[0],productId!!,date!!)
    }
    private fun calendarShow(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            binding?.editExpire.setText("$dayOfMonth/${String.format("%02d", monthOfYear+1)}/$year")
            date = "$year${String.format("%02d", monthOfYear+1)}$dayOfMonth"

        }, year, month, day)

        dpd.show()
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
                searchOrders()
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
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }

    override fun onNoNetwork() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }
}