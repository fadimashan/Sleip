package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable


@Serializable
data class Device (
    val registration_id: RegistrationID,
    val type: Type
)

