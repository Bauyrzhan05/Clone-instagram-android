package com.example.myapplication.Projects.Instagram

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.composable
import com.example.myapplication.Projects.Instagram.Authorization.LoginScreen
import com.example.myapplication.Projects.Instagram.Authorization.RegisterScreen
import com.example.myapplication.Projects.Instagram.Pages.ChatsScreen
import com.example.myapplication.Projects.Instagram.Pages.EditProfileScreen
import com.example.myapplication.Projects.Instagram.Pages.MainScreen
import com.example.myapplication.Projects.Instagram.chat.ChatScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyApp(){
    val navController = rememberAnimatedNavController()

    MaterialTheme {
        AnimatedNavHost(
            navController = navController,
            startDestination = "Login"
        ){
            composable("Login") { LoginScreen(navController) }
            composable("Register") { RegisterScreen(navController) }
            composable("MainScreen/{username}/{email}") { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                val userBio = backStackEntry.arguments?.getString("userBio")
                val email = backStackEntry.arguments?.getString("email")
                MainScreen(username =  username, email = email, navController = navController)
            }
            composable("EditProfile/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                EditProfileScreen(email, navController = navController)
            }
            composable("MainScreenProfile/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                MainScreen(navController = navController, email = email, username = null, startIndex = 4)
            }
            composable("chat/{targetUserId}/{currentUserId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("targetUserId") ?: ""
                val userId = backStackEntry.arguments?.getString("currentUserId") ?: ""
                ChatScreen(id = id, currentUserId = userId, navController = navController)
            }

            composable(
                "chats/{id}" ,
                enterTransition = { slideInHorizontally(initialOffsetX = {full -> full}) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = {full -> full}) + fadeOut() }
            ) {
                val id = it.arguments?.getString("id") ?: ""
                ChatsScreen(id = id.toInt(), navController = navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShowMyApp(){
    MyApp()
}