package com.example.stellarvision.model

import androidx.annotation.DrawableRes
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

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = ""
)

data class RegisterState(
    val email : String = "",
    val password : String = "",
    val username : String = "",
    val confirmPassword : String = "",
    val emailError : String = "",
    val passwordError : String = "",
    val usernameError : String = "",
    val confirmPasswordError : String = ""
)

data class mapState(
    val userPoint: GeoPoint = GeoPoint(4.60971, -74.08175),
    val startAddress: String = "",
    val endAddress: String = "",
    val startPoint: GeoPoint? = null,
    val endPoint: GeoPoint? = null,
    val destinationPoint: GeoPoint? = null,
    val routePoints: List<GeoPoint> = emptyList(),
    val lightLevel: Float = 99999f,
    val isDarkMap: Boolean = false,
    val selectedDistance: String = ""
)

data class Poi(
    val id: String,
    val title: String,
    val point: GeoPoint,
    @DrawableRes val imageRes: Int,
    @DrawableRes val markerIconRes: Int
)