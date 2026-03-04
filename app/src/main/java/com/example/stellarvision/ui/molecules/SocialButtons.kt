package com.example.stellarvision.ui.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stellarvision.R
import com.example.stellarvision.ui.atoms.AppButton
import com.example.stellarvision.ui.atoms.AppText
import com.example.stellarvision.ui.theme.OnSurfaceVariant
import com.example.stellarvision.ui.theme.Primary
import org.w3c.dom.Text

@Composable
fun SocialButtons(
    onGoogleClick : () -> Unit,
    onAppleClick : () -> Unit,
    onTextClick : () -> Unit,
    text: String,
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppButton(
            "Continuar con Google",
            onClick = onGoogleClick,
            modifier = Modifier.padding(horizontal = 16.dp)
                .clickable(onClick = onGoogleClick),
            color = OnSurfaceVariant,
            icon = R.drawable.google_icon,
            contentDescription = "Logo Google",
            textColor = Primary
        )
        AppButton(
            "Continuar con Apple",
            onClick = onAppleClick,
            modifier = Modifier.padding(horizontal = 16.dp)
                .clickable(onClick = onAppleClick),
            color = OnSurfaceVariant,
            icon = R.drawable.apple_icon,
            contentDescription = "Logo Apple",
            textColor = Primary
        )
        AppText(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable(onClick = onTextClick)
        )
    }
}
