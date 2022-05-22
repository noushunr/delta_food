package com.deltafood

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityMainBinding
import com.deltafood.ui.settings.SetSiteActivity
import com.deltafood.ui.settings.SettingsActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appPreferences : PrefManager
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        appPreferences = PrefManager(this)
        val builder = SpannableStringBuilder()

        val red = "Hey, "
        val redSpannable = SpannableString(red)
        redSpannable.setSpan(ForegroundColorSpan(getColor(R.color.black)), 0, red.length, 0)
        builder.append(redSpannable)

        val white = "${appPreferences.userName}!"
        val whiteSpannable = SpannableString(white)
        whiteSpannable.setSpan(ForegroundColorSpan(getColor(R.color.colorPrimary)), 0, white.length, 0)
        builder.append(whiteSpannable)
        binding?.tvName.setText(builder, TextView.BufferType.SPANNABLE)
        binding?.llStock.setOnClickListener {
            var intent = Intent(this,StockInventoryActivity::class.java)
            startActivity(intent)
        }
        binding?.llInquiry.setOnClickListener {
            var intent = Intent(this,InquiryActivity::class.java)
            startActivity(intent)
        }
        binding?.llManufacturing.setOnClickListener {
            var intent = Intent(this,ManufacturingActivity::class.java)
            startActivity(intent)
        }
        binding?.llSettings.setOnClickListener {
            var intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val builder = AlertDialog.Builder(this)
//        //set message for alert dialog
//        builder.setMessage(R.string.logout_message)
//        //performing positive action
//        builder.setPositiveButton("Yes"){dialogInterface, which ->
//            super.onBackPressed()
//        }
//        builder.setNegativeButton("Cancel"){dialogInterface, which ->
//
//        }
//        val alertDialog: AlertDialog = builder.create()
//        // Set other dialog properties
//        alertDialog.setCancelable(false)
//        alertDialog.show()
    }
}