package se.mobileinteraction.sleip.entities

import kotlinx.serialization.Serializable


@Serializable
data class CreateHorseBody(
    var name: String,
    var date_of_birth: String?,
    var description: String?,
    var image: String?

)
