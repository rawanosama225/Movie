package com.example.myfinalproject.Auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myfinalproject.AllMoviewHome.HomeScreen
import com.example.myfinalproject.AllMoviewHome.HomeViewModel
import com.example.myfinalproject.Model.Repo.MovieRepository
import com.example.myfinalproject.FavScreen.FavoritesScreen
import com.example.myfinalproject.FavScreen.FavoritesViewModel
import com.example.myfinalproject.FavScreen.FavoritesViewModelFactory
import com.example.myfinalproject.Model.Data.AuthMode
import com.example.myfinalproject.Model.DB.AppDatabase
import com.example.myfinalproject.Model.Repo.FavoritesRepository
import com.example.myfinalproject.MovieDetails.MovieDetailsScreen
import com.example.myfinalproject.MovieDetails.MovieDetailsViewModel
import com.example.myfinalproject.MovieDetails.MovieDetailsViewModelFactory
import com.example.myfinalproject.Model.Network.ApiService
import com.example.myfinalproject.Model.Repo.UserRepoImpl
import com.example.myfinalproject.ProfileScreen
import com.example.myfinalproject.SearchScreen.searchUi.SearchScreen
import com.example.myfinalproject.SearchScreen.SearchViewModel
import com.example.myfinalproject.SearchScreen.SearchViewModelFactory
import com.example.myfinalproject.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(navController: NavHostController) {
    var authMode by rememberSaveable { mutableStateOf(AuthMode.SIGNIN) }
    val startDestination = "splash"
        //if (FirebaseAuth.getInstance().currentUser != null) "home" else "auth"
    val authVM: AuthViewModel = viewModel()
    // Create context and database
    val context = LocalContext.current


    val database = remember { AppDatabase.getDatabase(context) }
    val favoritesRepo = remember { FavoritesRepository(database.favoriteMovieDao()) }

    val repository = remember {
        MovieRepository(
            api = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java),
            favoritesRepo = favoritesRepo,
            context = context
        )
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    val userRepo = remember { UserRepoImpl(firebaseAuth) }

    // Create snackbar host state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            //splash
            composable("splash") { SplashScreen(navController) }
            //  Auth
            composable("auth") {
                AuthScreenNew(
                    mode = authMode,
                    state = authVM.uiState,
                    onUsernameChange = authVM::onUsernameChange,
                    onEmailChange = authVM::onEmailChange,
                    onPasswordChange = authVM::onPasswordChange,
                    onConfirmPasswordChange = authVM::onConfirmPasswordChange,
                    onSubmit = {
                        if (authMode == AuthMode.SIGNIN) {
                            authVM.signIn(
                                onSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                },
                                onError = {}
                            )
                        } else {
                            authVM.signUp(
                                onSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                },
                                onError = {}
                            )
                        }
                    },
                    onSwitchMode = {
                        authVM.clearFields()
                        authMode =
                            if (authMode == AuthMode.SIGNIN) AuthMode.SIGN_UP else AuthMode.SIGNIN
                    }
                )
            }

            //  Home
            composable("home") {
                val homeVM: HomeViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return HomeViewModel(repository) as T
                    }
                })

                HomeScreen(
                    viewModel = homeVM,
                    onSignOut = {
                        authVM.signOut()
                        navController.navigate("auth") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    },
                    currentRoute = currentRoute ?: "home"
                )
            }

            //Movie details
            composable("details/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()

                if (movieId != null) {
                    val detailsVM: MovieDetailsViewModel = viewModel(
                        factory = MovieDetailsViewModelFactory(repository,userRepo)
                    )

                    MovieDetailsScreen(
                        movieId = movieId,
                        viewModel = detailsVM,
                        onBack = { navController.popBackStack() },
                        onShowSnackbar = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    )
                }
            }

            // fav screen
            composable("favorites") {
                val favoritesVM: FavoritesViewModel = viewModel(
                    factory = FavoritesViewModelFactory(repository,userRepo)
                )

                FavoritesScreen(
                    viewModel = favoritesVM,
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("favorites")
                            launchSingleTop = true
                        }
                    },
                    currentRoute = currentRoute ?: "favorites"
                )
            }

            // search
            composable("search") {
                val searchVM: SearchViewModel = viewModel(
                    factory = SearchViewModelFactory(repository)
                )

                SearchScreen(
                    viewModel = searchVM,
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("search")
                            launchSingleTop = true
                        }
                    },
                    currentRoute = currentRoute ?: "search"
                )
            }

            // profile
            composable("profile") {
                val username = FirebaseAuth.getInstance().currentUser?.displayName ?: "Guest"

                ProfileScreen(
                    username = username,
                    onSignOut = {
                        authVM.signOut()
                        navController.navigate("auth") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    onFavoritesClick = {
                        navController.navigate("favorites") {
                            popUpTo("profile")
                            launchSingleTop = true
                        }
                    },
                    onSearchClick = {
                        navController.navigate("search") {
                            popUpTo("profile")
                            launchSingleTop = true
                        }
                    },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("profile")
                            launchSingleTop = true
                        }
                    },
                    currentRoute = currentRoute ?: "profile"
                )
            }
        }
    }
}
