package com.deltafood.ui.stock_inventory.misc_receipt

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deltafood.*
import com.deltafood.data.preferences.PrefManager
import com.deltafood.database.entities.MiscProducts
import com.deltafood.databinding.ActivityMiscellaneousReceiptBinding
import com.deltafood.ui.inquiries.location_wise.LocationSelectionActivity
import com.deltafood.ui.inquiries.product_site_stock.ProductSelectionSiteActivity
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteViewModelFactory
import com.deltafood.ui.scanner.ScannerActivity
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModelFactory
import com.deltafood.ui.stock_inventory.purchase_receipt.StockByLotActivity
import com.deltafood.ui.stock_inventory.purchase_receipt.StockStatusActivity
import com.deltafood.utils.NetworkListener
import com.deltafood.utils.formattedCurrentDate
import com.deltafood.utils.formattedDate
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*


class MiscellaneousReceiptActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()

    private lateinit var viewModelProduct: ProductSiteStockViewModel
    private val factoryProduct: ProductSiteViewModelFactory by instance()
    private lateinit var binding: ActivityMiscellaneousReceiptBinding
    private lateinit var appPreferences: PrefManager
    private var poNo = ""
    private var lineNo = 1000
    private var productId = ""
    private var productName = ""
    private var locManagemnt = ""
    private var snoManagement = ""
    private var qty = ""
    private var status = ""
    private var description = ""
    private var locName = ""
    private var lot = ""
    private var wareHouse = ""
    private var productTextChanges = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiscellaneousReceiptBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        viewModelProduct = ViewModelProvider(this, factoryProduct).get(ProductSiteStockViewModel::class.java)
        viewModelProduct?.listener = this
        appPreferences = PrefManager(this)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        viewModel?.getTransactions()
        viewModel?.transaction?.observe(this) {
            binding?.editTxn.setText(it!!)
        }
        viewModel?.units?.observe(this) {
            binding?.editUnit.setText(it!!)
        }
        binding?.editSupl?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding?.editLot.text = p0
            }

        })

        binding?.rlProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionSiteActivity::class.java)
            startActivityForResult(intent, 101)

        }
        binding?.ivSearchProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionSiteActivity::class.java)
            startActivityForResult(intent, 101)
        }
        binding?.editProduct?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                productTextChanges = true
            }

        })
        binding?.editProduct?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding?.editProduct?.text.toString().isNotEmpty()) {
                    if (productTextChanges) {
                        getProductDetails(binding?.editProduct?.text.toString())
                        productTextChanges = false
                    }
                }
                // code to execute when EditText loses focus
            }
        }
        binding?.ivQrProduct?.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivityForResult(intent, 115)
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
            intent.putExtra("publicName","ZSTKSTA")
            startActivityForResult(intent, 102)
        }
        binding?.rlStatus?.setOnClickListener {
            val intent = Intent(this, StockStatusActivity::class.java)
            intent.putExtra("publicName","ZSTKSTA")
            startActivityForResult(intent, 102)
        }
