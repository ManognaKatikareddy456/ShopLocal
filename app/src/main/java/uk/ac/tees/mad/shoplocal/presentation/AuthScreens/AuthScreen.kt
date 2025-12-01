package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import android.R.attr.textColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import uk.ac.tees.mad.shoplocal.R
import uk.ac.tees.mad.shoplocal.presentation.navigation.Routes

@Composable
fun AuthScreen(navController: NavHostController) {
    val blueText = Color(0xFF0184FE)
    val  textColor = Color.Black
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Welcome to ShopLocal",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = blueText,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Find and support shops near you easily.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 15.sp
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Shop illustration",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }

            // Bottom Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate(Routes.SingInScreen) },
                    colors = ButtonDefaults.buttonColors(containerColor = blueText),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Get Started",
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate(Routes.LogInScreen) }) {
                    Text(
                        text = "Already have an account? Log in",
                        color = blueText,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
}
