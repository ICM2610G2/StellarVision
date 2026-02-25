package com.example.stellarvision.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.Screens.Actividad
import com.example.stellarvision.Screens.Homepage
import com.example.stellarvision.Screens.Login
import com.example.stellarvision.Screens.Mapa
import com.example.stellarvision.Screens.Perfil
import com.example.stellarvision.Screens.Register1
import com.example.stellarvision.Screens.Register2
import com.example.stellarvision.Screens.Vista360
import com.example.stellarvision.Screens.Vistalocalizacion

enum class AppScreens {
   Login,
    Register1,
    Register2,
    Homepage,
    Mapa,
    Vista360,
    Vistalocalizacion,
    Actividad,
    Perfil
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController, AppScreens.Login.name) {
        composable(AppScreens.Login.name) {
            Login(navController)
        }
        composable(AppScreens.Register1.name) {
            Register1(navController)
        }
        composable(AppScreens.Register2.name) {
            Register2(navController)
        }
        composable(AppScreens.Homepage.name) {
            Homepage(navController)
        }
        composable(AppScreens.Mapa.name) {
            Mapa(navController)
        }
        composable(AppScreens.Vista360.name) {
            Vista360(navController)
        }
        composable(AppScreens.Vistalocalizacion.name) {
            Vistalocalizacion(navController)
        }
        composable(AppScreens.Actividad.name) {
            Actividad(navController)
        }
        composable(AppScreens.Perfil.name) {
            Perfil(navController)
        }
    }
}