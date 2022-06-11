package com.deltafood.ui.stock_inventory.stock_change

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
import com.deltafood.adapter.StockListAdapter
import com.deltafood.adapter.TransactionListAdapter
import com.deltafood.data.model.response.Stock
import com.deltafood.data.model.response.Transactions
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityStockSelectionBinding
import com.deltafood.databinding.ActivityTransactionSelectionBinding
import com.deltafood.fragments.TransactionFilterFragment
import com.deltafood.interfaces.StockSelectListener
import com.deltafood.interfaces.TransactionSelectListener
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModelFactory
import com.deltafood.utils.NetworkListener
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class StockSelectionActivity : AppCompatActivity() , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding : ActivityStockSelectionBinding
    private lateinit var appPreferences : PrefManager
    var product = ""
    var loc = ""
    var status =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockSelectionBinding.inflate(layoutInflater)
        appPreferences = PrefManager(this)
        setContentView(binding?.root)
        var args = intent.extras
        product = args?.getString("product","")!!
        loc = args?.getString("loc","")!!
        status = args?.getString("status","")!!
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        lifecycleScope.launch {
            viewModel?.getStocks(mString[0],product,loc, status)
        }
        var adapter = StockListAdapter(mutableListOf(),object : StockSelectListener {
            override fun onStockClick(stock: Stock) {
                var args = Bundle()
                args.putString("sl_no",stock.serial)
                args.putString("lot",stock.lot)
                args.putString("slot",stock.slot)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        binding?.rvStocks?.adapter = adapter
        viewModel?.stocksList?.observe(this){
            adapter?.submitList(it)
            if (it.isNullOrEmpty()){
                binding?.tvEmpty.visibility = View.VISIBLE
                binding?.rvStocks.visibility = View.GONE
            }else{
                binding?.tvEmpty.visibility = View.GONE
                binding?.rvStocks.visibility = View.VISIBLE
            }
        }
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, TransactionFilterFragment.newInstance("",""), "")
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