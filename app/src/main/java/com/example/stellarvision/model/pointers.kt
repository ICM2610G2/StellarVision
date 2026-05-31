package com.example.stellarvision.model

import com.example.stellarvision.R
import com.google.android.gms.maps.model.LatLng

val pois = listOf(
    Poi(
        id = "1",
        title = "Orion Spot",
        point = LatLng(4.6289, -74.0640),
        imageRes = R.drawable.basicas,
        markerIconRes = R.drawable.location_icon
    ),
    Poi(
        id = "2",
        title = "Nebula Spot",
        point = LatLng(4.704723, -74.038195),
        imageRes = R.drawable.country,
        markerIconRes = R.drawable.location_icon
    ),
    Poi(
        id = "3",
        title = "Colpatria Spot",
        point = LatLng(4.610360, -74.070497),
        imageRes = R.drawable.colpatria,
        markerIconRes = R.drawable.location_icon
    )
)