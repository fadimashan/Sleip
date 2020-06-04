package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable


@Serializable
data class MenuItemType(
    val name: String,
    val date: String,
    val description: String
)


