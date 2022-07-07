package com.example.cooltimer

import android.os.Parcelable
import android.os.Parcel

class State : Parcelable {
    var isStarted = false
    var progress = 0
    var secondsRemains = 0

    constructor() {}
    protected constructor(`in`: Parcel) {
        isStarted = `in`.readByte().toInt() != 0
        progress = `in`.readInt()
        secondsRemains = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeByte((if (isStarted) 1 else 0).toByte())
        parcel.writeInt(progress)
        parcel.writeInt(secondsRemains)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<State?> = object : Parcelable.Creator<State?> {
            override fun createFromParcel(`in`: Parcel): State? {
                return State(`in`)
            }

            override fun newArray(size: Int): Array<State?> {
                return arrayOfNulls(size)
            }
        }
    }
}