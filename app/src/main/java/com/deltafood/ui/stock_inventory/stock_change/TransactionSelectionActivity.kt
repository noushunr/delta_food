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
import com.deltafood.adapter.TransactionListAdapter
import com.deltafood.data.model.response.Transactions
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityLocationSelectionBinding
import com.deltafood.databinding.ActivityTransactionSelectionBinding
import com.deltafood.fragments.LocationSelectionFilterFragment
import com.deltafood.fragments.TransactionFilterFragment
import com.deltafood.interfaces.LocationSelectListener
import com.deltafood.interfaces.TransactionSelectListener
import com.deltafood.ui.inquiries.location_wise.LocationWiseViewModel
import com.deltafood.ui.inquiries.location_wise.LocationWiseViewModelFactory
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModelFactory
import com.deltafood.utils.NetworkListener
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TransactionSelectionActivity : AppCompatActivity(), KodeinAware, NetworkListener
{
    override val kodein by kodein()
    private lateinit var viewModel: PurchaseViewModel
    private val factory: PurchaseViewModelFactory by instance()
    private lateinit var binding : ActivityTransactionSelectionBinding
    private lateinit var appPreferences : PrefManager
    private var type = ""
    private var subType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionSelectionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        type = intent.getStringExtra("type")!!
        subType = intent.getStringExtra("sub_type")!!
        viewModel = ViewModelProvider(this, factory).get(PurchaseViewModel::class.java)
        viewModel?.listener = this
        lifecycleScope.launch {
            viewModel?.getTransaction(type,subType)
        }
        var adapter = TransactionListAdapter(mutableListOf(),object :TransactionSelectListener{
            override fun onTransactionClick(transactions: Transactions) {
                var args = Bundle()
                args.putString("transaction",transactions.code)
                var intent = Intent()
                intent.putExtras(args)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        binding?.rvTransactions?.adapter = adapter
        viewModel?.transactionList?.observe(this){
            adapter?.submitList(it)
            if (it.isNullOrEmpty()){
                binding?.tvEmpty.visibility = View.VISIBLE
                binding?.rvTransactions.visibility = View.GONE
            }else{
                binding?.tvEmpty.visibility = View.GONE
                binding?.rvTransactions.visibility = View.VISIBLE
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