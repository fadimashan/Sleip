package se.mobileinteraction.sleip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Recording(
    val horse: Int,
    val comment: String?,
    val name: String?,
    val video: String?

) : Parcelable
