package com.example.stellarvision.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Homepage(controller: NavController)
{

}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Homepage(controller = rememberNavController())
}