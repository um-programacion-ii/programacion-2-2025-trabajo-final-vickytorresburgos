package org.mycompany.myapp.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.mycompany.myapp.config.ServerConfig

object NetworkClient {
    private val BASE_URL = ServerConfig.BASE_URL

    val client = HttpClient{
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            requestTimeoutMillis = 15000
            requestTimeoutMillis = 15000
        }
        defaultRequest {
            url(BASE_URL)
        }
    }
}