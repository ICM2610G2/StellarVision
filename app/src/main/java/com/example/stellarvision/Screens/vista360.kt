package com.example.stellarvision.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.CameraButton
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.templates.BottomBar

@Composable
fun Vista360(controller: NavController)
{
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.imagen_360_temp),
            contentDescription = "Imagen temporal 360",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.align(Alignment.BottomCenter).padding(40.dp)
        ){
            CameraButton (onClick = {} )
        }
    }





}

@Preview(showBackground = true)
@Composable
fun Vista360Preview() {
    Vista360(controller = rememberNavController())
}