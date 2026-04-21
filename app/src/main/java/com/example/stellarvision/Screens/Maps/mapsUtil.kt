package com.example.stellarvision.Screens.Maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.util.GeoPoint

fun findLocation(geocoder: Geocoder, address: String): GeoPoint? {
    return try {
        val addresses = geocoder.getFromLocationName(address, 1)
        if (!addresses.isNullOrEmpty()) {
            val a = addresses[0]
            GeoPoint(a.latitude, a.longitude)
        } else null
    } catch (_: Exception) {
        null
    }
}

fun distanceMeters(a: GeoPoint, b: GeoPoint): Double {
    val results = FloatArray(1)
    Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
    return results[0].toDouble()
}

fun createLocationRequest(): LocationRequest =
    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setMinUpdateIntervalMillis(2500)
        .build()

fun createLocationCallback(onLocation: (LocationResult) -> Unit): LocationCallback =
    object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocation(locationResult)
        }
    }

fun buildRoutePoints(
    roadManager: OSRMRoadManager,
    start: GeoPoint,
    end: GeoPoint
): List<GeoPoint> {
    val road = roadManager.getRoad(arrayListOf(start, end))
    return road.mRouteHigh ?: emptyList()
}

fun scaledDrawable(context: Context, resId: Int, sizePx: Int): Drawable? {
    val d = ContextCompat.getDrawable(context, resId) ?: return null

    val bmp = when (d) {
        is BitmapDrawable -> d.bitmap
        else -> {
            val bitmap = Bitmap.createBitmap(
                d.intrinsicWidth.coerceAtLeast(1),
                d.intrinsicHeight.coerceAtLeast(1),
                Bitmap.Config.ARGB_8888
            )
            val canvas = android.graphics.Canvas(bitmap)
            d.setBounds(0, 0, canvas.width, canvas.height)
            d.draw(canvas)
            bitmap
        }
    }

    val scaled = Bitmap.createScaledBitmap(bmp, sizePx, sizePx, true)
    return BitmapDrawable(context.resources, scaled)
}