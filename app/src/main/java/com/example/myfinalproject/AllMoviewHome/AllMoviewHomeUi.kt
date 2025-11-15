package com.example.myfinalproject.AllMoviewHome

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myfinalproject.Model.Data.Movie
import com.example.myfinalproject.Auth.netflixFont
import kotlinx.coroutines.delay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.myfinalproject.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSignOut: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onNavigate: (String) -> Unit = {},
    currentRoute: String = "home"
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    //  val pullRefreshState = rememberPullRefreshState(
    //    refreshing = isRefreshing,
    //    onRefresh = { viewModel.fetchMovies(isRefresh = true) }
    // )
    LaunchedEffect(Unit) {
        viewModel.initializeIfNeeded()
    }
    Scaffold(
        containerColor = Color(0xFF000000),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
            // .pullRefresh(pullRefreshState)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    LoadingScreen()
                }
                is HomeUiState.Success -> {
                    HomeContent(
                        nowPlaying = state.nowPlaying,
                        popular = state.popular,
                        topRated = state.topRated,
                        upcoming = state.upcoming,
                        onSignOut = onSignOut,
                        onMovieClick = onMovieClick,
                        onToggleFavorite = { movieId, isFavorite ->
                            viewModel.toggleFavorite(movieId, isFavorite)
                        }
                    )
                }
                is HomeUiState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.retry() }
                    )
                }
            }

//            PullRefreshIndicator(
//                refreshing = isRefreshing,
//                state = pullRefreshState,
//                modifier = Modifier.align(Alignment.TopCenter),
//                backgroundColor = Color.Black,
//                contentColor = Color.Red
//            )
        }
    }
}

@Composable
fun HomeContent(
    nowPlaying: List<Movie>,
    popular: List<Movie>,
    topRated: List<Movie>,
    upcoming: List<Movie>,
    onSignOut: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit
) {
    val allMovieLists = listOf(nowPlaying,popular,topRated)
    val randomMovies = allMovieLists.random().shuffled().take(5)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MOVIEVIBE",
                    color = Color.Red,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = netflixFont
                )

                IconButton(onClick = onSignOut) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "Sign Out",
                        tint = Color.White
                    )
                }
            }
        }

        // Highlight Slider
        item {
            if (nowPlaying.isNotEmpty()) {
                HighlightSlider(
                    movies = randomMovies,
                    onMovieClick = onMovieClick
                )
            }
        }

        // Now Playing
        if (nowPlaying.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("Now Playing 🎬")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = nowPlaying,
                        key = {"nowPlaying_${it.id}" }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = { onMovieClick(movie.id) },
                            onToggleFavorite = { onToggleFavorite(movie.id, movie.isFavorite) }
                        )
                    }
                }
            }
        }

        // Popular Movies
        if (popular.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Popular Now 🔥")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = popular,
                        key = { "popular_${it.id}" }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = { onMovieClick(movie.id) },
                            onToggleFavorite = { onToggleFavorite(movie.id, movie.isFavorite) }
                        )
                    }
                }
            }
        }

        // Top Rated
        if (topRated.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Top Rated ⭐")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = topRated,
                        key = { "topRated_${it.id}" }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = { onMovieClick(movie.id) },
                            onToggleFavorite = { onToggleFavorite(movie.id, movie.isFavorite) }
                        )
                    }
                }
            }
        }

        // Upcoming
        if (upcoming.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Coming Soon 🎯")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = upcoming,
                        key = { "upcoming_${it.id}" }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = { onMovieClick(movie.id) },
                            onToggleFavorite = { onToggleFavorite(movie.id, movie.isFavorite) }
                        )
                    }
                }
            }
        }


        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading movies...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Retry",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}

@Composable
fun HighlightSlider(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit
) {
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    Log.d("HighlightSlider", "current highlighter $currentIndex")
    LaunchedEffect(movies) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % movies.size
        }
    }

    if (movies.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable { onMovieClick(movies[currentIndex].id) }
        ) {
            AsyncImage(
                model = movies[currentIndex].backdropUrl,
                contentDescription = movies[currentIndex].title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Movie info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = movies[currentIndex].title ?: "",
                    fontFamily = netflixFont,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Dots indicator
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    movies.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (index == currentIndex) Color.White
                                    else Color.White.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = netflixFont,
        fontSize = 22.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onMovieClick() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Favorite button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (movie.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (movie.isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }


                if (movie.vote_average > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = String.format("%.1f", movie.vote_average),
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = movie.title ?: "Untitled",
            fontFamily = netflixFont,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Release date
        movie.release_date?.let { date ->
            Text(
                text = date.take(4),
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
