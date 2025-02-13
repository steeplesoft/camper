package com.edorex.mobile.composeForm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
