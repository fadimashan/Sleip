package se.mobileinteraction.sleip.entities


data class State(
    val defaultResponse: TokenResponse? = null,
    val exception: Exception? = null
)
