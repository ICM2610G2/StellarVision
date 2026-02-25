package com.example.stellarvision.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.Greeting
import com.example.stellarvision.ui.theme.StellarVisionTheme

@Composable
fun Actividad(controller: NavController)
{

}

@Preview(showBackground = true)
@Composable
fun ActividadPreview() {
    Actividad(controller = rememberNavController())
}