package com.example.stellarvision.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.molecules.HomeTopTabs
import com.example.stellarvision.ui.templates.BottomBar
import com.example.stellarvision.ui.templates.PostCard


@Composable
fun Homepage(controller: NavController) {
    BottomBar(controller, iconsNavBar) { padding ->
        var selected by rememberSaveable { mutableIntStateOf(1) }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            HomeTopTabs(
                selectedIndex = selected,
                modifier = Modifier,
                onSelect = {selected=it}
            )

            PostCard(
                userName = "Helena",
                groupText = "Hace 5 minutos",
                body = "Hoy en la noche tome una foto de la constelacion Orion. Me encanta como se ven las constelaciones con esta aplicacion!",
                likes= 8,
                comments = 1,
                previewUser = "Daniel",
                previewText = "Se ve bastante bien, desde que lugar la tomaste?"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Homepage(controller = rememberNavController())
}