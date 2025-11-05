package uk.ac.tees.mad.shoplocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.AuthViewModel
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.HomeViewModel
import uk.ac.tees.mad.shoplocal.presentation.navigation.Navigation
import uk.ac.tees.mad.shoplocal.ui.theme.ShopLocalTheme
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            ShopLocalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel,
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopLocalTheme {
        Greeting("Android")
    }
}