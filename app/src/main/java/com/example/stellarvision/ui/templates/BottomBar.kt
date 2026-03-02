package com.example.stellarvision.ui.templates

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.stellarvision.Model.NavItem
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.molecules.AppNavigationBar

@Composable
fun BottomBar(
    controller: NavController,
    items: List<NavItem>,
    modifier: Modifier = Modifier,
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(bottomBar = {
        AppNavigationBar(
            items = iconsNavBar,
            buttonSelected = selectedIndex,
            onSelect = { item ->
                selectedIndex = iconsNavBar.indexOfFirst { it.id == item.id }
                controller.navigate(iconsNavBar[selectedIndex].route)
            }
        )
    }
    ) { paddingValues -> paddingValues }
}