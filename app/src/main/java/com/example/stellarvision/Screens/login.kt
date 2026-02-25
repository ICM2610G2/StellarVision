package com.example.stellarvision.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.ui.theme.InterFont

@Composable
fun Login(controller: NavController)
{
    Text(
        "Stellar Vision ",
        fontFamily = InterFont
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(controller = rememberNavController())
}