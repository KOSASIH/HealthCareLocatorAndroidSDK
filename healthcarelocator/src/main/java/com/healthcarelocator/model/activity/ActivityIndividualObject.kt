package com.healthcarelocator.model.activity

import android.os.Parcel
import android.os.Parcelable
import com.healthcarelocator.extensions.isNullable
import com.healthcarelocator.model.LabelObject
import com.iqvia.onekey.GetActivitiesQuery
import com.iqvia.onekey.GetActivityByIdQuery

class ActivityIndividualObject(var id: String = "", var firstName: String = "", var lastName: String = "",
                               var middleName: String = "", var mailingName: String = "",
                               var specialties: ArrayList<LabelObject> = ArrayList(),
                               var professionalType: LabelObject? = null,
                               var otherActivities: ArrayList<OtherActivityObject> = arrayListOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createTypedArrayList(LabelObject) ?: arrayListOf(),
            parcel.readParcelable(LabelObject::class.java.classLoader),
            parcel.createTypedArrayList(OtherActivityObject) ?: arrayListOf()) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(id)
            writeString(firstName)
            writeString(lastName)
            writeString(middleName)
            writeString(mailingName)
            writeTypedList(specialties)
            writeParcelable(professionalType, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
            writeTypedList(otherActivities)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivityIndividualObject> {
        override fun createFromParcel(parcel: Parcel): ActivityIndividualObject {
            return ActivityIndividualObject(parcel)
        }

        override fun newArray(size: Int): Array<ActivityIndividualObject?> {
            return arrayOfNulls(size)
        }
    }

    /**
     * Convert data from GraphQL
     */
    fun parse(data: GetActivityByIdQuery.Individual?, activity: OtherActivityObject): ActivityIndividualObject {
        if (data.isNullable()) return this
        this.id = data!!.id()
        this.firstName = data.firstName() ?: ""
        this.lastName = data.lastName()
        this.middleName = data.middleName() ?: ""
        this.mailingName = data.mailingName() ?: ""
        this.professionalType = LabelObject().parse(data.professionalType())
        this.specialties = arrayListOf<LabelObject>().apply {
            data.specialties().forEach {
                if (it.label().isNotEmpty())
                    add(LabelObject().parse(it))
            }
        }
        this.otherActivities = arrayListOf<OtherActivityObject>().apply {
            add(activity)
            data.otherActivities().forEach {
                add(OtherActivityObject(it.id(), it.phone() ?: "", null, it.fax() ?: "",
                        it.webAddress()
                                ?: "", ActivityWorkplaceObject().parse(it.workplace(), it.id())))
            }
        }
        return this
    }

    fun parse(data: GetActivitiesQuery.Individual?): ActivityIndividualObject {
        if (data.isNullable()) return this
        this.id = data!!.id()
        this.firstName = data.firstName() ?: ""
        this.lastName = data.lastName()
        this.mailingName = data.mailingName() ?: ""
        this.professionalType = LabelObject().parse(data.professionalType())
        this.specialties = arrayListOf<LabelObject>().apply {
            data.specialties().forEach {
                if (it.label().isNotEmpty())
                    add(LabelObject().parse(it))
            }
        }
        return this
    }
}