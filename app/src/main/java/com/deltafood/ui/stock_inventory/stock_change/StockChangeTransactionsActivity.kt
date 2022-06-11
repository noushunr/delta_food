package com.deltafood.ui.stock_inventory.stock_change

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deltafood.R
import com.deltafood.UnitSelectionFragment
import com.deltafood.data.preferences.PrefManager
import com.deltafood.database.entities.Products
import com.deltafood.database.entities.StockChange
import com.deltafood.databinding.ActivityMiscellaneousReceiptBinding
import com.deltafood.databinding.ActivityStockChangeTransactionsBinding
import com.deltafood.ui.inquiries.location_wise.LocationSelectionActivity
import com.deltafood.ui.inquiries.product_site_stock.ProductSelectionSiteActivity
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModelFactory
import com.deltafood.ui.stock_inventory.purchase_receipt.StockStatusActivity
import com.deltafood.utils.NetworkListener
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class StockChangeTransactionsActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private var productId = ""
    private var productName = ""
    private var transaction = ""
    private var qty = ""
    private var status = ""
    private var description = ""
    private var loc = ""
    private var lot = ""
    private var s_io = ""
    private var serial = ""
    private var locName = ""

    var siteId = ""
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding: ActivityStockChangeTransactionsBinding
    private lateinit var appPreferences: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockChangeTransactionsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        siteId = mString[0]
        binding?.ivQr?.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.QR_CODE))
            intentIntegrator.initiateScan()
        }

        viewModel?.loc?.observe(this) {
            loc = it
        }
        var products = viewModel?.getStockProducts()
        if (!products?.isEmpty()!!) {
            transaction = products.get(0)?.txn!!
            showAlert()
        }
        binding?.rlProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionSiteActivity::class.java)
            startActivityForResult(intent, 101)

        }
        binding?.editProduct?.setOnClickListener {
            var intent = Intent(this, ProductSelectionSiteActivity::class.java)
            startActivityForResult(intent, 101)
        }
        binding?.rlTransaction?.setOnClickListener {
            var intent = Intent(this, TransactionSelectionActivity::class.java)
            intent.putExtra("type", "3")
            intent.putExtra("sub_type", "1")
            startActivityForResult(intent, 105)

        }
        binding?.editTxn?.setOnClickListener {
            var intent = Intent(this, TransactionSelectionActivity::class.java)
            intent.putExtra("type", "3")
            intent.putExtra("sub_type", "1")
            startActivityForResult(intent, 105)
        }

        binding?.rlLot?.setOnClickListener {
            if (productId.isNotEmpty() && loc?.isNotEmpty()!! && status?.isNotEmpty()) {
                val intent = Intent(this, StockSelectionActivity::class.java)
                intent.putExtra("product", productId)
                intent.putExtra("loc", viewModel.loc.value)
                intent.putExtra("status", status)
                startActivityForResult(intent, 106)
            }


        }
        binding?.editLot?.setOnClickListener {
            if (productId.isNotEmpty() && loc?.isNotEmpty()!! && status?.isNotEmpty()) {
                val intent = Intent(this, StockSelectionActivity::class.java)
                intent.putExtra("product", productId)
                intent.putExtra("loc", viewModel.loc.value)
                intent.putExtra("status", status)
                startActivityForResult(intent, 106)
            }
        }
        binding?.editLoc?.setOnClickListener {
            val intent = Intent(this, LocationSelectionActivity::class.java)
            startActivityForResult(intent, 103)
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
        binding?.rlFromIo?.setOnClickListener {
            if (!productId.isNullOrEmpty()) {
                val manager: FragmentManager = supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                transaction.add(
                    binding.clMain.id,
                    StockLocationsFragment.newInstance(viewModel?.alStockLocations, ""),
                    ""
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
        binding?.editFromIo?.setOnClickListener {
            if (!productId.isNullOrEmpty()) {
                val manager: FragmentManager = supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                transaction.add(
                    binding.clMain.id,
                    StockLocationsFragment.newInstance(viewModel?.alStockLocations, ""),
                    ""
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        binding?.editFromSt?.setOnClickListener {
            val intent = Intent(this, StockStatusActivity::class.java)
            intent.putExtra("publicName", "ZSTKSTA")
            startActivityForResult(intent, 102)
        }
        binding?.rlFromSt?.setOnClickListener {
            val intent = Intent(this, StockStatusActivity::class.java)
            intent.putExtra("publicName", "ZSTKSTA")
            startActivityForResult(intent, 102)
        }
        viewModel?.unit?.observe(this) {
            binding?.editUnit.setText(it!!)
        }
        viewModel?.unitCom?.observe(this) {
            binding?.editUnitCon.setText(it!!)
        }
        viewModel?.stockLocations?.observe(this) {
            binding?.editFromIo.setText(it!!)
        }
        viewModel?.successMessage?.observe(this) {
            Toast.makeText(this, "Stock change created", Toast.LENGTH_LONG).show()
            clear()
        }
        binding?.tvSave?.setOnClickListener {
            if (productId.isNullOrEmpty()) {
                Toast.makeText(this, "Please Select a product", Toast.LENGTH_LONG).show()
            } else if (binding?.editQty?.text?.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Quantity cannot be empty", Toast.LENGTH_LONG).show()
            }else if (status.isNullOrEmpty()) {
                Toast.makeText(this, "Quantity cannot be empty", Toast.LENGTH_LONG).show()
            } else {
                var products = StockChange()
                products?.userId = appPreferences?.userId
                products?.txn = binding?.editTxn?.text?.toString()
                products?.productId = productId
                products?.productName = productName
                products.fromLo = viewModel.loc.value
                products?.unit = viewModel?.unit.value
                products?.unitCon = viewModel?.unitCom.value
                products?.quantity = binding?.editQty?.text?.toString()
                products?.status = status
                products?.toSta = status
                products?.loc = binding?.editLoc?.text?.toString()
                products?.lot = binding?.editLot?.text?.toString()
                products?.sIo = binding?.editSio?.text?.toString()
                products?.serial = binding?.editSerial?.text?.toString()
                viewModel?.saveStockProduct(products)
                clear()
                binding?.scrollView?.post(Runnable {
                    binding?.scrollView?.scrollTo(
                        0,
                        binding?.editProduct?.bottom
                    )
                })
            }

        }

        binding?.tvCreate?.setOnClickListener {
            var products = viewModel?.getStockProducts()
            if (products?.isEmpty()!!) {
                Toast.makeText(this, "Please Select a product", Toast.LENGTH_LONG).show()
            } else {
                lifecycleScope.launch {
                    viewModel.createStockChange(
                        siteId,
                        transaction,
                        products!! as List<StockChange>
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkSite()
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

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                clear()
                var args = data?.extras
                productName = args?.getString("product_name")!!
                productId = args?.getString("product_id")!!
                binding?.editProduct.setText("${productId!!} - $productName")
                lifecycleScope.launch {
                    viewModel?.getUnits(productId, "ZMISCUOM")
                }


                lifecycleScope.launch {
                    viewModel?.getStockLocations(productId, siteId)
                }


            }
        } else if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                status = args?.getString("status")!!
                description = args?.getString("description")!!
                binding?.editSta.setText("$status($description)")
                binding?.editFromSt.setText("$status($description)")
            }
        } else if (requestCode == 105) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                transaction = args?.getString("transaction", "")!!
                binding?.editTxn.setText(transaction)
            }
        } else if (requestCode == 106) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                serial = args?.getString("sl_no", "")!!
                s_io = args?.getString("slot", "")!!
                lot = args?.getString("lot", "")!!
                binding?.editSerial.setText(serial)
                binding?.editLot.setText(lot)
                binding?.editSio.setText(s_io)
            }
        } else if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                var args = data?.extras
                locName = args?.getString("loc_name")!!
                binding?.editLoc.setText(locName!!)

            }
        } else {
            var result = IntentIntegrator.parseActivityResult(resultCode, data)
            if (result?.contents != null) {
                var content = result.contents
                Toast.makeText(this, content, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun clear() {
        productId = ""
        qty = ""
        productName = ""
        status = ""
        description = ""
        locName = ""
        viewModel?.units.postValue("")
        viewModel?.unit.postValue("")
        viewModel?.unitCom.postValue("")
        viewModel?.stockLocations?.postValue("")
        binding?.editProduct.setText("")
        binding?.editUnit?.setText("")
        binding?.editUnitCon?.setText("")
        binding?.editQty?.setText("")
        binding?.editSta?.setText("")
        binding?.editLoc?.setText("")
        binding?.editLot?.setText("")
        binding?.editSerial?.setText("")
        binding?.editSio?.setText("")
        binding?.editFromSt?.setText("")
        binding?.editFromIo?.setText("")
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("You have saved products. Do you wish to continue with saved products?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }

        builder.setNegativeButton(R.string.delete) { dialog, which ->
            viewModel?.deleteAllStock()
        }

        builder.show()
    }
}