package com.example.stellarvision.screens

import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.R
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppHeader
import com.example.stellarvision.common.AppSeparator
import com.example.stellarvision.common.AppText
import com.example.stellarvision.common.AppTextField
import com.example.stellarvision.common.SocialButtons
import com.example.stellarvision.viewmodel.LoginViewModel

@Composable
fun Login(controller: NavController, model : LoginViewModel = viewModel())
{
    val context = LocalContext.current
    val user by model.loginState.collectAsState()
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
            user.email,
            {model.updateEmail(it)},
            placeholder = "Correo Electrónico ",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                     contentDescription = "Email"
                )
            },
           supportingText = {Text(user.emailError, color = Color.Red)}
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            user.password,
            {model.updatePassword(it)},
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
            },
            supportingText = {Text(user.passwordError, color = Color.Red)}
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
                model.login(user.email,user.password,
                    onSuccess = {controller.navigate(AppScreens.Homepage.name)},
                    onError = {Toast.makeText(context, "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show()}
                )
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
            onTextClick = {controller.navigate("Register1")},
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

