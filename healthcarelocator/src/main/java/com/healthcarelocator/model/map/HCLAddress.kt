package com.healthcarelocator.model.map

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class HCLAddress(
        @SerializedName("amenity") var amenity: String = "",
        @SerializedName("tourism") var tourism: String = "",
        @SerializedName("house_number") var houseNumber: String = "",
        @SerializedName("road") var road: String = "",
        @SerializedName("neighbourhood") var neighbourhood: String = "",
        @SerializedName("suburb") var suburb: String = "",
        @SerializedName("town") var town: String = "",
        @SerializedName("district") var district: String = "",
        @SerializedName("postcode") var postcode: String = "",
        @SerializedName("country") var country: String = "",
        @SerializedName("country_code") var countryCode: String = "",
        @SerializedName("city") var city: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "", parcel.readString() ?: "") {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(amenity)
            writeString(tourism)
            writeString(houseNumber)
            writeString(road)
            writeString(neighbourhood)
            writeString(suburb)
            writeString(town)
            writeString(district)
            writeString(postcode)
            writeString(country)
            writeString(countryCode)
            writeString(city)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<HCLAddress> {
        override fun createFromParcel(parcel: Parcel): HCLAddress {
            return HCLAddress(parcel)
        }

        override fun newArray(size: Int): Array<HCLAddress?> {
            return arrayOfNulls(size)
        }
    }

}