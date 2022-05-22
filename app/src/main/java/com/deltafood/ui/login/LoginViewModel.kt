package com.deltafood.ui.login

import androidx.lifecycle.ViewModel
import com.deltafood.R
import com.deltafood.data.model.response.LoginApi
import com.deltafood.data.repositories.UserRepositories
import com.deltafood.utils.*

/**
 * Created by Noushad N on 02-05-2022.
 */
class LoginViewModel(
    private val repository: UserRepositories
) : ViewModel() {
    val appContext by lazy {
        repository.appContext
    }

    var userName: String? = null
    var password: String? = null

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var name: String? = null
    var accessToken: String? = null

    fun login() {


        val mEmail = userName
        val mPassword = password

        if (mEmail.isNullOrEmpty() || mPassword.isNullOrEmpty()) {
            errorMessage = appContext.getString(R.string.all_field_mandate)
            listener?.onFailure()
            return
        }

        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userLogin(
                    mEmail, mPassword
                )

                checkLoginResponse(response, mEmail, mPassword)

            } catch (error: ErrorBodyException) {
                try {
                    println(error.message.toString())
                    val response: LoginApi.LoginResponse = fromJson(error.message.toString())

                    checkLoginResponse(response, mEmail, mPassword)
                } catch (e: Exception) {
                    errorMessage = appContext.getString(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getString(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkLoginResponse(
        response: LoginApi.LoginResponse,
        mEmail: String,
        mPassword: String
    ) {

        val data = response.data
        var message = response.message
        val status = response.error
        if (status != null && status) {
            message = response.messages
        }

        errorMessage = message!!

        if (data != null && status != null && !status!!) {
//            errorMessage = response.data?.name.toString()
            name = data?.name
            accessToken = data?.token
            listener?.onSuccess()
        } else {
            listener?.onError()
        }

        println(response.toString())
    }
}