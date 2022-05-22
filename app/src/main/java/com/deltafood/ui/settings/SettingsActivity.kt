package com.deltafood.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityManufacturingBinding
import com.deltafood.databinding.ActivitySettingsBinding
import com.deltafood.ui.login.LoginActivity
import com.deltafood.ui.login.LoginViewModel
import com.deltafood.ui.login.LoginViewModelFactory
import com.deltafood.utils.NetworkListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private lateinit var appPreferences : PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        appPreferences = PrefManager(this)
        supportActionBar?.hide()
        binding?.layoutBackClick?.setOnClickListener {
            finish()
        }
        binding?.llLogout?.setOnClickListener {
            appPreferences?.setSite = ""
            appPreferences?.isUserLoggedIn = false
            appPreferences?.token = ""
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        binding?.llSites?.setOnClickListener {
            var intent = Intent(this, SetSiteActivity::class.java)
            startActivity(intent)
        }

    }
}