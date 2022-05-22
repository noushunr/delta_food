package com.deltafood.ui.inquiries.po_inquiry

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.deltafood.R
import com.deltafood.adapter.LocationWiseAdapter
import com.deltafood.adapter.POInquiryAdapter
import com.deltafood.data.model.response.POInquiry
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityPoInquiryBinding
import com.deltafood.interfaces.PoInquiryProductSelectListener
import com.deltafood.ui.inquiries.orders_receive.ProductSelectionActivity
import com.deltafood.ui.inquiries.product_site_stock.ProductSelectionSiteActivity
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.utils.NetworkListener
import com.deltafood.utils.currentDate
import com.deltafood.utils.formattedCurrentDate
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class POInquiryActivity : AppCompatActivity() , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: POInquiryViewModel
    private val factory: POInquiryViewModelFactory by instance()
    private lateinit var binding : ActivityPoInquiryBinding
    private lateinit var appPreferences : PrefManager
    var poNo = ""
    var date = ""
    var productId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoInquiryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, factory).get(POInquiryViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        binding?.editDate.setText(currentDate())
        date = formattedCurrentDate()

        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.editDate.setOnClickListener {
            calendarShow()
        }
        binding?.rlDate.setOnClickListener {
            calendarShow()
        }
        binding?.rlProduct?.setOnClickListener {
            if (!poNo.isNullOrEmpty()){
                var intent = Intent(this, POInquiryProductsActivity::class.java)
                var bundle = Bundle()
                bundle?.putString("po_no",poNo)
                intent.putExtras(bundle)
                startActivityForResult(intent,101)
            }

        }
        binding?.editProduct?.setOnClickListener {
            if (!poNo.isNullOrEmpty()){
                var intent = Intent(this, POInquiryProductsActivity::class.java)
                var bundle = Bundle()
                bundle?.putString("po_no",poNo)
                intent.putExtras(bundle)
                startActivityForResult(intent,101)
            }
        }

        binding?.ivClose?.setOnClickListener {
            productId = ""
            binding?.editProduct.setText(productId!!)
            viewModel?.searchPOInquiries(poNo,productId,date)
            binding?.ivClose.visibility = View.GONE
        }
        binding?.rlPoNumber?.setOnClickListener {
            var intent = Intent(this, PONumberSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.editPoNumber?.setOnClickListener {
            var intent = Intent(this, PONumberSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.btnSearch?.setOnClickListener {

            if (poNo.isNullOrEmpty()){

                Toast.makeText(this,"Please select an order",Toast.LENGTH_LONG).show()
            }else{
                viewModel?.searchPOInquiries(poNo,productId,date)
            }
            
        }
        viewModel?.livePOInquiries?.observe(this){
            binding?.tvTotal.text = "${it.size} Result"
            var adapter = POInquiryAdapter(this,it,object : PoInquiryProductSelectListener {
                override fun onProductClick(products: POInquiry) {

                }

            })
            binding?.rvItems.adapter = adapter
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
                poNo = args?.getString("po_no")!!
                binding?.editPoNumber.setText(poNo!!)
                productId = ""
                binding?.editProduct.setText(productId!!)
                binding?.ivClose.visibility = View.GONE
                var adapter = POInquiryAdapter(this, listOf(),object : PoInquiryProductSelectListener{
                    override fun onProductClick(products: POInquiry) {

                    }

                })
                binding?.rvItems.adapter = adapter
            }
        }
        if (requestCode == 101){
            if (resultCode == RESULT_OK){
                var args = data?.extras
                productId = args?.getString("product_id")!!
                binding?.editProduct.setText(productId!!)
                viewModel?.searchPOInquiries(poNo,productId,date)
                binding?.ivClose.visibility = View.VISIBLE
            }
        }

    }

    private fun calendarShow(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            binding?.editDate.setText("$dayOfMonth/${String.format("%02d", monthOfYear+1)}/$year")
            date = "$year${String.format("%02d", monthOfYear+1)}$dayOfMonth"

        }, year, month, day)

        dpd.show()
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