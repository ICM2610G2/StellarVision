package com.example.stellarvision.ui.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.molecules.NotificationHeader

@Composable
fun NotificationCard(
    userName: String,
    groupText: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier=modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical=10.dp)
    ) {
        NotificationHeader(userName, groupText, onMore = {})

        Spacer(Modifier.height(8.dp))

        Text(
            body,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(10.dp))

    }
}