package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse (
    val token: String
)
