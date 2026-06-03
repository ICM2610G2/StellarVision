package com.example.stellarvision.model

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng

data class NavItem(
    val id: String,
    val iconRes: Int,
    val contentDescription: String,
    val route: String
)

data class Post(
    val postId: String = "",
    val userId: String = "",
    val userName: String = "",
    val title: String = "",
    val description: String = "",
    val constellation: String = "",
    val locationPrivacy: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0,
    val likes: Int = 0,
    val comments: Int = 0
)

data class PostUi(
    val userName: String,
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
    val phoneNumber : String = "",
    val profilePictureUrl : String = "",
    val emailError : String = "",
    val passwordError : String = "",
    val usernameError : String = "",
    val confirmPasswordError : String = "",
    val phoneNumberError : String = ""
)

data class mapState(
    val userPoint: LatLng = LatLng(4.60971, -74.08175),
    val startAddress: String = "",
    val endAddress: String = "",
    val startPoint: LatLng? = null,
    val endPoint: LatLng? = null,
    val destinationPoint: LatLng? = null,
    val routePoints: List<LatLng> = emptyList(),
    val lightLevel: Float = 99999f,
    val isDarkMap: Boolean = false,
    val selectedDistance: String = "",
    val pois: List<Poi> = emptyList(),
    val isLoadingPois: Boolean = false,
    val poisError: String? = null
)

data class Poi(
    val id: String = "",
    val title: String = "",
    val point: LatLng = LatLng(0.0, 0.0),
    val imagePath: String = "",
    val imageUrl: String = "",
    @DrawableRes val imageRes: Int? = null,
    @DrawableRes val markerIconRes: Int? = null
)