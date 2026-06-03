package com.example.stellarvision.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Vistalocalizacion(controller: NavController)
{

}

@Preview(showBackground = true)
@Composable
fun VistalocalizacionPreview() {
    Vistalocalizacion(controller = rememberNavController())
}