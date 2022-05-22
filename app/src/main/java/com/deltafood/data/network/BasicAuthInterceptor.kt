package com.deltafood.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Noushad N on 10-05-2022.
 */


class BasicAuthInterceptor() :
    Interceptor {
    private val credentials: String = Credentials.basic("API", "API@123")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}