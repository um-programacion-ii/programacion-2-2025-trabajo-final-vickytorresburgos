package org.mycompany.myapp.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterData(
    val login: String,
    val email: String,
    val password: String,
    val langKey: String = "es"
)