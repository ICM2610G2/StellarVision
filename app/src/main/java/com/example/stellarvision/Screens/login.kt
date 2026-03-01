package com.example.stellarvision.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.AppButton
import com.example.stellarvision.ui.atoms.AppSeparator
import com.example.stellarvision.ui.atoms.AppText
import com.example.stellarvision.ui.atoms.AppTextField
import com.example.stellarvision.ui.molecules.AppHeader
import com.example.stellarvision.ui.molecules.SocialButtons
import com.example.stellarvision.ui.theme.InterFont
import com.example.stellarvision.ui.theme.OnSurfaceVariant
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.ui.theme.Secondary

@Composable
fun Login(controller: NavController)
{
    var username by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}
    var showPassword by remember{mutableStateOf(false)}
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(
            "Stellar Vision",
            "Inicia Sesión",
            R.drawable.vectorsvlogo,
            "Ingresa tus credenciales para continuar"
        )
       AppTextField(
            username,
            {username = it},
            placeholder = "Nombre de usuario",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                     contentDescription = "Usuario"
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            password,
            {password = it},
            placeholder = "Contraseña",
            isPassword = !showPassword,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña"
                )
            },
            trailingIcon = {
                IconButton(onClick = {showPassword = !showPassword}){
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                        ),
                        contentDescription = "Visibilidad"
                    )
                }
            }
        )
        AppText(
            "¿Olvidaste tu Contraseña?",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable(onClick = {})
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        AppButton(
            "Iniciar Sesión",
            onClick = {
                controller.navigate("homepage")
            },
            modifier = Modifier.padding(horizontal = 16.dp)
            )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        AppSeparator(
            text = "o"
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        SocialButtons(
            onGoogleClick = {},
            onAppleClick = {},
            "¿No tienes una cuenta? Registrate",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(controller = rememberNavController())
}

