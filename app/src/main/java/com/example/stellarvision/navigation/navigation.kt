package com.example.stellarvision.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.screens.Mapa
import com.example.stellarvision.screens.PointerDetail
import com.example.stellarvision.screens.CrearPublicacion
import com.example.stellarvision.screens.Homepage
import com.example.stellarvision.screens.Login
import com.example.stellarvision.screens.Mensajeria
import com.example.stellarvision.screens.Perfil
import com.example.stellarvision.screens.Publicacion
import com.example.stellarvision.screens.Register1
import com.example.stellarvision.screens.Register2
import com.example.stellarvision.screens.Vistalocalizacion
import com.example.stellarvision.viewmodel.LoginViewModel
import com.example.stellarvision.viewmodel.RegisterViewModel

enum class AppScreens {
   Login,
    Register1,
    Register2,
    Homepage,
    Mapa,
    Vista360,
    Publicacion,
    Vistalocalizacion,
    Mensajeria,
    Perfil,

    Crearpublicacion
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val registerViewModel: RegisterViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    LaunchedEffect(Unit) {
        if (loginViewModel.isUserLoggedIn()) {
            navController.navigate(AppScreens.Homepage.name) {
                popUpTo(AppScreens.Login.name) { inclusive = true }
            }
        }
    }
    NavHost(navController, AppScreens.Login.name) {
        composable(AppScreens.Login.name) {
            Login(navController)
        }
        composable(AppScreens.Register1.name) {
            Register1(navController, registerViewModel)
        }
        composable(AppScreens.Register2.name) {
            Register2(navController, registerViewModel)
        }
        composable(AppScreens.Homepage.name) {
            Homepage(navController)
        }
        composable(AppScreens.Mapa.name) {
            Mapa(navController)
        }
        composable(AppScreens.Publicacion.name) {
            Publicacion(navController)
        }
        composable(AppScreens.Vistalocalizacion.name) {
            Vistalocalizacion(navController)
        }
        composable(AppScreens.Mensajeria.name) {
            Mensajeria(navController)
        }
        composable(AppScreens.Perfil.name) {
            Perfil(navController)
        }
        composable(AppScreens.Crearpublicacion.name) {
            CrearPublicacion(navController)
        }
        composable("poi/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            PointerDetail(navController, poiId = id)
        }
    }
}