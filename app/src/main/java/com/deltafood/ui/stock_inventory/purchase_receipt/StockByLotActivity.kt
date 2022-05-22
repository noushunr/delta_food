package com.deltafood.ui.stock_inventory.purchase_receipt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.deltafood.adapter.OrderLineSelectionAdapter
import com.deltafood.databinding.ActivityOrderLineSelectionBinding
import com.deltafood.databinding.ActivityStockByLotBinding
import com.deltafood.fragments.OrderLineSelectionFilterFragment
import com.deltafood.fragments.StockByLotFilterFragment

class StockByLotActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStockByLotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockByLotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        var adapter = OrderLineSelectionAdapter(this)
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
}