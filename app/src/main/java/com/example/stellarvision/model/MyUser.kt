package com.example.stellarvision.model

const val USERS = "users/"

data class MyUser(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profilePictureUrl: String = ""
)