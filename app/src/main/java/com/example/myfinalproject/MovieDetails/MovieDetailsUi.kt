package com.example.myfinalproject.MovieDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myfinalproject.Auth.netflixFont
import com.example.myfinalproject.Model.Data.MovieDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel,
    onBack: () -> Unit = {},
    onShowSnackbar: (String) -> Unit = {}
) {
    val movie by viewModel.movieState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    movie?.let {
        MovieDetailsContent(
            movie = it,
            isFavorite = isFavorite,
            onBack = onBack,
            onToggleFavorite = {
                viewModel.toggleFavorite()
                onShowSnackbar(
                    if (!isFavorite) "Added to favorites "
                    else "Removed from favorites"
                )
            }
        )
    } ?: LoadingIndicator()
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsContent(
    movie: MovieDetails,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        TopAppBar(
            title = { Text(movie.title, color = Color.White, fontFamily = netflixFont) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )

        LazyColumn {
            item {
                MoviePosterSection(movie)
                MovieInfoSection(movie)
                MovieOverviewSection(movie)
            }
        }
    }
}
@Composable
fun MoviePosterSection(movie: MovieDetails) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.backdrop_path ?: movie.poster_path}"
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().height(250.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MovieInfoSection(movie: MovieDetails) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontFamily = netflixFont
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
            Text(
                text = " ${movie.vote_average}/10",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Text(
            text = "Release: ${movie.release_date}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = movie.genres.joinToString(", ") { it.name },
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun MovieOverviewSection(movie: MovieDetails) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontFamily = netflixFont
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
