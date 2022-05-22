package com.deltafood.ui.stock_inventory.purchase_receipt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.deltafood.adapter.OrderSelectionAdapter
import com.deltafood.adapter.StockStatusAdapter
import com.deltafood.data.model.response.Status
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityOrderSelectionBinding
import com.deltafood.databinding.ActivityStockStatusBinding
import com.deltafood.fragments.FilterFragment
import com.deltafood.fragments.StockFilterFragment
import com.deltafood.interfaces.StatusSelectListener
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class StockStatusActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding : ActivityStockStatusBinding
    private lateinit var appPreferences : PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityStockStatusBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel?.getStatus(mString[0])
        supportActionBar?.hide()
        var adapter = StockStatusAdapter(this, mutableListOf(),object : StatusSelectListener{
            override fun onStatusClick(status: Status) {

                var args = Bundle()
                args.putString("status",status.status)
                args.putString("description",status.description)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        binding?.rvStocks.adapter = adapter
        viewModel?.liveStatus?.observe(this){
            adapter?.submitList(it)
        }

        binding?.layoutBackClick.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, StockFilterFragment.newInstance("",""), "")
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