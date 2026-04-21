package com.example.stellarvision.Model

import androidx.annotation.DrawableRes
import androidx.compose.ui.tooling.preview.Preview
import org.osmdroid.util.GeoPoint

data class NavItem(
    val id: String,
    val iconRes: Int,
    val contentDescription: String,
    val route: String
)

data class PostUi (
    val userName : String,
    val groupText: String,
    val body: String,
    val likes: Int,
    val comments: Int,
    val previewUser: String,
    val previewText: String
)

data class mapState(
    val userPoint: GeoPoint = GeoPoint(4.6287979, -74.0645618), //Bogota
    val startAddress: String = "",
    val endAddress: String = "",
    val startPoint: GeoPoint? = null,
    val endPoint: GeoPoint? = null,
    val routePoints: List<GeoPoint> = emptyList(),
    val destinationPoint: GeoPoint? = null,
)

data class Poi(
    val id: String,
    val title: String,
    val point: GeoPoint,
    @DrawableRes val imageRes: Int,
    @DrawableRes val markerIconRes: Int
)