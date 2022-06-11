package com.deltafood.ui.inquiries.po_inquiry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.deltafood.R
import com.deltafood.adapter.OrderSelectionAdapter
import com.deltafood.adapter.POOrderSelectionAdapter
import com.deltafood.data.model.response.PurchaseOrder
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityOrderSelectionBinding
import com.deltafood.databinding.ActivityPonumberSelectionBinding
import com.deltafood.fragments.FilterFragment
import com.deltafood.fragments.PoOrderFilterFragment
import com.deltafood.interfaces.PurchaseOrderSelectListener
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteViewModelFactory
import com.deltafood.utils.NetworkListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class PONumberSelectionActivity : AppCompatActivity() , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: POInquiryViewModel
    private val factory: POInquiryViewModelFactory by instance()
    private lateinit var binding : ActivityPonumberSelectionBinding
    private lateinit var appPreferences : PrefManager
    private lateinit var adapter : POOrderSelectionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPonumberSelectionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, factory).get(POInquiryViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        var site = appPreferences?.setSite
        val mString = site!!.split(":").toTypedArray()
        viewModel?.getPurchaseOrderList(mString[0])
        adapter = POOrderSelectionAdapter(this, mutableListOf(),object : PurchaseOrderSelectListener{
            override fun onOrderClick(purchaseOrder: PurchaseOrder) {
                var args = Bundle()
                args.putString("po_no",purchaseOrder.poNo)
                args.putString("supplier",purchaseOrder.vendorId)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        binding?.rvOrders.adapter = adapter
        binding?.layoutBackClick.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, PoOrderFilterFragment.newInstance(1,""), "")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        viewModel?.liveOrders?.observe(this){
            adapter?.submitList(it)
            if (it?.size == 0){
                binding?.tvEmpty.visibility = View.VISIBLE
            }else{
                binding?.tvEmpty.visibility = View.GONE
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
        Toast.makeText(this,viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onNoNetwork() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }
}