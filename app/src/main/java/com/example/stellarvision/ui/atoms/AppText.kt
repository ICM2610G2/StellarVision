package com.example.stellarvision.ui.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AppText(
    text : String,
    style : TextStyle = MaterialTheme.typography.bodyLarge,
    color : Color = MaterialTheme.colorScheme.onSurface,
    modifier : Modifier = Modifier,
    ){
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}
