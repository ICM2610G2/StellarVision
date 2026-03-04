package com.example.stellarvision.ui.atoms


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.ui.theme.PurpleGrey80

@Composable
fun AppRowButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color = Primary,
    unselectedColor: Color = PurpleGrey80,
) {

        Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected) selectedColor else unselectedColor,
                disabledContainerColor = MaterialTheme.colorScheme.outline
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) unselectedColor else selectedColor
                )
            }
        }



}