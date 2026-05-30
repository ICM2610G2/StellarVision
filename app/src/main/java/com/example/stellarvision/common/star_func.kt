package com.example.stellarvision.common

import android.R.attr.textSize
import android.content.Context
import android.graphics.Canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.style.Style
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import com.example.stellarvision.model.Star
import com.example.stellarvision.R
import kotlin.collections.filter
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import android.graphics.Color as AndroidColor

data class ScreenStar(
    val star: Star,
    val xPercent: Float,
    val yPercent: Float
)
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
): List<ScreenStar>{
    val lst = calculateLST(utcTime, lon)
    val fovX = 60.0
    val fovY = 40.0

    return stars.mapNotNull{ star ->

        if(star.properName.isNullOrEmpty()) return@mapNotNull null

        val ha = (lst - star.rightAscension) * 15.0
        val latRad = Math.toRadians(lat)
        val decRad = Math.toRadians(star.declination)
        val haRad = Math.toRadians(ha)

        val sinAlt = sin(decRad) * sin(latRad) + cos(decRad) * cos(latRad) * cos(haRad)
        val starAlt = Math.toDegrees(asin(sinAlt))

        if(starAlt < -1.0) return@mapNotNull null

        val cozAz = (sin(decRad) - sin(latRad) * sinAlt) / (cos(latRad) * cos(asin(sinAlt)))
        var starAz = Math.toDegrees(acos(cozAz))
        if(sin(haRad) > 0) starAz = 360.0 - starAz

        val deltaAz = ((starAz - azimuth + 540) % 360) - 180
        val deltaPitch = starAlt - pitch

        val isOnScreen = abs(deltaAz) < (fovX / 2) && abs(deltaPitch) < (fovY / 2)

        val isBrightEnough = star.visualMagnitude <= 3.0

        if(isOnScreen && isBrightEnough){
            val xPercent = (deltaAz / fovX).toFloat()
            val yPercent = (deltaPitch / fovY).toFloat()
            ScreenStar(star, xPercent, yPercent)
        }else{
            null
        }

    }
}

@Composable
fun StarOverlay(
    visibleStars: List<ScreenStar>,
    modifier: Modifier = Modifier
){
    Canvas(modifier = modifier){
        val canvasWidth = size.width
        val canvasHeight = size.height

        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        visibleStars.forEach { screenStar ->
            val x = centerX + (screenStar.xPercent * canvasWidth)
            val y = centerY - (screenStar.yPercent * canvasHeight)

            val radius = ((6.0 - screenStar.star.visualMagnitude) * 4).toFloat().coerceIn(3f, 12f)

            drawCircle(
                color = Color.White,
                radius = radius,
                center = androidx.compose.ui.geometry.Offset(x,y)
            )

            drawContext.canvas.nativeCanvas.apply {
                val textPaint = Paint().apply {
                    color = AndroidColor.WHITE
                    textSize = 40f
                    isAntiAlias = true
                    style = Paint.Style.FILL
                    setShadowLayer(4f, 0f, 0f, AndroidColor.BLACK)
                }

                val infoText = screenStar.star.properName
                val constelationText = if(!screenStar.star.constelation.isNullOrEmpty()){
                    " (${screenStar.star.constelation})"
                }else ""

                drawText(
                    "$infoText$constelationText",
                    x + radius + 8f,
                    y + 10f,
                    textPaint
                )
            }
        }
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