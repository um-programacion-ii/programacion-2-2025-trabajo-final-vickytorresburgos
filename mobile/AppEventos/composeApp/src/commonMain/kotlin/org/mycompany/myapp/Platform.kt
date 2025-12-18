package org.mycompany.myapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform