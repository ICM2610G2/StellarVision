package com.example.stellarvision.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Mapa(controller: NavController)
{

}

@Preview(showBackground = true)
@Composable
fun MapaPreview() {
    Mapa(controller = rememberNavController())
}