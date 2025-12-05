package uk.ac.tees.mad.shoplocal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.shoplocal.presentation.AuthScreens.AuthScreen


import uk.ac.tees.mad.shoplocal.presentation.AuthScreens.HomeScreen
import uk.ac.tees.mad.shoplocal.presentation.AuthScreens.LoginScreen

import uk.ac.tees.mad.shoplocal.presentation.AuthScreens.SingInScreen
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.AuthViewModel
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.HomeViewModel

@Composable
fun Navigation(
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
) {

    val navController = rememberNavController()

    val auth = FirebaseAuth.getInstance()


    var currentUser by remember { mutableStateOf(auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            currentUser = it.currentUser
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    val startDestination = if (currentUser == null) {
        Routes.AuthScreen
    } else {
        Routes.HomeScreen
    }

    NavHost(navController, startDestination = startDestination) {

        composable<Routes.AuthScreen> {


         AuthScreen(
                navController = navController,
            )

        }

        composable<Routes.SingInScreen> {


            SingInScreen(
                authViewModel = authViewModel,

                navController = navController,
            )

        }

        composable<Routes.HomeScreen> {


            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
            )

        }

        composable<Routes.LogInScreen> {


            LoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )

        }


    }


}