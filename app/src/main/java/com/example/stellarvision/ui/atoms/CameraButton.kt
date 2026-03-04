package com.example.stellarvision.ui.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.stellarvision.R

@Composable
fun CameraButton(onClick: () -> Unit){
    LargeFloatingActionButton(
        onClick = onClick,
        containerColor = Color.White.copy(alpha = 0.7f),
        contentColor = Color.Black,
        shape = CircleShape
    ){
        Image(
            painter = painterResource(R.drawable.camarabutton),
            contentDescription = "Tomar foto",
            modifier = Modifier.size(35.dp)
        )
    }
}