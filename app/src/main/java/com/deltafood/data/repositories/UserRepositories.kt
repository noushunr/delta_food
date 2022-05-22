package com.deltafood.data.repositories

import android.content.Context
import com.deltafood.data.model.response.LoginApi
import com.deltafood.data.model.response.UserDetailsApi
import com.deltafood.data.network.MyApi
import com.deltafood.data.network.SafeApiRequest

/**
 * Created by Noushad N on 02-05-2022.
 */
class UserRepositories(
    val appContext: Context,
    private val api: MyApi
) : SafeApiRequest() {
    suspend fun userLogin(
        name: String,
        password: String
    ): LoginApi.LoginResponse {
        return apiRequest {
            api.userLogin(name, password)
        }
    }

    suspend fun userDetails(
        token: String,
    ): UserDetailsApi.UserDetailsResponse {
        return apiRequest {
            api.getUserDetails(token)
        }
    }
}