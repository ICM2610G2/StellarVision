package com.example.stellarvision.ui.atoms

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.stellarvision.R
import com.example.stellarvision.ui.theme.Primary

@Composable
fun AppButton(
    text : String,
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    color : Color = Primary,
    enabled : Boolean = true,
    icon: Int? = null,
    contentDescription : String = "",
    textColor: Color = Color.White

){
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = MaterialTheme.colorScheme.outline
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painterResource(icon),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(Modifier.padding(horizontal = 8.dp))
            AppText(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor
            )
        }
    }

}