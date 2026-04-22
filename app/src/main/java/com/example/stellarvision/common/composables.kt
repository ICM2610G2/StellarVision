package com.example.stellarvision.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.InsertComment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stellarvision.model.NavItem
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.R
import com.example.stellarvision.ui.theme.Gray
import com.example.stellarvision.ui.theme.OnSurfaceVariant
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.ui.theme.PurpleGrey40
import com.example.stellarvision.ui.theme.PurpleGrey80

@Composable
fun AppAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier.size(size).clip(CircleShape).background(Color.LightGray)
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = "person",
            modifier = Modifier.fillMaxWidth().fillMaxSize())
    }
}

val iconsNavBar = listOf(
    NavItem("home", R.drawable.home_icon, "Home", AppScreens.Homepage.name),
    NavItem("map", R.drawable.map_icon, "Map", AppScreens.Mapa.name),
    NavItem("camera", R.drawable.camera_icon, "Camera", AppScreens.Vista360.name),
    NavItem("bell", R.drawable.chat_icon, "Messages", AppScreens.Mensajeria.name),
    NavItem("user", R.drawable.person_icon, "Profile", AppScreens.Perfil.name)
)

@Composable
fun AppButton(
    text : String,
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    color : Color = Primary,
    enabled : Boolean = true,
    icon: Int? = null,
    contentDescription : String = "",
    textColor: Color = Color.White

){
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = MaterialTheme.colorScheme.outline
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painterResource(icon),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(Modifier.padding(horizontal = 8.dp))
            AppText(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor
            )
        }
    }

}

@Composable
fun AppCounterIcon(
    icon: ImageVector,
    count: Int,
    modifier: Modifier= Modifier,
    tint: Color=Color.Black
) {
    Row(modifier=modifier) {
        Icon(imageVector = icon, contentDescription=null, tint=tint)
        Spacer(Modifier.width(8.dp))
        Text(text = count.toString(), fontSize = 14.sp,color=tint)
    }
}

@Composable
fun AppRowButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color = Primary,
    unselectedColor: Color = PurpleGrey80,
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) selectedColor else unselectedColor,
            disabledContainerColor = MaterialTheme.colorScheme.outline
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) unselectedColor else selectedColor
            )
        }
    }
}

@Composable
fun AppSeparator(
    modifier : Modifier = Modifier,
    text : String = ""
){
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f)
                .height(1.dp),
            color = Gray
        )
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(
            modifier = Modifier.weight(1f)
                .height(1.dp),
            color = Gray
        )
    }
}

@Composable
fun AppTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Primary,
    unselectedColor: Color = PurpleGrey80,
    indicatorColor: Color = Color.Black
) {
    Column(
        modifier = modifier.clickable { onClick()},
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text=text,
            color=if (selected) selectedColor else unselectedColor,
            fontWeight = FontWeight.Normal
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.width(22.dp).height(2.dp).background(
                color=if(selected) indicatorColor else Color.Transparent,
                shape = RoundedCornerShape(99.dp)
            )
        )
    }
}

@Composable
fun AppText(
    text : String,
    style : TextStyle = MaterialTheme.typography.bodyLarge,
    color : Color = MaterialTheme.colorScheme.onSurface,
    modifier : Modifier = Modifier,
    textAlign : TextAlign = TextAlign.Center
){
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String)-> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    allowWhitespace: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText : @Composable (() -> Unit)? = null
){
    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> if(allowWhitespace) {
            onValueChange(newValue)
        } else {
            val cleaned = newValue.filter { !it.isWhitespace() }
            onValueChange(cleaned)
        } },
        placeholder = { AppText(
            text = placeholder,
            color = colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        ) },
        maxLines = if(allowWhitespace) Int.MAX_VALUE else 1,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        supportingText = supportingText
    )
}

