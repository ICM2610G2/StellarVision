package com.example.stellarvision.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.templates.BottomBar


@Composable
fun Homepage(controller: NavController) {
    BottomBar(controller, iconsNavBar)
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Homepage(controller = rememberNavController())
}