package com.example.stellarvision.Screens.Maps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.stellarvision.ui.atoms.AppFab
import com.example.stellarvision.Model.pois

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointerDetail(
    navController: NavController,
    poiId: String
) {
    val poi = pois.first { it.id == poiId }

    Scaffold(
        topBar = { TopAppBar(title = { Text(poi.title) }) },
        floatingActionButton = {
            AppFab(
                contentDescription = "Ruta",
                onClick = {
                    //Mandar destino al mapa
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("poi_destination_id", poi.id)

                    //Volver al mapa
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->
        Image(
            painter = painterResource(poi.imageRes),
            contentDescription = poi.title,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}