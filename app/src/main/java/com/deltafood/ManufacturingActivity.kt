package com.deltafood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deltafood.databinding.ActivityManufacturingBinding

class ManufacturingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManufacturingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManufacturingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
    }
}