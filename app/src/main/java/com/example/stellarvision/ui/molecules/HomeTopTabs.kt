package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.atoms.AppTabItem

@Composable
fun HomeTopTabs(
    selectedIndex: Int,
    onSelect: (Int)-> Unit,
    modifier: Modifier= Modifier
) {
    Row(
        modifier=modifier.fillMaxWidth().padding(top=10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTabItem(text="Seguidos", selected = selectedIndex === 0, onClick = {onSelect(0)})
        AppTabItem(text="Para ti", selected = selectedIndex === 1, onClick = {onSelect(1)})
        AppTabItem(text="Favoritos", selected = selectedIndex === 2, onClick = {onSelect(2)})
    }
}