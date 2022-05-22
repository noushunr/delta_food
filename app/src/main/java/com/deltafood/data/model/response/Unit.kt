package com.deltafood.data.model.response

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Noushad N on 12-05-2022.
 */
class Unit() : Parcelable {
    var description : String = ""
    var uomCon : String = ""
    var uom : String= ""

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()!!
        uomCon = parcel.readString()!!
        uom = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(uomCon)
        parcel.writeString(uom)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Unit> {
        override fun createFromParcel(parcel: Parcel): Unit {
            return Unit(parcel)
        }

        override fun newArray(size: Int): Array<Unit?> {
            return arrayOfNulls(size)
        }
    }
}
