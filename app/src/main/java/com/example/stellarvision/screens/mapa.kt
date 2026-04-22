package com.example.stellarvision.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.common.AppText
import com.example.stellarvision.common.AppTextField
import com.example.stellarvision.common.BottomBar
import com.example.stellarvision.common.iconsNavBar

@Composable
fun Mapa(controller: NavController)
{
    var buscar by remember{mutableStateOf("")}
    BottomBar(controller, iconsNavBar) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            AppTextField(
                buscar,
                { buscar = it },
                placeholder = "Universidad Javeriana",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            )
            AppText(
                text= "9 resultados encontrados",
                modifier = Modifier.padding(20.dp)
            )
            Image(
                painter = painterResource(R.drawable.mapa),
                contentDescription = "Mapa",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapaPreview() {
    Mapa(controller = rememberNavController())
}