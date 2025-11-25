package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import uk.ac.tees.mad.shoplocal.ui.BottomNavigation
import uk.ac.tees.mad.shoplocal.ui.NavItems

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,navController: NavHostController) {


    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {

        }
    }
}