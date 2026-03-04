package com.example.stellarvision.Screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.molecules.ActivityTopRow
import com.example.stellarvision.ui.templates.BottomBar
import com.example.stellarvision.ui.templates.NotificationCard


@Composable
fun Actividad(controller: NavController)
{
    BottomBar(controller, iconsNavBar) { padding ->
        var selected by rememberSaveable { mutableIntStateOf(1) }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {

            ActivityTopRow(
                selectedIndex = selected,
                modifier = Modifier,
                onSelect = {selected=it}
            )

            NotificationCard(
                userName = "Felipe",
                groupText = "Hace 1 dia",
                body = "Le dio like a tu post"
            )
        }



    }
}

@Preview(showBackground = true)
@Composable
fun ActividadPreview() {
    Actividad(controller = rememberNavController())
}