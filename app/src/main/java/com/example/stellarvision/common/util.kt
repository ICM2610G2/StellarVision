package com.example.stellarvision.common

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

fun validEmailAddress(email:String):Boolean{
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    return email.matches(regex.toRegex())
}

fun validPassword(password: String): Boolean {
    val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[\\S]{8,255}$"
    return password.matches(regex.toRegex())
}

fun validUsername(username: String): Boolean {
    val regex = "^[A-Za-z0-9._-]{3,20}$"
    return username.matches(regex.toRegex())
}

fun createLocationRequest(
    interval: Long = 10000L,
    minInterval: Long = 5000L,
    accuracy: Int = Priority.PRIORITY_HIGH_ACCURACY
): LocationRequest {
    return LocationRequest.Builder(accuracy, interval)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(minInterval)
        .build()
}

fun createLocationCallback(
    onLocationChange: (LocationResult) -> Unit
): LocationCallback {
    return object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            onLocationChange(result)
        }
    }
}