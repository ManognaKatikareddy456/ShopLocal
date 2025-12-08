package uk.ac.tees.mad.shoplocal.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.tees.mad.shoplocal.presentation.navigation.Routes
import kotlin.collections.forEachIndexed

@Composable
fun BottomNavigation(navController: NavHostController, modifier: Modifier = Modifier) {

    val navItems = listOf(
        NavItems(
            "My Shops",
            filledIcon = Icons.Filled.Store,
            outlinedIcon = Icons.Outlined.Store
        ),
        NavItems(
            "Home",
            filledIcon = Icons.Filled.Home,
            outlinedIcon = Icons.Outlined.Home
        ),

        NavItems(
            "Profile",
            filledIcon = Icons.Filled.AccountCircle,
            outlinedIcon = Icons.Outlined.AccountCircle
        )
    )
    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navController.currentDestination?.route

    NavigationBar(
        modifier = Modifier.height(70.dp),
        containerColor = Color(0xFF0184FE)
    ) {
        navItems.forEachIndexed { index, navItem ->
            val isSelected = selectedIndex == index

            NavigationBarItem(
                modifier = Modifier.offset(y = 10.dp),
                selected = false,
                onClick = {
                    selectedIndex = index

                    when (selectedIndex) {

                        0 -> if (currentRoute != Routes.MyShops::class.qualifiedName) {
                            navController.navigate(Routes.MyShops)
                        }

                        1 -> if (currentRoute != Routes.HomeScreen::class.qualifiedName) {
                            navController.navigate(Routes.HomeScreen)
                        }

                        2 -> if (currentRoute != Routes.ProfileScreen::class.qualifiedName) {
                            navController.navigate(Routes.ProfileScreen)
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) navItem.filledIcon else navItem.outlinedIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background

                    )
                },
                label = {
                    Text(
                        text = navItem.title,
                        modifier = Modifier.offset(y = (-4).dp),
                        color =  MaterialTheme.colorScheme.background
                    )
                }
            )
        }

    }


}

data class NavItems(val title: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector)