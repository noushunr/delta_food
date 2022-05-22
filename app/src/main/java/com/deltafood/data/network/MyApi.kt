package com.deltafood.data.network

import com.deltafood.data.model.response.LoginApi
import com.deltafood.data.model.response.UserDetailsApi
import com.deltafood.utils.BASE_API_URL

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


/*
 *Created by Noushad on 02-05-2022
*/

interface MyApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("user_name") email: String,
        @Field("password") password: String
    ) : Response<LoginApi.LoginResponse>

    @POST("user-details")
    suspend fun getUserDetails(
        @Header("Authorization") authHeader: String
    ) : Response<UserDetailsApi.UserDetailsResponse>


    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .callTimeout(300, TimeUnit.SECONDS)
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BASE_API_URL)
//                .baseUrl(TEST_BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}