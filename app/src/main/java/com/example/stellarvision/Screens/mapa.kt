package com.example.stellarvision.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.AppText
import com.example.stellarvision.ui.molecules.AppHeader
import org.w3c.dom.Text

@Composable
fun Mapa(controller: NavController)
{
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(
            "Stellar Vision",
            "Mapa",
            R.drawable.vectorsvlogo,
            "Mapa para ubicar constelaciones"
        )
        AppText(
            "Mapa API",
            MaterialTheme.typography.labelMedium,
            MaterialTheme.colorScheme.onBackground
        )

        //BottomBar(controller, iconsNavBar)
    }
}

@Preview(showBackground = true)
@Composable
fun MapaPreview() {
    Mapa(controller = rememberNavController())
}