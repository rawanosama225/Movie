package com.example.myfinalproject.SearchScreen.searchUi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myfinalproject.Model.Data.SearchCategory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text(
                "Search movies, actors, or genres...",
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.White
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.1f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.Red,
            focusedIndicatorColor = Color.Red,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun CategoryChips(
    selectedCategory: SearchCategory,
    onCategorySelected: (SearchCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            CategoryChip(
                text = "Movies",
                icon = Icons.Default.PlayArrow,
                isSelected = selectedCategory == SearchCategory.MOVIES,
                onClick = { onCategorySelected(SearchCategory.MOVIES) }
            )
        }
        item {
            CategoryChip(
                text = "Actors",
                icon = Icons.Default.Person,
                isSelected = selectedCategory == SearchCategory.ACTORS,
                onClick = { onCategorySelected(SearchCategory.ACTORS) }
            )
        }
        item {
            CategoryChip(
                text = "Genres",
                icon = Icons.Default.List,
                isSelected = selectedCategory == SearchCategory.GENRES,
                onClick = { onCategorySelected(SearchCategory.GENRES) }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        enabled = true,
        label = {
            Text(
                text = text,
                color = if (isSelected) Color.Black else Color.White
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isSelected) Color.Black else Color.White
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Red,
            containerColor = Color.White.copy(alpha = 0.1f),
            labelColor = if (isSelected) Color.Black else Color.White,
            iconColor = if (isSelected) Color.Black else Color.White,
        ),

    )
}
