package com.example.stellarvision.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.common.CameraButton

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

        Icon(
            painter = painterResource(R.drawable.cardinalstar),
            contentDescription = "Estrella cardinal",
            modifier = Modifier.align(Alignment.TopEnd).size(60.dp).padding(end = 20.dp)
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