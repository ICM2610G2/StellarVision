package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stellarvision.ui.atoms.AppAvatar
import com.example.stellarvision.ui.atoms.AppText

@Composable
fun CommentPreview(
    userName: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier= modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        AppAvatar(size=34.dp)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(userName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            Text(text, fontSize = 13.sp)
        }
    }
}