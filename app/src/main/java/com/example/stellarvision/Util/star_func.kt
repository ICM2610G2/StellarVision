package com.example.stellarvision.Util

import android.content.Context
import com.example.stellarvision.Model.Star
import com.example.stellarvision.R
import kotlin.collections.filter
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

fun getStarsFromHYG(context: Context): List<Star> {
    val starObjects = mutableListOf<Star>()
    val inputStream = context.resources.openRawResource(R.raw.hyg_v42)

    inputStream.bufferedReader().useLines { lines ->
        lines.drop(1).forEach { line ->
            val columns = line.split(",")

            val id = columns.getOrNull(0) ?: ""
            val properName = columns.getOrNull(6) ?: ""
            val rightAscension = columns.getOrNull(7) ?.toDoubleOrNull()
            val declination = columns.getOrNull(8) ?.toDoubleOrNull()
            val distance = columns.getOrNull(9)?.toDoubleOrNull() ?: -1.0
            val visualMagnitude = columns.getOrNull(13) ?.toDoubleOrNull()
            val colorIndex = columns.getOrNull(16) ?.toDoubleOrNull()
            val constelation = columns.getOrNull(29) ?: ""
            val luminosity = columns.getOrNull(33) ?.toDoubleOrNull()

            if(rightAscension != null && declination != null && visualMagnitude != null){
                starObjects.add(Star(id, properName, rightAscension, declination, visualMagnitude, colorIndex, constelation, distance, luminosity))
            }
        }
    }
    return starObjects
}

fun visibleStars(
    stars: List<Star>,
    lat: Double,
    lon: Double,
    alt: Double,
    azimuth: Float,
    pitch: Float,
    utcTime: Long
): List<Star>{
    val lst = calculateLST(utcTime, lon)

    return stars.filter{ star ->

        if(star.properName.isNullOrEmpty()) return@filter false
        val ha = (lst - star.rightAscension) * 15.0

        val latRad = Math.toRadians(lat)
        val decRad = Math.toRadians(star.declination)
        val haRad = Math.toRadians(ha)

        val sinAlt = sin(decRad) * sin(latRad) + cos(decRad) * cos(latRad) * cos(haRad)
        val starAlt = Math.toDegrees(asin(sinAlt))

        if(starAlt < -1.0) return@filter false

        val cozAz = (sin(decRad) - sin(latRad) * sinAlt) / (cos(latRad) * cos(asin(sinAlt)))
        var starAz = Math.toDegrees(acos(cozAz))
        if(sin(haRad) > 0) starAz = 360.0 - starAz

        val deltaAz = ((starAz - azimuth + 540) % 360) - 180
        val deltaPitch = starAlt - pitch

        val isOnScreen = abs(deltaAz) < (60.0 / 2) && abs(deltaPitch) < (40.0 / 2)

        val isBrightEnough = star.visualMagnitude <= 5.0

        isOnScreen && isBrightEnough

    }
}

fun calculateLST(millis: Long, longitude: Double): Double {

    val jd = (millis / 86400000.0) + 2440587.5
    val d = jd - 241545.0

    var gmst = 18.677374558 + 24.06570982441908 * d
    gmst %= 24.0
    if (gmst < 0) gmst += 24.0

    var lst = gmst + (longitude / 15.0)
    lst %= 24.0
    if (lst < 0) lst += 24.0

    return lst
}