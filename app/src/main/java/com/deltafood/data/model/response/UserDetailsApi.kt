package com.deltafood.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Noushad N on 02-05-2022.
 */
class UserDetailsApi {
    data class UserDetailsResponse(

        @SerializedName("status")
        var status: Int? = null,

        @SerializedName("error")
        var error: Boolean? = null,

        @SerializedName("messages")
        var messages: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("data")
        var data: UserDataResponse? = null

    )

    data class UserDataResponse(

        @SerializedName("id")
        var id: String? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("sites")
        var alSites: List<String> = listOf(),

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("functionality")
        var alFunctionalities: List<String> = listOf(),

        @SerializedName("functionality_sub")
        var alSubFunctionalities: List<String> = listOf()

    )
}