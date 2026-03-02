package com.example.stellarvision.ui.atoms

import com.example.stellarvision.Model.NavItem
import com.example.stellarvision.Navigation.AppScreens
import com.example.stellarvision.R
val iconsNavBar = listOf(
    NavItem("home", R.drawable.home_icon, "Home", AppScreens.Homepage.name),
    NavItem("map", R.drawable.map_icon, "Map", AppScreens.Mapa.name),
    NavItem("camera", R.drawable.camera_icon, "Camera", AppScreens.Vista360.name),
    NavItem("bell", R.drawable.bell_icon, "Notifications", AppScreens.Actividad.name),
    NavItem("user", R.drawable.person_icon, "Profile", AppScreens.Perfil.name)
)