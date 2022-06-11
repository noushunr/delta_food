package com.deltafood.data.model.response

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Noushad N on 12-05-2022.
 */
class StockLocations() : Parcelable {
    var location : String = ""
    var locType : String = ""
    var locCategory : String= ""

    constructor(parcel: Parcel) : this() {
        location = parcel.readString()!!
        locType = parcel.readString()!!
        locCategory = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(location)
        parcel.writeString(locType)
        parcel.writeString(locCategory)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockLocations> {
        override fun createFromParcel(parcel: Parcel): StockLocations {
            return StockLocations(parcel)
        }

        override fun newArray(size: Int): Array<StockLocations?> {
            return arrayOfNulls(size)
        }
    }
}
