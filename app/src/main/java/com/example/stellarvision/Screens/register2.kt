package com.example.stellarvision.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.AppButton
import com.example.stellarvision.ui.atoms.AppText
import com.example.stellarvision.ui.atoms.AppTextField
import com.example.stellarvision.ui.molecules.AppHeader

@Composable
fun Register2(controller: NavController)
{

    var username by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}
    var showPassword by remember{mutableStateOf(false)}
    var security_1 by remember{mutableStateOf("")}
    var securityAnswer_1 by remember{mutableStateOf("")}
    var security_2 by remember{mutableStateOf("")}
    var securityAnswer_2 by remember{mutableStateOf("")}
    var security_3 by remember{mutableStateOf("")}
    var securityAnswer_3 by remember{mutableStateOf("")}

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
            username,
            { username = it },
            placeholder = "Nombre de usuario",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuario"
                )
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        AppTextField(
            password,
            { password = it },
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
                        contentDescription = "Visibilidad"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        AppTextField(
            password,
            { password = it },
            placeholder = "Confirmar contraseña",
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
                        contentDescription = "Visibilidad"
                    )
                }
            }
        )
        AppText(text="Preguntas de Seguridad", modifier = Modifier.align(alignment = Alignment.Start).padding(horizontal = 16.dp))
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
        }

        AppButton(
            "Crear Cuenta",
            onClick = {
                Toast.makeText(context,"Cuenta creada con exito", Toast.LENGTH_LONG).show()
                controller.navigate("login")
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