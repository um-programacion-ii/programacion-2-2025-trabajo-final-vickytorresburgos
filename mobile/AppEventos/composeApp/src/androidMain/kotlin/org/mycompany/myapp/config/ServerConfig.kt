package org.mycompany.myapp.config

import org.mycompany.myapp.BuildConfig

actual object ServerConfig {
    actual val BASE_URL: String = BuildConfig.BACKEND_URL
}