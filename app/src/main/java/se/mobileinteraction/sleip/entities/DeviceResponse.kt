package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable


@Serializable
data class DeviceResponse (
    val id: String,
    val name: String?,
    val registration_id: RegistrationID,
    val device_id: String,
    val active: Boolean,
    val date_created: String?,
    val type: String
)

