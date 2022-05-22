package com.deltafood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deltafood.databinding.ActivityInquiryBinding
import com.deltafood.ui.inquiries.orders_receive.OrdersReceiveActivity
import com.deltafood.ui.inquiries.location_wise.LocationWiseActivity
import com.deltafood.ui.inquiries.po_inquiry.POInquiryActivity
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockActivity

class InquiryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInquiryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInquiryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.llOrders.setOnClickListener {
            var intent = Intent(this, OrdersReceiveActivity::class.java)
            startActivity(intent)
        }
        binding?.llLocationWise.setOnClickListener {
            var intent = Intent(this,LocationWiseActivity::class.java)
            startActivity(intent)
        }
        binding?.llProductSite.setOnClickListener {
            var intent = Intent(this,ProductSiteStockActivity::class.java)
            startActivity(intent)
        }
        binding?.llPoInquiry.setOnClickListener {
            var intent = Intent(this,POInquiryActivity::class.java)
            startActivity(intent)
        }
    }
}