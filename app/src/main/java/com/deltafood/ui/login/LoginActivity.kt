package com.deltafood.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.deltafood.MainActivity
import com.deltafood.data.preferences.PrefManager
import com.deltafood.databinding.ActivityLoginBinding
import com.deltafood.utils.NetworkListener
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), KodeinAware, NetworkListener {
    override val kodein by kodein()
    private lateinit var viewModel: LoginViewModel
    private val factory: LoginViewModelFactory by instance()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var appPreferences : PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreferences = PrefManager(this)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding?.viewmodel = viewModel
        setContentView(binding.root)
        viewModel.listener = this

    }

    override fun onStarted() {

        binding?.progressCircular.visibility = View.VISIBLE
    }


    override fun onSuccess() {
        appPreferences.userName = viewModel.name
        appPreferences.token = viewModel.accessToken
//        appPreferences.isUserLoggedIn = true
        binding?.progressCircular.visibility = View.GONE
//        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFailure() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }

    override fun onError() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }

    override fun onNoNetwork() {
        binding?.progressCircular.visibility = View.GONE
        Toast.makeText(this,viewModel.errorMessage,Toast.LENGTH_LONG).show()
    }
}