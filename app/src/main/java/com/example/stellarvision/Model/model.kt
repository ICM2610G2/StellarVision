package com.example.stellarvision.Model

import androidx.compose.ui.tooling.preview.Preview

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