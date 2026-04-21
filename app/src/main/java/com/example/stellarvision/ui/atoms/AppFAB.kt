package com.example.stellarvision.ui.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.stellarvision.ui.theme.Primary

@Composable
fun AppFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Primary,
    contentDescription: String = "Accion",
    icon: ImageVector= Icons.Outlined.Route
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}