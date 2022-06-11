package com.deltafood.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Noushad N on 29-05-2022.
 */
@Entity
class Products {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    @SerializedName("product_id")
    @Expose
    var id: String? = null

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @ColumnInfo(name = "product_name")
    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @ColumnInfo(name = "line")
    @SerializedName("line")
    @Expose
    var line: String? = null

    @ColumnInfo(name = "unit")
    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    @Expose
    var quantity: String? = null

    @ColumnInfo(name = "quantity_stk")
    @SerializedName("quantity_stk")
    @Expose
    var quantityStk: String? = null

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    var status: String? = null

    @ColumnInfo(name = "loc")
    @SerializedName("loc")
    @Expose
    var loc: String? = null

    @ColumnInfo(name = "supl")
    @SerializedName("supl")
    @Expose
    var supl: String? = null

    @ColumnInfo(name = "lot")
    @SerializedName("lot")
    @Expose
    var lot: String? = null

    @ColumnInfo(name = "s_io")
    @SerializedName("s_io")
    @Expose
    var sIo: String? = null

    @ColumnInfo(name = "serial")
    @SerializedName("serial")
    @Expose
    var serial: String? = null

    @ColumnInfo(name = "expire")
    @SerializedName("expire")
    @Expose
    var expire: String? = null

    @ColumnInfo(name = "supplier")
    @SerializedName("supplier")
    @Expose
    var supplier: String? = null

    @ColumnInfo(name = "order_no")
    @SerializedName("order_no")
    @Expose
    var orderNo: String? = null
}