@Composable
fun CameraButton(onClick: () -> Unit){
    LargeFloatingActionButton(
        onClick = onClick,
        containerColor = Color.White.copy(alpha = 0.7f),
        contentColor = Color.Black,
        shape = CircleShape
    ){
        Image(
            painter = painterResource(R.drawable.camarabutton),
            contentDescription = "Tomar foto",
            modifier = Modifier.size(35.dp)
        )
    }
}

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
        AppRowButton(text = "Todo", selected = selectedIndex === 0, onClick = { onSelect(0) })
        AppRowButton(text="Seguidores",selected= selectedIndex === 1, onClick = {onSelect(1)})
        AppRowButton(text="Comentarios",selected= selectedIndex === 2, onClick = {onSelect(2)})
        AppRowButton(text="Me Gusta",selected= selectedIndex === 3, onClick = {onSelect(3)})

    }
}

@Composable
fun AppHeader(
    appName: String,
    title: String,
    logoRes: Int,
    description : String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            appName,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(logoRes),
            contentDescription = "Logo",
        )
        Spacer(modifier = Modifier.height(32.dp))
        AppText(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppText(
            description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

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

@Composable
fun CommentPreview(
    userName: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier= modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        AppAvatar(size = 34.dp)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(userName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            Text(text, fontSize = 13.sp)
        }
    }
}

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
        AppTabItem(text = "Seguidos", selected = selectedIndex === 0, onClick = { onSelect(0) })
        AppTabItem(text="Para ti", selected = selectedIndex === 1, onClick = {onSelect(1)})
        AppTabItem(text="Favoritos", selected = selectedIndex === 2, onClick = {onSelect(2)})
    }
}

@Composable
fun NotificationHeader(
    userName: String,
    groupText: String,
    onMore: () -> Unit,
    modifier: Modifier= Modifier
) {
    Row(
        modifier=modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAvatar(size = 42.dp)
        Spacer(Modifier.width(12.dp))

        Column(modifier= Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(userName, fontWeight= FontWeight.SemiBold, fontSize=12.sp)
                Spacer(Modifier.width(6.dp))
                Text(groupText, color= PurpleGrey80, fontSize = 12.sp)
            }
        }
        IconButton(onClick = onMore) {
            Icon(Icons.Default.Favorite, contentDescription = "like")
        }
    }
}

@Composable
fun PostActionsRow(
    likes: Int,
    comments: Int,
    modifier: Modifier= Modifier
) {
    Row(modifier=modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        AppCounterIcon(Icons.Outlined.FavoriteBorder, count = likes)
        Spacer(Modifier.width(24.dp))
        AppCounterIcon(Icons.Outlined.MailOutline ,count = comments)
    }
}

@Composable
fun PostHeader(
    userName: String,
    groupText: String,
    onMore: () -> Unit,
    modifier: Modifier= Modifier
) {
    Row(
        modifier=modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAvatar(size = 42.dp)
        Spacer(Modifier.width(12.dp))

        Column(modifier= Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(userName, fontWeight= FontWeight.SemiBold, fontSize=12.sp)
                Spacer(Modifier.width(6.dp))
                Text(groupText, color= PurpleGrey80, fontSize = 12.sp)
            }
        }
        IconButton(onClick = onMore) {
            Icon(Icons.Default.MoreVert, contentDescription = "more")
        }
    }
}

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

@Composable
fun BottomBar(
    controller: NavController,
    items: List<NavItem>,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
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
    },
        content = content
    )
}

@Composable
fun NotificationCard(
    userName: String,
    groupText: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier=modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical=10.dp)
    ) {
        NotificationHeader(userName, groupText, onMore = {})

        Spacer(Modifier.height(8.dp))

        Text(
            body,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(10.dp))

    }
}

@Composable
fun PostCard(
    userName: String,
    groupText: String,
    body: String,
    likes: Int,
    comments: Int,
    previewUser: String,
    previewText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier=modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical=10.dp)
    ) {
        PostHeader(userName, groupText, onMore = {})

        Spacer(Modifier.height(8.dp))

        Text(
            body,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(10.dp))

        Image(
            painter = painterResource(id=R.drawable.orion_post),
            contentDescription = null,
            modifier= Modifier.fillMaxWidth().height(230.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(10.dp))

        PostActionsRow(likes, comments)

        Spacer(Modifier.height(14.dp))

        CommentPreview(userName = previewUser, text = previewText)
    }
}




