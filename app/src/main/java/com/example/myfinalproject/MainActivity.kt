package com.example.myfinalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.myfinalproject.Auth.AppNavHost
import com.example.myfinalproject.ui.theme.MyAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



     //   val repository = MovieRepository(api)
       // val viewModel = HomeViewModel(repository)
        FirebaseApp.initializeApp(this)


        setContent {
            MyAppTheme {

                val navController = rememberNavController()

                AppNavHost(navController = navController)
            }
        }
    }
}


