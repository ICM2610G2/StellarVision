package com.example.stellarvision.Model

import android.content.Context
import com.example.stellarvision.R

data class Star(
    val id: String,
    val properName: String?,
    val rightAscension: Double?,
    val declination: Double?,
    val visualMagnitude: Double?,
    val colorIndex: Double?
)

fun getStarsFromHYG(context: Context): List<Star> {
    val starObjects = mutableListOf<Star>()
    val inputStream = context.resources.openRawResource(R.raw.hyg_v42)

    inputStream.bufferedReader().useLines { lines ->
        lines.drop(1).forEach { line ->
            val columns = line.split(",")

            val star = Star(
                id = columns.getOrNull(0) ?: "",
                properName = columns.getOrNull(6) ?: "",
                rightAscension = columns.getOrNull(7) ?.toDoubleOrNull(),
                declination = columns.getOrNull(8) ?.toDoubleOrNull(),
                visualMagnitude = columns.getOrNull(13) ?.toDoubleOrNull(),
                colorIndex = columns.getOrNull(16) ?.toDoubleOrNull()
            )
            starObjects.add(star)
        }
    }
    return starObjects
}