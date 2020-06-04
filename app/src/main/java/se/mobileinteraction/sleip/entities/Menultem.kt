package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable


@Serializable
data class Menultem (
    val title: String,
    val text: String,
    val isEditable: Boolean,
    val type: MenuItemType

)
