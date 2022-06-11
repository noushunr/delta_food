package com.deltafood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deltafood.databinding.ActivityStockInventoryBinding
import com.deltafood.ui.stock_inventory.inter_site.InterSiteTransfersActivity
import com.deltafood.ui.stock_inventory.misc_receipt.MiscellaneousReceiptActivity
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseReceiptActivity
import com.deltafood.ui.stock_inventory.stock_change.StockChangeTransactionsActivity

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
        binding?.llMiscellaneous?.setOnClickListener {
            var intent = Intent(this, MiscellaneousReceiptActivity::class.java)
            startActivity(intent)
        }
        binding?.llStockChange?.setOnClickListener {
            var intent = Intent(this, StockChangeTransactionsActivity::class.java)
            startActivity(intent)
        }
        binding?.llInterSite?.setOnClickListener {
            var intent = Intent(this, InterSiteTransfersActivity::class.java)
            startActivity(intent)
        }
    }
}