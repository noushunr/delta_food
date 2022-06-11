package com.deltafood.ui.stock_inventory.purchase_receipt

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.deltafood.*
import com.deltafood.data.preferences.PrefManager
import com.deltafood.database.entities.Products
import com.deltafood.databinding.ActivityPurchaseReceiptBinding
import com.deltafood.ui.inquiries.location_wise.LocationSelectionActivity
import com.deltafood.ui.inquiries.po_inquiry.POInquiryProductsActivity
import com.deltafood.ui.inquiries.po_inquiry.PONumberSelectionActivity
import com.deltafood.ui.scanner.ScannerActivity
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.utils.NetworkListener
import com.deltafood.utils.formattedCurrentDate
import com.deltafood.utils.formattedDate
import com.deltafood.utils.roundOffDecimal
import com.google.zxing.integration.android.IntentIntegrator
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception
import java.nio.file.Files.delete
import java.util.*


class PurchaseReceiptActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding: ActivityPurchaseReceiptBinding
    private lateinit var appPreferences: PrefManager
    private var poNo = ""
    private var supplier = ""
    private var productId = ""
    var productName = ""
    private var popLine = ""
    private var uom = ""
    private var qty = ""
    private var qtyUom = ""
    private var puqty = 0.0
    private var rcpqty = 0.0
    var remQty = 0.0
    var quantity = 0.0
    private var puUom = ""
    private var status = ""
    private var description = ""
    private var locName = ""
    private var lot = ""
    private var subLot = ""
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
        viewModel?.transaction?.observe(this) {
            binding?.editTxn.setText(it!!)
        }
        binding?.ivQrProduct?.setOnClickListener {
            if (!poNo.isNullOrEmpty()) {
                val intent = Intent(this, ScannerActivity::class.java)
                startActivityForResult(intent, 115)
            }
        }
        viewModel?.units?.observe(this) {
            try {
                uom = viewModel?.unit.value!!
                binding?.editUnit.setText(it!!)

                var uomCon = 0.0
                var con = 0.0
                viewModel?.alUnits?.forEach {

                    if (puUom.equals(it.uom)) {
                        if (it.uomCon.isNullOrEmpty() || it.uomCon?.equals("0"))
                            uomCon = 1.0
                        else{
                            uomCon = it.uomCon.toDouble()
                        }

                    }
                    if (uom.equals(it.uom)) {
                        if (it.uomCon.isNullOrEmpty() || it.uomCon?.equals("0"))
                            con = 1.0
                        else
                            con = it.uomCon.toDouble()

                    }
                }
                Log.d("uom",uom)
                Log.d("puUom",puUom)
                Log.d("uomCon",uomCon.toString())
                Log.d("con",con.toString())

                quantity  = roundOffDecimal((remQty * uomCon) / con)!!

                Log.d("remainingqty",quantity.toString())
                binding?.editQty.setText(quantity.toString())
            }catch (e:Exception){

                e.printStackTrace()
            }

        }
        binding?.rlOrder?.setOnClickListener {
            var intent = Intent(this, PONumberSelectionActivity::class.java)
            startActivityForResult(intent, 100)
        }
        binding?.editOrd?.setOnClickListener {
            var intent = Intent(this, PONumberSelectionActivity::class.java)
            startActivityForResult(intent, 100)
        }
        var products = viewModel?.getProducts()
        if (!products?.isEmpty()!!) {
            showAlert()
        }

        viewModel?.successMessage?.observe(this) {
            Toast.makeText(this, "Purchase receipt created", Toast.LENGTH_LONG).show()
            clear()
        }
        viewModel?.productQuantity?.observe(this) {
//            puUom = it.purchaseUnity
            puqty = it.puQuantity
            remQty = roundOffDecimal(qtyUom.toDouble().minus((rcpqty.div(puqty))))!!
            quantity = remQty
            binding?.editQty.setText(remQty.toString())
        }
        binding?.spinnerClos?.setItems("Yes", "No")
        binding?.editSupl?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding?.editLot.text = p0
            }

        })
        binding?.editQty?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    if (p0.toString().isNotEmpty()){
                        if (p0.toString().toDouble() <= quantity) {
//                            binding?.editQty.text = p0
                        } else {
                            binding?.editQty.error = "Quantity exceeded"
                            binding?.editQty.setText("")
                        }
                    }
                }catch (e:Exception){

                }


            }

        })
        binding?.rlProduct?.setOnClickListener {
            if (!poNo.isNullOrEmpty()) {
                var intent = Intent(this, POInquiryProductsActivity::class.java)
                var bundle = Bundle()
                bundle?.putString("po_no", poNo)
                intent.putExtras(bundle)
                startActivityForResult(intent, 101)
            }

        }
        binding?.editProduct?.setOnClickListener {
            if (!poNo.isNullOrEmpty()) {
                var intent = Intent(this, POInquiryProductsActivity::class.java)
                var bundle = Bundle()
                bundle?.putString("po_no", poNo)
                intent.putExtras(bundle)
                startActivityForResult(intent, 101)
            }
        }
        binding?.rlUnit?.setOnClickListener {
            if (!productId.isNullOrEmpty()) {
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
            if (!productId.isNullOrEmpty()) {
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
            intent.putExtra("publicName", "ZSTKSTA")
            startActivityForResult(intent, 102)
        }
        binding?.rlStatus?.setOnClickListener {
            val intent = Intent(this, StockStatusActivity::class.java)
            intent.putExtra("publicName", "ZSTKSTA")
            startActivityForResult(intent, 102)
        }
//
        binding?.ivSearchLot?.setOnClickListener {
            if(productId.isNotEmpty()){
                val intent = Intent(this, StockByLotActivity::class.java)
                intent.putExtra("product_id", productId)
                startActivityForResult(intent, 110)
            }

        }
        binding?.rlLot?.setOnClickListener {
            if(productId.isNotEmpty()){
                val intent = Intent(this, StockByLotActivity::class.java)
                intent.putExtra("product_id", productId)
                startActivityForResult(intent, 110)
            }
        }
        binding?.editLoc?.setOnClickListener {
            val intent = Intent(this, LocationSelectionActivity::class.java)
            startActivityForResult(intent, 103)
        }
        binding?.editExpire.setOnClickListener {
            calendarShow()
        }
        binding?.rlExpiry.setOnClickListener {
            calendarShow()
        }
        binding?.tvCreate?.setOnClickListener {
            var products = viewModel?.getProducts()
            if (products?.isEmpty()!!) {
                Toast.makeText(this, "Please Select a product", Toast.LENGTH_LONG).show()
            } else {
                var inputXML = ""
                products?.forEachIndexed { index, products ->
                    if (index != 0) {
                        inputXML += "|"
                    }
                    var site = appPreferences?.setSite?.split(":")
                        ?.toTypedArray()  //${formattedCurrentDate()}
                    inputXML = "$inputXML${
                        site!![0].replace(
                            " ",
                            ""
                        )
                    };${formattedCurrentDate()};${products?.supplier};STKRE;;;;;${products?.line};" +
                            "${products?.orderNo};${products?.line};1;${products?.id};${products?.unit};${products?.quantityStk};;;${products?.status};" +
                            "${products?.unit};${products?.quantity};1;${products?.loc};${products?.lot};;${products?.sIo};${products?.serial};;${
                                formattedDate(
                                    products?.expire!!
                                )
                            };;;;;0;;;;100;"

                }
                viewModel?.createReceipt(inputXML)
            }

        }
        binding?.tvSave?.setOnClickListener {
            if (productId.isNullOrEmpty()) {
                Toast.makeText(this, "Please Select a product", Toast.LENGTH_LONG).show()

            } else if (binding?.editQty?.text?.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Quantity cannot be empty", Toast.LENGTH_LONG).show()
            }else if (status.isNullOrEmpty()){
                Toast.makeText(this,"Please Select the status",Toast.LENGTH_LONG).show()
            }else if (locName.isNullOrEmpty()){
                Toast.makeText(this,"Please Select the location",Toast.LENGTH_LONG).show()
            } else {
                var products = Products()
                products?.userId = appPreferences?.userId
                products?.orderNo = poNo
                products?.supplier = supplier
                products?.id = productId
                products?.productName = productName
                products?.line = binding?.editLine?.text?.toString()
                products?.unit = viewModel?.unit.value
                products?.quantity = binding?.editQty?.text?.toString()
                products?.quantityStk = qty
                products?.status = status
                products?.loc = binding?.editLoc?.text?.toString()
                products?.supl = binding?.editSupl?.text?.toString()
                products?.lot = binding?.editLot?.text?.toString()
                products?.sIo = binding?.editSio?.text?.toString()
                products?.serial = binding?.editSerial?.text?.toString()
                products?.expire = binding?.editExpire?.text?.toString()
                viewModel?.saveProduct(products)
                clear()

            }

        }

    }

    override fun onResume() {
        super.onResume()
        checkSite()
    }

    private fun calendarShow() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                binding?.editExpire.setText("$dayOfMonth/${monthOfYear + 1}/$year")

            },
            year,
            month,
            day
        )

        dpd.show()
    }

    private fun checkSite() {
        if (appPreferences?.setSite?.isNullOrEmpty() == true) {
            val builder = AlertDialog.Builder(this)
            //set message for alert dialog
            builder.setMessage(R.string.dialogMessage)
            //performing positive action
            builder.setPositiveButton("Ok") { dialogInterface, which ->
                var intent = Intent(this, SetSiteActivity::class.java)
                startActivity(intent)
                dialogInterface?.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        } else {
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
        Toast.makeText(this, viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onError() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this, viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onNoNetwork() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this, viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                poNo = args?.getString("po_no")!!
                supplier = args?.getString("supplier")!!
                binding?.editOrd.setText(poNo!!)
                binding?.editSupp.setText(supplier!!)
                clear()

            }
        } else if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                productName = args?.getString("product_name")!!
                productId = args?.getString("product_id")!!
                productId = args?.getString("product_id")!!
                popLine = args?.getString("pop_line")!!
                uom = args?.getString("uom")!!
                puUom = args?.getString("uom")!!
                qty = args?.getString("quantity")!!
                qtyUom = args?.getString("quantity_uom")!!
                rcpqty = args?.getString("rcp_quantity")!!.toDouble()
                status = ""
                description = ""
                binding?.editProduct.setText("${productId!!} - $productName")
                binding?.editLine.setText(popLine!!)
                binding?.editUnit.setText(uom!!)
                viewModel?.unit?.postValue(uom)
//                binding?.editQty.setText(qty!!)
                viewModel?.getUnits(productId, "ZITMUOM")
                viewModel?.getProductQuantity(productId)
                binding?.editSta.setText("")
                binding?.editSupl.setText("")
                binding?.editLoc?.setText("")
                binding?.editLot?.setText("")
                binding?.editSerial?.setText("")
                binding?.editExpire?.setText("")
            }
        } else if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                status = args?.getString("status")!!
                description = args?.getString("description")!!
                binding?.editSta.setText("$status($description)")
            }
        } else if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                locName = args?.getString("loc_name")!!
                binding?.editLoc.setText(locName!!)

            }
        }else if (requestCode == 110) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                lot = args?.getString("lot")!!
                subLot = args?.getString("sub_lot")!!
                var expDate = args.getString("exp_date")
                binding?.editExpire.setText(expDate)
                binding?.editLot.setText(lot!!)
                binding?.editSio.setText(subLot!!)
            }
        } else if (requestCode == 115) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                var id = args?.getString("id")!!
                binding?.editProduct.setText(id!!)

            }
        }


    }

    fun clear() {
        productId = ""
        popLine = ""
        uom = ""
        qty = ""
        productName = ""
        status = ""
        lot = ""
        subLot = ""
        binding?.editProduct.setText(productId!!)
        binding?.editLine.setText(popLine!!)
        binding?.editUnit.setText(uom!!)
        binding?.editQty.setText(qty!!)
        binding?.editSta.setText("")
        binding?.editSupl.setText("")
        binding?.editLoc?.setText("")
        binding?.editLot?.setText("")
        binding?.editSio?.setText("")
        binding?.editSerial?.setText("")
        binding?.editExpire?.setText("")
        binding?.editProduct?.isFocusable = true
        binding?.scrollView?.post(Runnable {
            binding?.scrollView?.scrollTo(
                0,
                binding?.editProduct?.bottom
            )
        })
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("You have saved products. Do you wish to continue with saved products?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }

        builder.setNegativeButton(R.string.delete) { dialog, which ->
            viewModel?.deleteAll()
        }

        builder.show()
    }
}