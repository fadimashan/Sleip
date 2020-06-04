package se.mobileinteraction.sleip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class RecordingResponse(
    val id: Int,
    val comment: String?,
    val name: String?,
    val uploaded_date: String?,
    val video: String?,
    val horse : Int,
    val analysis : Analysis?,
    var status: AnalysisStatus? = AnalysisStatus.pending
) : Parcelable
