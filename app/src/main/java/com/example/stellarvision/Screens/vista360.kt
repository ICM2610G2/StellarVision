package com.example.stellarvision.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.Model.Star
import com.example.stellarvision.Model.getStarsFromHYG
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.CameraButton
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.templates.BottomBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

@Composable
fun Vista360(controller: NavController)
{

    var stars by remember { mutableStateOf<List<Star>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            stars = getStarsFromHYG(context)
        }
    }

    /* Vista 360
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
    }*/

    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Column(modifier = Modifier.weight(1F), verticalArrangement = Arrangement.Center) {
            Text("La altitud es: 0", fontSize = 21.sp)
            Text("La longitud es: 0", fontSize = 21.sp)
        }

        LazyColumn(modifier = Modifier.weight(1F)) {
            items(stars){ star ->
                Text(star.toString())
            }
        }
    }



}

/*@Preview(showBackground = true)
@Composable
fun Vista360Preview() {
    Vista360(controller = rememberNavController())
}*/