package com.deltafood.ui.stock_inventory.purchase_receipt

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deltafood.adapter.OrderLineSelectionAdapter
import com.deltafood.adapter.StockByLotListAdapter
import com.deltafood.data.model.response.StockByLot
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityOrderLineSelectionBinding
import com.deltafood.databinding.ActivityStockByLotBinding
import com.deltafood.databinding.ActivityStockSelectionBinding
import com.deltafood.fragments.OrderLineSelectionFilterFragment
import com.deltafood.fragments.StockByLotFilterFragment
import com.deltafood.interfaces.StockByLotSelectListener
import com.deltafood.utils.NetworkListener
import com.deltafood.utils.formatDate
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class StockByLotActivity : AppCompatActivity()  , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var appPreferences : PrefManager
    private lateinit var binding : ActivityStockByLotBinding
    var productId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockByLotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        supportActionBar?.hide()
        productId = intent.getStringExtra("product_id")!!
        lifecycleScope.launch {
            viewModel.getStocksByLot(productId)
        }
        var adapter = StockByLotListAdapter(mutableListOf(),object : StockByLotSelectListener{
            override fun onStockClick(stock: StockByLot) {

                var args = Bundle()
                args.putString("lot",stock.lot)
                args.putString("sub_lot",stock.slot)
                args.putString("exp_date", formatDate(stock.expDate!!))
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        viewModel?.stocksByLotList.observe(this){
            if (it.isEmpty()){
                binding?.rvStockByLot.visibility = View.GONE
                binding?.tvEmpty.visibility = View.VISIBLE
            }else{
                adapter?.submitList(it)
                binding?.tvEmpty.visibility = View.GONE
                binding?.rvStockByLot.visibility = View.VISIBLE
            }

        }
        binding?.rvStockByLot.adapter = adapter
        binding?.layoutBackClick.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, StockByLotFilterFragment.newInstance("",""), "")
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