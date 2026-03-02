package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stellarvision.Model.NavItem
import com.example.stellarvision.R
import com.example.stellarvision.Screens.Homepage
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.ui.theme.PurpleGrey40

@Composable
fun AppNavigationBar(
    items: List<NavItem>,
    buttonSelected: Int,
    onSelect: (NavItem) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 80.dp,
    backgroundColor: Color=Color.White,
    contentColor: Color= PurpleGrey40,
    selectedColor: Color = Color.Black,
    iconSize: Dp = 30.dp,
    itemSize: Dp = 56.dp,
    )
{
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp).height(height).background(backgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == buttonSelected

            Column(
                modifier = modifier.size(itemSize).clickable {onSelect(item)},
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.contentDescription,
                    tint = if (selected) selectedColor else contentColor,
                    modifier = Modifier.size(iconSize),
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
