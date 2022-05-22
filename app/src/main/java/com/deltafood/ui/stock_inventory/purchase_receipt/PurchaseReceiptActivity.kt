package com.deltafood.ui.stock_inventory.purchase_receipt

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.deltafood.*
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityPurchaseReceiptBinding
import com.deltafood.ui.inquiries.location_wise.LocationSelectionActivity
import com.deltafood.ui.inquiries.po_inquiry.POInquiryProductsActivity
import com.deltafood.ui.inquiries.po_inquiry.PONumberSelectionActivity
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.utils.NetworkListener
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*


class PurchaseReceiptActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding : ActivityPurchaseReceiptBinding
    private lateinit var appPreferences : PrefManager
    private var poNo =""
    private var supplier = ""
    private var productId =""
    private var popLine =""
    private var uom = ""
    private var qty =""
    private var status = ""
    private var description =""
    private var locName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseReceiptBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        viewModel?.getTransactions()
        viewModel?.transaction?.observe(this){
            binding?.editTxn.setText(it!!)
        }
        viewModel?.units?.observe(this){
            binding?.editUnit.setText(it!!)
        }
        binding?.rlOrder?.setOnClickListener {
            var intent = Intent(this, PONumberSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.editOrd?.setOnClickListener {
            var intent = Intent(this, PONumberSelectionActivity::class.java)
            startActivityForResult(intent,100)
        }
        binding?.spinnerClos?.setItems("Yes","No")
        binding?.editSupl?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding?.editLot.text = p0
            }

        })

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
        binding?.rlUnit?.setOnClickListener {
            if (!productId.isNullOrEmpty()){
                val manager: FragmentManager = supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                transaction.add(
                    binding.clMain.id,
                    UnitSelectionFragment.newInstance(viewModel?.alUnits, ""),
                    ""
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
        binding?.editUnit?.setOnClickListener {
            if (!productId.isNullOrEmpty()){
                val manager: FragmentManager = supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                transaction.add(
                    binding.clMain.id,
                    UnitSelectionFragment.newInstance(viewModel?.alUnits, ""),
                    ""
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        binding?.editSta?.setOnClickListener {
            val intent = Intent(this, StockStatusActivity::class.java)
            startActivityForResult(intent,102)
        }
        binding?.rlStatus?.setOnClickListener {
            val intent = Intent(this, StockStatusActivity::class.java)
            startActivityForResult(intent,102)
        }
//
        binding?.editLoc?.setOnClickListener {
            val intent = Intent(this, LocationSelectionActivity::class.java)
            startActivityForResult(intent,103)
        }
        binding?.editExpire.setOnClickListener {
            calendarShow()
        }
        binding?.rlExpiry.setOnClickListener {
            calendarShow()
        }

    }

    override fun onResume() {
        super.onResume()
        checkSite()
    }

    private fun calendarShow(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            binding?.editExpire.setText("$dayOfMonth/${monthOfYear+1}/$year")

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){
            if (resultCode == RESULT_OK){
                var args = data?.extras
                poNo = args?.getString("po_no")!!
                supplier = args?.getString("supplier")!!
                binding?.editOrd.setText(poNo!!)
                binding?.editSupp.setText(supplier!!)
                productId = ""
                popLine = ""
                uom = ""
                qty = ""
                binding?.editProduct.setText(productId!!)
                binding?.editLine.setText(popLine!!)
                binding?.editUnit.setText(uom!!)
                binding?.editQty.setText(qty!!)

            }
        }else if (requestCode == 101){
            if (resultCode == RESULT_OK){
                var args = data?.extras
                productId = args?.getString("product_id")!!
                popLine = args?.getString("pop_line")!!
                uom = args?.getString("uom")!!
                qty = args?.getString("quantity")!!
                binding?.editProduct.setText(productId!!)
                binding?.editLine.setText(popLine!!)
                binding?.editUnit.setText(uom!!)
                binding?.editQty.setText(qty!!)
                viewModel?.getUnits(productId)
            }
        }else if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                status = args?.getString("status")!!
                description = args?.getString("description")!!
                binding?.editSta.setText(description)
            }
        }else if (requestCode == 103){
            if (resultCode == RESULT_OK){
                var args = data?.extras
                locName = args?.getString("loc_name")!!
                binding?.editLoc.setText(locName!!)

            }
        }


    }
}