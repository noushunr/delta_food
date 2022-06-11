package com.deltafood.ui.inquiries.po_inquiry

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
import com.deltafood.adapter.POInquiryAdapter
import com.deltafood.data.model.response.POInquiry
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityPoInquiryBinding
import com.deltafood.databinding.ActivityPoinquiryProductsBinding
import com.deltafood.databinding.ActivityPonumberSelectionBinding
import com.deltafood.fragments.PoInquiryProductFilterFragment
import com.deltafood.fragments.PoOrderFilterFragment
import com.deltafood.interfaces.PoInquiryProductSelectListener
import com.deltafood.utils.NetworkListener
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class POInquiryProductsActivity : AppCompatActivity() , KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: POInquiryViewModel
    private val factory: POInquiryViewModelFactory by instance()
    private lateinit var binding : ActivityPoinquiryProductsBinding
    private lateinit var appPreferences : PrefManager
    var poNo = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var args = intent.extras
        poNo = args?.getString("po_no","")!!
        binding = ActivityPoinquiryProductsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, factory).get(POInquiryViewModel::class.java)
        viewModel?.listener = this
        appPreferences = PrefManager(this)
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        lifecycleScope.launch {
            viewModel?.searchPOInquiries(poNo,"","")
        }

        viewModel?.livePOInquiries?.observe(this){
            var adapter = POInquiryAdapter(this,it,object : PoInquiryProductSelectListener{
                override fun onProductClick(products: POInquiry) {
                    var args = Bundle()
                    args.putString("product_id",products.productId)
                    args.putString("product_name",products.description)
                    args.putString("pop_line",products.lineNo)
                    args.putString("uom",products.uom)
                    args.putString("quantity_uom",products.qtyuom)
                    args.putString("quantity",products.remQty)
                    args.putString("rcp_quantity",products.rcpQty)
                    var intent = Intent()
                    intent.putExtras(args)
                    setResult(RESULT_OK,intent)
                    finish()
                }

            })
            binding?.rvProducts.adapter = adapter
            if (it?.size == 0){
                binding?.tvEmpty.visibility = View.VISIBLE
            }else{
                binding?.tvEmpty.visibility = View.GONE
            }
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, PoInquiryProductFilterFragment.newInstance("",""), "")
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