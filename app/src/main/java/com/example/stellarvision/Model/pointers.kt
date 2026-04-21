package com.example.stellarvision.Model

import com.example.stellarvision.R
import org.osmdroid.util.GeoPoint

val pois = listOf(
    Poi(
        id = "1",
        title = "Orion Spot",
        point = GeoPoint(4.6289, -74.0640),
        imageRes = R.drawable.orion_post,
        markerIconRes = R.drawable.location_icon
    ),
    Poi(
        id = "2",
        title = "Nebula Spot",
        point = GeoPoint(4.6294, -74.0652),
        imageRes = R.drawable.orion_post,
        markerIconRes = R.drawable.location_icon
    )
)