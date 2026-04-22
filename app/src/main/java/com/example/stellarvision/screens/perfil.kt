package com.example.stellarvision.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.stellarvision.common.*
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.viewmodel.LoginViewModel

@Composable
fun Perfil(controller: NavController)
{
    val loginViewModel: LoginViewModel = viewModel()

    BottomBar(controller, iconsNavBar) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = "Esteban Navas",
                    style = MaterialTheme.typography.headlineLarge
                )

                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5E5E5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(1f).height(60.dp).clickable { },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        AppText("Configuración")
                    }
                }
                ElevatedCard(
                    modifier = Modifier.weight(1f).height(60.dp).clickable { },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        AppText("Seguridad")
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(1f).height(60.dp).clickable { },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        AppText("Amigos")
                    }
                }

                ElevatedCard(
                    modifier = Modifier.weight(1f).height(60.dp).clickable { },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.List, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        AppText("Notificaciones")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        loginViewModel.logout()
                        controller.navigate(AppScreens.Login.name) {
                            popUpTo(AppScreens.Homepage.name) { inclusive = true }
                        }
                    },
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    Spacer(Modifier.width(8.dp))
                    AppText("Cerrar sesión")
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilPreview() {
    Perfil(controller = rememberNavController())
}