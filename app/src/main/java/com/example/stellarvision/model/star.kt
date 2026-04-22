package com.example.stellarvision.model

data class Star(
    val id: String,
    val properName: String? = "",
    val rightAscension: Double,
    val declination: Double,
    val visualMagnitude: Double,
    val colorIndex: Double?,
    val constelation: String? = "",
    val distance: Double?,
    val luminosity: Double?
)

