package com.example.myfinalproject.SearchScreen.searchUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myfinalproject.Auth.netflixFont
import com.example.myfinalproject.Model.Data.Genre
import com.example.myfinalproject.Model.Data.Movie
import com.example.myfinalproject.Model.Data.Person

@Composable
fun MoviesList(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit
) {
    if (movies.isEmpty()) {
        EmptyResultState("No movies found")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                SearchMovieItem(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) }
                )
            }
        }
    }
}

@Composable
private fun SearchMovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Movie Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title ?: "Untitled",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = netflixFont
                )

                movie.release_date?.let { date ->
                    Text(
                        text = date.take(4),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                if (movie.vote_average > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = String.format("%.1f", movie.vote_average),
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "View details",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
@Composable
fun ActorsList(actors: List<Person>) {
    if (actors.isEmpty()) {
        EmptyResultState("No actors found")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(actors) { actor ->
                ActorItem(actor = actor)
            }
        }
    }
}

@Composable
private fun ActorItem(actor: Person) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = actor.profileUrl,
                contentDescription = actor.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Actor Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = actor.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = netflixFont
                )

                actor.known_for_department?.let { department ->
                    Text(
                        text = department,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                if (actor.known_for.isNotEmpty()) {
                    Text(
                        text = "Known for: ${actor.known_for.firstOrNull()?.title ?: ""}",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun GenresList(
    genres: List<Genre>,
    onGenreClick: (Int) -> Unit
) {
    if (genres.isEmpty()) {
        EmptyResultState("No genres found")
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(genres) { genre ->
                GenreItem(
                    genre = genre,
                    onClick = { onGenreClick(genre.id) }
                )
            }
        }
    }
}

@Composable
private fun GenreItem(
    genre: Genre,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = genre.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = netflixFont
            )
        }
    }
}