package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import android.R.attr.textColor
import android.R.id.home
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import uk.ac.tees.mad.careerconnect.data.remote.uriToByteArray
import uk.ac.tees.mad.shoplocal.R
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.HomeViewModel
import uk.ac.tees.mad.shoplocal.ui.BottomNavigation
import uk.ac.tees.mad.shoplocal.ui.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
) {


    LaunchedEffect(Unit) {
        homeViewModel.fetchCurrentUserData()
    }
    val currentUser = homeViewModel.currentUserData.collectAsState().value
    val context = LocalContext.current
    var update by remember { mutableStateOf(false) }
    var newName by rememberSaveable { mutableStateOf("") }
    var newMobileNum by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val cornerShape = RoundedCornerShape(14.dp)
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val freshUrl = "${currentUser.profileImageUrl}?t=${System.currentTimeMillis()}"
    val imageRequest = ImageRequest.Builder(context).data(freshUrl).crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED).build()
    val painter = rememberAsyncImagePainter(model = imageRequest)

    // Access state directly (no collectAsState needed)

    val state by painter.state.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val bgColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground
    val defaulImagetUri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.drawable.default_profile}"

    )

    val imageUri: Uri = if (selectedImageUri == null) {
        defaulImagetUri
    } else {
        selectedImageUri!!
    }

//android 13
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })

//android 12
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })



    Scaffold(bottomBar = { BottomNavigation(navController = navController) }, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    modifier = Modifier.padding(start = 10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.background
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF0184FE),
                titleContentColor = MaterialTheme.colorScheme.background,
                actionIconContentColor = MaterialTheme.colorScheme.background
            )
        )
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {

                    if (selectedImageUri != null) {

                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    } else if (state is AsyncImagePainter.State.Loading) {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)
                        ) {
                            AsyncImage(
                                model = R.drawable.pf,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )


                            CircularProgressIndicator(
                                color = Color.Black,

                                strokeWidth = 2.dp, modifier = Modifier.size(30.dp)

                            )
                        }


                    } else if (state is AsyncImagePainter.State.Success) {

                        AsyncImage(
                            model = imageRequest,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        AsyncImage(
                            model = R.drawable.pf,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    }
                    if (isEditing) {
                        IconButton(
                            onClick = {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                } else {
                                    val intent = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    galleryLauncher.launch(intent)
                                }
                            }, modifier = Modifier
                                .size(35.dp)
                                .background(
                                    color = Color(0xFF0184FE), CircleShape
                                )
                                .size(30.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Image",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                }
                Spacer(modifier = Modifier.height(8.dp))

                if (isEditing == false) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Edit your profile",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = textColor
                        )

                        IconButton(onClick = {
                            isEditing = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = textColor
                            )
                        }
                    }


                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newName else currentUser.name,
                    enabled = isEditing,
                    onValueChange = { input ->
                        newName = input.split(" ").joinToString(" ") { word ->
                            if (word.isNotEmpty()) word.replaceFirstChar { it.uppercase() }
                            else word
                        }
                    },
                    label = { Text("Name", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = cornerShape,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        disabledTextColor = textColor,
                        focusedContainerColor = bgColor,
                        unfocusedContainerColor = bgColor,
                        disabledContainerColor = bgColor,
                        focusedIndicatorColor = textColor,
                        unfocusedIndicatorColor = textColor.copy(alpha = 0.5f),
                        disabledIndicatorColor = textColor.copy(alpha = 0.3f),
                        focusedLabelColor = textColor,
                        unfocusedLabelColor = textColor.copy(alpha = 0.8f),
                        disabledLabelColor = textColor.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = if (isEditing) newMobileNum else currentUser.mobNumber,
                    enabled = isEditing,
                    onValueChange = {
                        newMobileNum = it
                    },
                    label = {
                        Text(
                            if (currentUser?.mobNumber.isNullOrEmpty()) "Add Mobile" else "Update Mobile",
                            color = textColor
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = cornerShape,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        disabledTextColor = textColor,
                        focusedContainerColor = bgColor,
                        unfocusedContainerColor = bgColor,
                        disabledContainerColor = bgColor,
                        focusedIndicatorColor = textColor,
                        unfocusedIndicatorColor = textColor.copy(alpha = 0.5f),
                        disabledIndicatorColor = textColor.copy(alpha = 0.3f),
                        focusedLabelColor = textColor,
                        unfocusedLabelColor = textColor.copy(alpha = 0.8f),
                        disabledLabelColor = textColor.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = if (isEditing) currentUser.email else currentUser.email,
                    enabled = false,
                    onValueChange = {

                    },
                    label = {
                        Text(
                            " Email ", color = textColor
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = cornerShape,
                    isError = showError,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        disabledTextColor = textColor,
                        focusedContainerColor = bgColor,
                        unfocusedContainerColor = bgColor,
                        disabledContainerColor = bgColor,
                        focusedIndicatorColor = textColor,
                        unfocusedIndicatorColor = textColor.copy(alpha = 0.5f),
                        disabledIndicatorColor = textColor.copy(alpha = 0.3f),
                        focusedLabelColor = textColor,
                        unfocusedLabelColor = textColor.copy(alpha = 0.8f),
                        disabledLabelColor = textColor.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (isEditing) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isEditing = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, textColor)
                        ) {
                            Text("Cancel", color = textColor)
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        Button(


                            onClick = {
                                isLoading = true
                                val profielImageByteArray = imageUri.uriToByteArray(context)
                                profielImageByteArray?.let() {
                                    homeViewModel.updateProfile(
                                        ProfielImageByteArray = profielImageByteArray,
                                        name = if (newName.isNotBlank()) newName else currentUser.name,
                                        mob = if (newMobileNum.isNotBlank()) newMobileNum else currentUser.mobNumber,
                                        onResult = { message, boolean ->
                                            if (boolean) {
                                                Toast.makeText(
                                                    context, message, Toast.LENGTH_SHORT
                                                ).show()

                                                isEditing = false
                                                isLoading = false

                                            } else {
                                                isLoading = false

                                                Toast.makeText(
                                                    context, message, Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        },
                                    )
                                }

                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0184FE)
                            )
                        ) {


                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    "Update",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        homeViewModel.logoutUser()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0184FE)
                    )
                ) {
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }




            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "ShopLocal – Profile Screen")
@Composable
fun ProfileScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0184FE))
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800",
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(35.dp)
                        .background(Color(0xFF0184FE), CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = "Sarah Johnson",
                onValueChange = {},
                label = { Text("Name") },
                enabled = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "+44 7700 900123",
                onValueChange = {},
                label = { Text("Update Mobile") },
                enabled = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "sarah.j@example.com",
                onValueChange = {},
                label = { Text("Email") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0184FE))
                ) {
                    Text("Update", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0184FE))
            ) {
                Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}