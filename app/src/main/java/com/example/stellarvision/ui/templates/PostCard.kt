package com.example.stellarvision.ui.templates

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.molecules.CommentPreview
import com.example.stellarvision.R
import com.example.stellarvision.ui.molecules.PostActionsRow
import com.example.stellarvision.ui.molecules.PostHeader
import com.example.stellarvision.ui.theme.PurpleGrey80

@Composable
fun PostCard(
    userName: String,
    groupText: String,
    body: String,
    likes: Int,
    comments: Int,
    previewUser: String,
    previewText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier=modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical=10.dp)
    ) {
        PostHeader(userName, groupText, onMore = {})

        Spacer(Modifier.height(8.dp))

        Text(
            body,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(10.dp))

        Image(
            painter = painterResource(id=R.drawable.orion_post),
            contentDescription = null,
            modifier= Modifier.fillMaxWidth().height(230.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(10.dp))

        PostActionsRow(likes,comments)

        Spacer(Modifier.height(14.dp))

        CommentPreview(userName=previewUser, text = previewText)
    }
}