package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stellarvision.ui.atoms.AppRowButton

@Composable
fun ActivityTopRow(
    selectedIndex: Int,
    onSelect: (Int)-> Unit,
    modifier: Modifier= Modifier
) {
    Row(
        modifier=modifier.fillMaxWidth().padding( 20.dp).horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppRowButton(text="Todo",selected= selectedIndex === 0, onClick = {onSelect(0)})
        AppRowButton(text="Seguidores",selected= selectedIndex === 1, onClick = {onSelect(1)})
        AppRowButton(text="Comentarios",selected= selectedIndex === 2, onClick = {onSelect(2)})
        AppRowButton(text="Me Gusta",selected= selectedIndex === 3, onClick = {onSelect(3)})

    }
}