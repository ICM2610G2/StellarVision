package com.example.stellarvision.ui.atoms

import android.R.attr.content
import android.media.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppCounterIcon(
    icon: ImageVector,
    count: Int,
    modifier: Modifier= Modifier,
    tint: Color=Color.Black
) {
    Row(modifier=modifier) {
        Icon(imageVector = icon, contentDescription=null, tint=tint)
        Spacer(Modifier.width(8.dp))
        Text(text = count.toString(), fontSize = 14.sp,color=tint)
    }
}