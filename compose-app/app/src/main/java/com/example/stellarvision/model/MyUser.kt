package com.example.stellarvision.model

const val USERS = "users/"

data class MyUser(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profilePictureUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val locationUpdatedAt: Long = 0L
)