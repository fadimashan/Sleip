package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable

@Serializable
data class Analysis (
    val id: Int,
    val name: String?,
    val video: String?,
    val score: String,
    val comment: String,
    val analysisImage: List<AnalysisImage>

)
