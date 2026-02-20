package io.sorceweaver.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform