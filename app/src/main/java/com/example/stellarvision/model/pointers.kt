package com.example.stellarvision.model

import com.example.stellarvision.R
import org.osmdroid.util.GeoPoint

val pois = listOf(
    Poi(
        id = "1",
        title = "Orion Spot",
        point = GeoPoint(4.6289, -74.0640),
        imageRes = R.drawable.basicas,
        markerIconRes = R.drawable.location_icon
    ),
    Poi(
        id = "2",
        title = "Nebula Spot",
        point = GeoPoint(4.704723, -74.038195),
        imageRes = R.drawable.country,
        markerIconRes = R.drawable.location_icon
    ),
    Poi(
        id = "2",
        title = "Nebula Spot",
        point = GeoPoint(4.610360, -74.070497),
        imageRes = R.drawable.colpatria,
        markerIconRes = R.drawable.location_icon
    )

)

