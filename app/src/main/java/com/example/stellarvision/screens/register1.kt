package com.example.stellarvision.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppHeader
import com.example.stellarvision.common.AppSeparator
import com.example.stellarvision.common.AppText
import com.example.stellarvision.common.AppTextField
import com.example.stellarvision.common.SocialButtons
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.viewmodel.RegisterViewModel

@Composable
fun Register1(controller: NavController, model: RegisterViewModel = viewModel())
{
    val user by model.registerState.collectAsState()
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
            user.email,
            {model.updateEmail(it)},
            placeholder = "correo@ejemplo.com",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Correo Electrónico"
                )
            },
            supportingText = {Text(user.emailError, color = Color.Red)}
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )
        AppButton(
            "Continuar",
            onClick = {
                if(model.validateEmail(user.email)){
                    controller.navigate(AppScreens.Register2.name)
                }
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
                controller.navigate(AppScreens.Login.name)
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