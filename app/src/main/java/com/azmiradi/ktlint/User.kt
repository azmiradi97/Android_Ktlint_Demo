package com.azmiradi.ktlint

import androidx.compose.runtime.Composable

class User {
    // variable naming
    @Suppress("ktlint:standard:property-naming")
    val Name = "Azmi"

    // spacing
    fun getLocation() {
    }

    // fun naming
    @Composable
    fun SetAzmiName() {
    }

    @Composable
    fun aod(): Float {
        return 0f
    }
}