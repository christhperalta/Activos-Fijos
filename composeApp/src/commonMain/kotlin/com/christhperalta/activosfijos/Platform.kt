package com.christhperalta.activosfijos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform