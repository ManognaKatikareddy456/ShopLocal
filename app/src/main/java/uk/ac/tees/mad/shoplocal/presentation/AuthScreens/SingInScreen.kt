package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.AuthViewModel
import uk.ac.tees.mad.shoplocal.presentation.navigation.Routes

@Composable
fun SingInScreen(authViewModel: AuthViewModel, navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var trigger by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isGoogleLoading by rememberSaveable { mutableStateOf(false) }

    val primaryBlue = Color(0xFF0184FE)
    val textColor = Color.Black
    val context = LocalContext.current
    val cornerShape = RoundedCornerShape(14.dp)

    // Automatically toggle password visibility after delay
    LaunchedEffect(trigger) {
        delay(3000)
        passwordVisible = !passwordVisible
    }

    // Email and Password Validation
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val passwordRegex = Regex("^(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,10}\$")
    val isPasswordValid = passwordRegex.matches(password)
    val isFormValid = name.isNotBlank() && isEmailValid && isPasswordValid

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
                    append("Create your ")
                    withStyle(
                        style = SpanStyle(
                            color = primaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("ShopLocal") }
                    append(" account")
                },
                style = MaterialTheme.typography.headlineSmall,
                color = primaryBlue,
                modifier = Modifier.padding(horizontal = 12.dp),
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(38.dp))

            // Full Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth(),
                shape = cornerShape,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth(),
                shape = cornerShape,
                isError = email.isNotEmpty() && !isEmailValid,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth(),
                shape = cornerShape,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                isError = password.isNotEmpty() && !isPasswordValid,
                singleLine = true
            )
        }

        // Bottom Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sign Up Button
            Button(
                onClick = {
                    if (isFormValid) {
                        isLoading = true
                        authViewModel.signUp(
                            email = email,
                            password = password,
                            name = name,
                            onResult = { message, success ->
                                isLoading = false
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) {
                                    navController.navigate(Routes.HomeScreen)
                                }
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill all fields correctly",
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
                        text = "Sign Up",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Login Redirect Text
            TextButton(onClick = {
                navController.navigate(Routes.LogInScreen)
            }) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = textColor)) {
                            append("Already have an account? ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = primaryBlue,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) { append("Log in") }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
