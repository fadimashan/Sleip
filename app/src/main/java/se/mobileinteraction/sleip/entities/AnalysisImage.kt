package se.mobileinteraction.sleip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AnalysisImage (
    val image: String
) : Parcelable

