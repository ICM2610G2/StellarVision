package com.example.stellarvision.model

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
    val username : String ="",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val usernameError : String = ""
)