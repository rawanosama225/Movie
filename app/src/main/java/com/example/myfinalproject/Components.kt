package com.example.myfinalproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myfinalproject.Auth.netflixFont

@Composable
fun EmptyStateScreen(
    icon: ImageVector,
    title: String,
    message: String,
    iconTint: Color = Color.White.copy(alpha = 0.4f),
    textColor: Color = Color.White.copy(alpha = 0.6f),
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = iconTint
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor,
            fontFamily = netflixFont
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor.copy(alpha = 0.6f)
        )
    }
}
@Composable
fun BottomNavigationBar(
    currentRoute: String = "home",
    onNavigate: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
            label = { Text("Home", color = Color.White) }
        )
        NavigationBarItem(
            selected = currentRoute == "favorites",
            onClick = { onNavigate("favorites") },
            icon = {
                Icon(
                    if (currentRoute == "favorites") Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorites",
                    tint = if (currentRoute == "favorites") Color.Red else Color.White
                )
            },
            label = { Text("Favorites", color = Color.White) }
        )
        NavigationBarItem(
            selected = currentRoute == "search",
            onClick = { onNavigate("search") },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White) },
            label = { Text("Search", color = Color.White) }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White) },
            label = { Text("Profile", color = Color.White) }
        )
    }
}

