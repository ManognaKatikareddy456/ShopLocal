package uk.ac.tees.mad.shoplocal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.AuthViewModel
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.HomeViewModel

@Composable
fun Navigation(
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
) {

    val navController = rememberNavController()

}