//
        binding?.editLoc?.setOnClickListener {
            val intent = Intent(this, LocationSelectionActivity::class.java)
            startActivityForResult(intent, 103)
        }
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
        binding?.editExpire.setOnClickListener {
            calendarShow()
        }
        binding?.rlExpiry.setOnClickListener {
            calendarShow()
        }

        var products = viewModel?.getMiscProducts()
        if (!products?.isEmpty()!!){
            showAlert()
        }
        viewModel?.successMessage?.observe(this){
            showAlertSuccess(it!!)
            clear()
        }
        binding?.tvCreate?.setOnClickListener {
            var products = viewModel?.getMiscProducts()
            if (products?.isEmpty()!!){
                Toast.makeText(this,"Please Select a product",Toast.LENGTH_LONG).show()
            }else{
                var inputXML = ""
                products?.forEachIndexed { index, products ->
                    if (index!=0){
                        inputXML += "|"
                    }
                    var site = appPreferences?.setSite?.split(":")?.toTypedArray()  //${formattedCurrentDate()}
                    inputXML = "${inputXML}19;;${site!![0].replace(" ","")};${formattedCurrentDate()};;;;;STKEN;;;" +
                            "$lineNo;${products?.productId};${products?.productName};${products?.quantity};${products?.unit};1;0;;${products?.quantity};${products?.unit};1;${products?.status};" +
                            "${products?.wareHouse};;${products?.loc};;;;${products?.lot};;;;;;;100;${formattedDate(products?.expire!!)};;;;;;"

                }
                viewModel?.createMiscReceipt(inputXML)
            }

        }
        binding?.tvSave?.setOnClickListener {
            if (productId.isNullOrEmpty()){
                Toast.makeText(this,"Please Select a product",Toast.LENGTH_LONG).show()
            }else if (status.isNullOrEmpty()){
                Toast.makeText(this,"Please Select the status",Toast.LENGTH_LONG).show()
            }else if (locName.isNullOrEmpty()){
                Toast.makeText(this,"Please Select the location",Toast.LENGTH_LONG).show()
            }else{
                var products = MiscProducts()
                products?.userId = appPreferences?.userId
                products?.productId = productId
                products?.productName = productName
                products?.line = lineNo?.toString()
                products?.unit = viewModel?.unit.value
                products?.quantity = binding?.editQty?.text?.toString()
                products?.status = status
                products?.loc = binding?.editLoc?.text?.toString()
                products?.wareHouse = wareHouse
                products?.supl = binding?.editSupl?.text?.toString()
                products?.lot = binding?.editLot?.text?.toString()
                products?.serial = binding?.editSerial?.text?.toString()
                products?.expire = binding?.editExpire?.text?.toString()
                viewModel?.saveMiscProduct(products)
                lineNo += 1000
                clear()
                Toast.makeText(this,"Product Saved",Toast.LENGTH_LONG).show()
                binding?.scrollView?.post(Runnable { binding?.scrollView?.scrollTo(0, binding?.editProduct?.bottom) })
            }

        }
    }

    fun clear(){
        productId = ""
        qty = ""
        productName = ""
        status = ""
        description = ""
        locName = ""
        wareHouse = ""
        viewModel?.units.postValue("")
        viewModel?.unit.postValue("")
        binding?.editProduct.setText("")
        binding?.editUnit?.setText("")
        binding?.editQty?.setText("")
        binding?.editSta?.setText("")
        binding?.editLoc?.setText("")
        binding?.editSupl?.setText("")
        binding?.editLot?.setText("")
        binding?.editSerial?.setText("")
        binding?.editExpire?.setText("")
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

    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                productId = args?.getString("product_id")!!
                productName = args?.getString("product_name")!!
                locManagemnt = args?.getString("lot_management")!!
                snoManagement = args?.getString("sno_management")!!
                manageProducts()
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
                wareHouse = args?.getString("ware_house")!!
                binding?.editLoc.setText(locName!!)

            }
        }else if (requestCode == 110) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                lot = args?.getString("lot")!!
                var expDate = args.getString("exp_date")
                binding?.editExpire.setText(expDate)
                binding?.editLot.setText(lot!!)

            }
        }else if (requestCode == 115) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                var id = args?.getString("id")!!
                getProductDetails(id)

            }
        }


    }

    private fun manageProducts(){
        binding?.editProduct.setText("${productId!!} - $productName")
        binding?.editUnit?.setText("")
        binding?.editQty?.setText("")
        binding?.editSta?.setText("")
        binding?.editLoc?.setText("")
        binding?.editSupl?.setText("")
        binding?.editLot?.setText("")
        binding?.editSerial?.setText("")
        binding?.editExpire?.setText("")
        viewModel?.getUnits(productId,"ZMISCUOM")
        if (locManagemnt?.equals("Not managed",ignoreCase = true)){

            binding?.editSupl?.isClickable = false
            binding?.editSupl?.isLongClickable = false
            binding?.editSupl?.isFocusableInTouchMode = false
            binding?.editLot?.isClickable = false
            binding?.editLot?.isLongClickable = false
            binding?.editLot?.isFocusableInTouchMode = false
            binding?.editExpire?.isClickable = false
            binding?.editExpire?.isLongClickable = false
            binding?.editExpire?.isFocusableInTouchMode = false
            binding?.rlSupplier?.background = getDrawable(R.drawable.drawable_edit_text_disabled)
            binding?.llExpiry?.background = getDrawable(R.drawable.drawable_edit_text_disabled)
            binding?.llLot?.background = getDrawable(R.drawable.drawable_edit_text_disabled)

        }else{
            binding?.editSupl?.isClickable = true
            binding?.editSupl?.isLongClickable = true
            binding?.editSupl?.isFocusableInTouchMode = true
            binding?.editLot?.isClickable = true
            binding?.editLot?.isLongClickable = true
            binding?.editLot?.isFocusableInTouchMode = true
            binding?.editExpire?.isClickable = true
            binding?.editExpire?.isLongClickable = true
            binding?.editExpire?.isFocusableInTouchMode = true
            binding?.rlSupplier?.background = getDrawable(R.drawable.drawable_edit_text_normal)
            binding?.llExpiry?.background = getDrawable(R.drawable.drawable_edit_text_normal)
            binding?.llLot?.background = getDrawable(R.drawable.drawable_edit_text_normal)
        }
        if (snoManagement?.equals("Not managed",ignoreCase = true)){
            binding?.editSerial?.isClickable = false
            binding?.editSerial?.isLongClickable = false
            binding?.editSerial?.isFocusableInTouchMode = false
            binding?.rlSerial?.background = resources?.getDrawable(R.drawable.drawable_edit_text_disabled)
        }else{
            binding?.editSerial?.isClickable = true
            binding?.editSerial?.isLongClickable = true
            binding?.editSerial?.isFocusableInTouchMode = true
            binding?.rlSerial?.background = resources?.getDrawable(R.drawable.drawable_edit_text_normal)
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("You have saved products. Do you wish to continue with saved products?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }

        builder.setNegativeButton(R.string.delete) { dialog, which ->
            viewModel?.deleteMiscAll()
        }

        builder.show()
    }

    private fun showAlertSuccess(message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("$message")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }


        builder.show()
    }
    private fun getProductDetails(id:String){
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        lifecycleScope.launch {
            viewModelProduct?.getProductsBySite(mString[0])
        }
        viewModelProduct?.liveProducts?.observe(this) {
            var productFound = false
            run breaking@{
                it?.forEach { products ->
                    if (products?.productId == id) {
                        productId = products.productId
                        productName = products.productName
                        locManagemnt = products.locManagement
                        snoManagement = products.serialNo
                        manageProducts()
                        productFound = true
                        return@breaking
                    }
                }
            }
            if (!productFound){
                showAlertSuccess("Product not found")
                binding?.editProduct?.setText("")
            }
        }
    }
}