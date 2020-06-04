package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable


@Serializable
data class Recording(
    val id: Int,
    val comment: String?,
    val name: String?,
    val uploaded_date: String?,
    val video: String?,
    val horse : Int,
    val analysis : Analysis?
)
