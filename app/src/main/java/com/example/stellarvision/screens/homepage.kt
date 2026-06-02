package com.example.stellarvision.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.common.BottomBar
import com.example.stellarvision.common.HomeTopTabs
import com.example.stellarvision.common.PostCard
import com.example.stellarvision.common.iconsNavBar
import com.example.stellarvision.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Homepage(
    controller: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {

    val listaPublicaciones by homeViewModel.publicaciones.collectAsState()


    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonimo"

    BottomBar(controller, iconsNavBar) { padding ->
        var selected by rememberSaveable { mutableIntStateOf(1) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HomeTopTabs(
                    selectedIndex = selected,
                    modifier = Modifier,
                    onSelect = { selected = it }
                )


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 85.dp)
                ) {
                    items(listaPublicaciones) { publicacion ->

                        val yaTieneLike = publicacion.likedBy.containsKey(currentUserId)

                        PostCard(
                            userName = publicacion.userName,
                            groupText = publicacion.groupText,
                            body = publicacion.body,
                            imageUrl = publicacion.imageUrl,
                            likes = publicacion.likes,
                            comments = publicacion.comments,
                            isLiked = yaTieneLike,
                            onLikeClick = {
                                homeViewModel.conmutarLike(publicacion.id, currentUserId)
                            },
                            onCommentClick = {
                                controller.navigate("comentarios/${publicacion.id}")
                            }
                        )
                    }
                }
            }

            /*Button(
                onClick = {
                    controller.navigate("publicacion")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Nueva Publicación", modifier = Modifier.padding(vertical = 4.dp))
            }*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Homepage(controller = rememberNavController())
}