package se.mobileinteraction.sleip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Analysis(
    val id: Int,
    val name: String?,
    val video: String?,
    val score: String,
    val comment: String,
    val analysisimage_set: List<AnalysisImage>,
    val json_data: String?,
    val status: AnalysisStatus

) : Parcelable
