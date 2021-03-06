package com.healthcarelocator.model

import android.os.Parcel
import android.os.Parcelable
import com.healthcarelocator.extensions.isNullable
import com.google.gson.annotations.SerializedName
import com.iqvia.onekey.GetActivitiesQuery
import com.iqvia.onekey.GetActivityByIdQuery

class LabelObject(@SerializedName("code") var code: String = "",
                  @SerializedName("label") var label: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(code)
            writeString(label)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabelObject> {
        override fun createFromParcel(parcel: Parcel): LabelObject {
            return LabelObject(parcel)
        }

        override fun newArray(size: Int): Array<LabelObject?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return label
    }

    /**
     * Convert data from GraphQL
     */
    fun parse(data: GetActivityByIdQuery.Role?): LabelObject {
        if (data.isNullable()) return this
        this.code = data!!.code()
        this.label = data.label()
        return this
    }

    fun parse(data: GetActivityByIdQuery.ProfessionalType?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivitiesQuery.ProfessionalType?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivityByIdQuery.County?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivityByIdQuery.City?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivityByIdQuery.Specialty?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        this.code = data.code()
        return this
    }

    fun parse(data: GetActivitiesQuery.County?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivitiesQuery.City?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivitiesQuery.Specialty?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivityByIdQuery.County1?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }

    fun parse(data: GetActivityByIdQuery.City1?): LabelObject {
        if (data.isNullable()) return this
        this.label = data!!.label()
        return this
    }
}