package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.atoms.AppCounterIcon

@Composable
fun PostActionsRow(
    likes: Int,
    comments: Int,
    modifier: Modifier= Modifier
) {
    Row(modifier=modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        AppCounterIcon(Icons.Outlined.FavoriteBorder, count = likes)
        Spacer(Modifier.width(24.dp))
        AppCounterIcon(Icons.Outlined.MailOutline ,count = comments)
    }
}