package se.mobileinteraction.sleip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Horse (
    var id: Int ,
    var name: String,
    var date_of_birth: String? ,
    var description: String?  ,
    var image: String?,
    var user_id: Int
) : Parcelable
