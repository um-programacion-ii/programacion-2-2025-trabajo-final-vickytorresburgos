package org.mycompany.myapp.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val username: String,
    val password: String,
    val rememberMe: Boolean = true
)

@Serializable
data class TokenResponse(
    val id_token: String
)