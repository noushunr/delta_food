package com.deltafood.ui.settings

import androidx.lifecycle.ViewModel
import com.deltafood.R
import com.deltafood.data.model.response.LoginApi
import com.deltafood.data.model.response.UserDetailsApi
import com.deltafood.data.repositories.UserRepositories
import com.deltafood.utils.*

/**
 * Created by Noushad N on 06-05-2022.
 */
class SettingsViewModel(
    private val repository: UserRepositories
) : ViewModel() {
    val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var name: String? = null
    var alSites = listOf<String>()

    fun getUserDetails(token : String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userDetails(
                    token
                )
                checkUserDetailsResponse(response)
            } catch (error: ErrorBodyException) {
                try {
                    println(error.message.toString())
                    val response: UserDetailsApi.UserDetailsResponse = fromJson(error.message.toString())
                    checkUserDetailsResponse(response)
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

    private fun checkUserDetailsResponse(
        response: UserDetailsApi.UserDetailsResponse,
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
            alSites = data.alSites
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }
}