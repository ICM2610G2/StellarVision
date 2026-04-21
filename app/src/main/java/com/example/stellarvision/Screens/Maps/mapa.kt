package com.example.stellarvision.Screens.Maps


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun Mapa(controller: NavController) {
        MapaPermiso(controller=controller)
}


@Preview(showBackground = true)
@Composable
fun MapaPreview() {
    Mapa(controller = rememberNavController())
}