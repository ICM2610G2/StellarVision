package com.example.stellarvision.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.AppButton
import com.example.stellarvision.ui.atoms.AppSeparator
import com.example.stellarvision.ui.atoms.AppText
import com.example.stellarvision.ui.atoms.AppTextField
import com.example.stellarvision.ui.molecules.AppHeader
import com.example.stellarvision.ui.molecules.SocialButtons

@Composable
fun Register1(controller: NavController)
{
    var mail by remember{mutableStateOf("")}
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(
            "Stellar Vision",
            "Crea una cuenta",
            R.drawable.vectorsvlogo,
            "Ingresa tu correo electrónico para registrarte en esta aplicación"
        )
        AppTextField(
            mail,
            {mail = it},
            placeholder = "correo@ejemplo.com",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Correo Electrónico"
                )
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )
        AppButton(
            "Continuar",
            onClick = {
                controller.navigate("Register2")
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        AppText(
            "¿Ya tienes una cuenta? Inicia Sesión",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable(
                onClick = {
                controller.navigate("Login")
            })
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
            onTextClick = {},
            "Al hacer clic en continuar, aceptas nuestros Términos de Servicio y Política de Privacidad",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Register1Preview() {
    Register1(controller = rememberNavController())
}