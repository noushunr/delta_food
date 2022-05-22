package com.deltafood.ui.stock_inventory.purchase_receipt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.deltafood.adapter.OrderSelectionAdapter
import com.deltafood.databinding.ActivityOrderSelectionBinding
import com.deltafood.fragments.FilterFragment


class OrderSelectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOrderSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSelectionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        var adapter = OrderSelectionAdapter(this)
        binding?.rvOrders.adapter = adapter
        binding?.layoutBackClick.setOnClickListener {
            finish()
        }
        binding?.layoutFilter?.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(binding.clMain.id, FilterFragment.newInstance(1,""), "")
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}