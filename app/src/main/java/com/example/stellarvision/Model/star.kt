package com.example.stellarvision.Model

import android.content.Context
import com.example.stellarvision.R

data class Star(
    val id: String,
    val properName: String? = "",
    val rightAscension: Double?,
    val declination: Double?,
    val visualMagnitude: Double?,
    val colorIndex: Double?,
    val constelation: String? = "",
    val distance: Double?,
    val luminosity: Double?
)

