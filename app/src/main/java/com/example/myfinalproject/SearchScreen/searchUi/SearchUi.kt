package com.example.myfinalproject.SearchScreen.searchUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinalproject.Auth.netflixFont
import com.example.myfinalproject.BottomNavigationBar
import com.example.myfinalproject.Model.Data.SearchUiState
import com.example.myfinalproject.SearchScreen.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onMovieClick: (Int) -> Unit,
    onNavigate: (String) -> Unit,
    currentRoute: String
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
        ) {
            // Header
            Text(
                text = "SEARCH",
                color = Color.Red,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = netflixFont,
                modifier = Modifier.padding(16.dp)
            )

            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = viewModel::search,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips
            CategoryChips(
                selectedCategory = selectedCategory,
                onCategorySelected = viewModel::onCategorySelected,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is SearchUiState.Initial -> EmptySearchState()
                    is SearchUiState.Loading -> LoadingState()
                    is SearchUiState.Success -> SearchResultsContent(
                        searchResult = state.searchResult,
                        selectedCategory = selectedCategory,
                        onMovieClick = onMovieClick,
                        onGenreClick = viewModel::onGenreClick
                    )
                    is SearchUiState.Error -> ErrorState(message = state.message)
                }
            }
        }
    }
}





