package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.AuthViewModel

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.shoplocal.R

import uk.ac.tees.mad.shoplocal.presentation.navigation.Routes

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var trigger by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val primaryBlue = Color(0xFF0184FE)
    val textColor = Color.Black

    LaunchedEffect(trigger) {
        delay(3000)
        passwordVisible = !passwordVisible
    }

    val context = LocalContext.current
    val cornerShape = RoundedCornerShape(14.dp)

    // Email validation
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // Password validation
    val passwordRegex = Regex("^(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,10}\$")
    val isPasswordValid = passwordRegex.matches(password)

    // Enable button only if fields valid
    val isFormValid = isEmailValid && isPasswordValid

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
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                buildAnnotatedString {
                    append("Welcome Back! ")
                    withStyle(
                        style = SpanStyle(
                            color = primaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("Log In") }
                    append(" to continue.")
                },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 12.dp),
                lineHeight = 28.sp,
                color =  primaryBlue
            )

            Spacer(modifier = Modifier.height(38.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email",) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp,),
                modifier = Modifier.fillMaxWidth(),
                shape = cornerShape,
                isError = email.isNotEmpty() && !isEmailValid
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp,),
                modifier = Modifier.fillMaxWidth(),
                shape = cornerShape,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                        trigger = !trigger
                    }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.baseline_visibility_24
                                else R.drawable.outline_visibility_off_24
                            ),
                            contentDescription = null,
                            tint = primaryBlue
                        )
                    }
                },
                isError = password.isNotEmpty() && !isPasswordValid
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    if (isFormValid) {
                        authViewModel.logIn(
                            email = email,
                            passkey = password,
                            onResult = { message, success ->
                                if (success) {
                                    isLoading = true
                                    navController.navigate(Routes.HomeScreen)
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter valid email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = cornerShape
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Text(
                        "Log In",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Text
            TextButton(onClick = {
                navController.navigate(Routes.SingInScreen)
            }) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = textColor)
                        ) { append("Don’t have an account? ") }

                        withStyle(
                            style = SpanStyle(
                                color = primaryBlue,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) { append("Sign Up") }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
