package com.example.stellarvision.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.stellarvision.R
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppHeader
import com.example.stellarvision.common.AppTextField
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.viewmodel.RegisterViewModel

@Composable
fun Register2(controller: NavController, model : RegisterViewModel = viewModel())
{
    val user by model.registerState.collectAsState()
    var showPassword by remember{mutableStateOf(false)}

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(
            "Stellar Vision",
            "Completa tu Registro",
            R.drawable.vectorsvlogo,
            "Crea tu nombre de usuario y contraseña"
        )
        AppTextField(
            user.username,
            {model.updateUsername(it)},
            placeholder = "Nombre de usuario",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuario"
                )
            },
            supportingText = {Text(user.usernameError, color = Color.Red)}
        )
        Spacer(modifier = Modifier.height(4.dp))
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
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                        ),
                        contentDescription = "Visibilidad Contraseña"
                    )
                }
            },
            supportingText = {Text(user.passwordError, color = Color.Red)}
        )
        Spacer(modifier = Modifier.height(4.dp))
        AppTextField(
            user.confirmPassword,
            {model.updateConfirmPassword(it)},
            placeholder = "Confirmar contraseña",
            isPassword = !showPassword,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirmar Contraseña"
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                        ),
                        contentDescription = "Visibilidad Confirmar Contraseña"
                    )
                }
            },
            supportingText = {Text(user.confirmPasswordError, color = Color.Red)}
        )
        /*AppText(text="Preguntas de Seguridad", modifier = Modifier.align(alignment = Alignment.Start).padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(4.dp))


        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            AppTextField(
                security_1,
                { security_1 = it },
                placeholder = "1. ¿Cuál es tu color favorito? ",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop Down"
                    )
                }
            )

            AppTextField(
                securityAnswer_1,
                { securityAnswer_1 = it },
                placeholder = "Respuesta"
            )

            AppTextField(
                security_2,
                { security_2 = it },
                placeholder = "2. Nombre de tu primera mascota ",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop Down"
                    )
                }
            )

            AppTextField(
                securityAnswer_2,
                { securityAnswer_2 = it },
                placeholder = "Respuesta"
            )
            AppTextField(
                security_3,
                { security_3 = it },
                placeholder = "3. ¿Cuál es tu superheroe favorito? ",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop Down"
                    )
                }
            )
            AppTextField(
                securityAnswer_3,
                { securityAnswer_3 = it },
                placeholder = "Respuesta"
            )
        }*/

        AppButton(
            "Crear Cuenta",
            onClick = {
                model.register(user.email,user.password,user.username,user.confirmPassword,
                    onSuccess = {controller.navigate(AppScreens.Homepage.name)},
                    onError = {})
            },
            modifier = Modifier.padding(16.dp)
        )

    }

}

@Preview(showBackground = true)
@Composable
fun Register2Preview() {
    Register2(controller = rememberNavController())
}