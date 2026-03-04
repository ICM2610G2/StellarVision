package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stellarvision.ui.atoms.AppAvatar
import com.example.stellarvision.ui.theme.PurpleGrey80

@Composable
fun NotificationHeader(
    userName: String,
    groupText: String,
    onMore: () -> Unit,
    modifier: Modifier= Modifier
) {
    Row(
        modifier=modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAvatar(size = 42.dp)
        Spacer(Modifier.width(12.dp))

        Column(modifier= Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(userName, fontWeight= FontWeight.SemiBold, fontSize=12.sp)
                Spacer(Modifier.width(6.dp))
                Text(groupText, color= PurpleGrey80, fontSize = 12.sp)
            }
        }
        IconButton(onClick = onMore) {
            Icon(Icons.Default.Favorite, contentDescription = "like")
        }
    }
}