package com.deltafood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deltafood.databinding.ActivityStockInventoryBinding
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseReceiptActivity

class StockInventoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStockInventoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInventoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.llPurchase?.setOnClickListener {
            var intent = Intent(this, PurchaseReceiptActivity::class.java)
            startActivity(intent)
        }
    }
}