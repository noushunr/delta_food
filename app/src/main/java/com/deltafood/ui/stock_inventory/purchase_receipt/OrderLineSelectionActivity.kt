package com.deltafood.ui.stock_inventory.purchase_receipt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.deltafood.adapter.OrderLineSelectionAdapter
import com.deltafood.adapter.OrderSelectionAdapter
import com.deltafood.databinding.ActivityOrderLineSelectionBinding
import com.deltafood.fragments.FilterFragment
import com.deltafood.fragments.OrderLineSelectionFilterFragment

class OrderLineSelectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOrderLineSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderLineSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        var adapter = OrderLineSelectionAdapter(this)
        binding?.rvOrderLine.adapter = adapter
        binding?.layoutBackClick.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, OrderLineSelectionFilterFragment.newInstance("",""), "")
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}