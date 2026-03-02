package com.example.stellarvision.ui.atoms

import android.R
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.ui.theme.PurpleGrey80

@Composable
fun AppTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Primary,
    unselectedColor: Color = PurpleGrey80,
    indicatorColor: Color = Color.Black
) {
    Column(
        modifier = modifier.clickable { onClick()},
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text=text,
            color=if (selected) selectedColor else unselectedColor,
            fontWeight = FontWeight.Normal
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.width(22.dp).height(2.dp).background(
                color=if(selected) indicatorColor else Color.Transparent,
                shape = RoundedCornerShape(99.dp)
            )
        )
    }
}