package com.example.myfinalproject.FavScreen

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myfinalproject.Auth.netflixFont
import com.example.myfinalproject.BottomNavigationBar
import com.example.myfinalproject.EmptyStateScreen
import com.example.myfinalproject.Model.Data.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onMovieClick: (Int) -> Unit,
    onNavigate: (String) -> Unit = {},
    currentRoute: String = "favorites"
) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Favorites",
                        color = Color.White,
                        fontFamily = netflixFont
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            favoriteMovies.isEmpty() -> {
                EmptyFavoritesContent()
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.Black),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteMovies,{ "favorite_${it.id}" }) { movie ->
                        FavoriteMovieItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) },
                            onRemove = { viewModel.removeFavorite(movie.id) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun EmptyFavoritesContent() {
    EmptyStateScreen(
        icon = Icons.Default.FavoriteBorder,
        title = "No favorites yet",
        message = "Add movies to see them here",
        iconTint = Color.Gray,
        textColor = Color.Gray
    )
}


@Composable
fun FavoriteMovieItem(
    movie: Movie,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box {
            Column {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = movie.title ?: "Untitled",
                    fontFamily = netflixFont,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Remove from favorites",
                    tint = Color.White,
                    modifier = Modifier
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(4.dp)
                )
            }
        }
    }
}

//@Composable
//fun EmptyStateScreen(
//    icon: ImageVector,
//    title: String,
//    message: String,
//    iconTint: Color = Color.White.copy(alpha = 0.4f),
//    textColor: Color = Color.White.copy(alpha = 0.6f),
//    modifier: Modifier = Modifier,
//
//) {
//    Column(
//        modifier = modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier.size(100.dp),
//            tint = iconTint
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = title,
//            style = MaterialTheme.typography.headlineSmall,
//            color = textColor,
//            fontFamily = netflixFont
//        )
//        Text(
//            text = message,
//            style = MaterialTheme.typography.bodyMedium,
//            color = textColor.copy(alpha = 0.6f)
//        )
//    }
//}
