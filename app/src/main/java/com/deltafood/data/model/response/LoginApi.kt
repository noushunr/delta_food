package com.deltafood.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Noushad N on 02-05-2022.
 */
class LoginApi {
    data class LoginResponse(

        @SerializedName("status")
        var status: Int? = null,

        @SerializedName("error")
        var error: Boolean? = null,

        @SerializedName("messages")
        var messages: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("data")
        var data: LoginDataResponse? = null

    )

    data class LoginDataResponse(

        @SerializedName("userid")
        var userId: String? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("token_type")
        var tokenType: String? = null,

        @SerializedName("access_token")
        var token: String? = null

    )
}