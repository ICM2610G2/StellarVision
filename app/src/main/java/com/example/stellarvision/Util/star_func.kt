package com.example.stellarvision.Util

import android.content.Context
import com.example.stellarvision.Model.Star
import com.example.stellarvision.R
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

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
                distance = columns.getOrNull(9)?.toDoubleOrNull(),
                visualMagnitude = columns.getOrNull(13) ?.toDoubleOrNull(),
                colorIndex = columns.getOrNull(16) ?.toDoubleOrNull(),
                constelation = columns.getOrNull(29) ?: "",
                luminosity = columns.getOrNull(33) ?.toDoubleOrNull()
            )
            starObjects.add(star)
        }
    }
    return starObjects
}

fun visibleStars(stars: List<Star>, lat: Double, lon: Double): List<Star>{
    val currentTimeMillis = System.currentTimeMillis()
    val lst = calculateLST(currentTimeMillis, lon)
    val latRad = Math.toRadians(lat)

    return stars.filter{ star ->
        val ra = star.rightAscension ?: return@filter false
        val dec = star.declination ?: return@filter false
        val mag = star.visualMagnitude ?: 100.0

        if(star.properName!!.isEmpty() || mag > 2.0) return@filter false

        val raRad = Math.toRadians(ra * 15.0)
        val decRad = Math.toRadians(dec)
        val lstRad = Math.toRadians(lst * 15.0)

        val hourAngle = lstRad - raRad

        val sinAlt = sin(decRad) * sin(latRad) + cos(decRad) * cos(latRad) * cos(hourAngle)
        val altRad = asin(sinAlt)
        val altDegrees = Math.toDegrees(altRad)

        altDegrees > 0

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