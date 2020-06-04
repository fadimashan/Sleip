package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable

@Serializable
data class LoginData (
    val username: String,
    val password: String
)
