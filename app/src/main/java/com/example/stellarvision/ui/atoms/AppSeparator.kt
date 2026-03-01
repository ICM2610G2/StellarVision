package com.example.stellarvision.ui.atoms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.theme.Gray

@Composable
fun AppSeparator(
    modifier : Modifier = Modifier,
    text : String = ""
){
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f)
                .height(1.dp),
            color = Gray
        )
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(
            modifier = Modifier.weight(1f)
                .height(1.dp),
            color = Gray
        )
    }
}