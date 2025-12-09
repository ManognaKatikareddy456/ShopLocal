package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import android.R
import android.R.attr.textColor
import android.icu.text.CaseMap
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import uk.ac.tees.mad.shoplocal.data.local.ShopEntity
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.HomeViewModel
import uk.ac.tees.mad.shoplocal.presentation.navigation.Routes
import uk.ac.tees.mad.shoplocal.ui.BottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyShopScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
) {

    var isLoading = homeViewModel.isLoading.collectAsState().value
    val savedShopList by homeViewModel.savedShop.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getSavedShop()
    }

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Shops",
                        modifier = Modifier.padding(start = 10.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0184FE),

                    )
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center

        ) {


            if (savedShopList.isEmpty()) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Color(0xFF0184FE),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "No Shop saved yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(modifier.padding(8.dp)) {
                    items(savedShopList) {
                        MyShoopCard(
                            shopEntity = it, navHostController = navController,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }


        }
    }
}


@Composable
fun MyShoopCard(shopEntity: ShopEntity, navHostController: NavHostController,homeViewModel: HomeViewModel) {
    Card(
        modifier = Modifier
            .height(170.dp)
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 6.dp)
            .clickable {
                navHostController.navigate(
                    Routes.ShopDetailArgs(
                        id = shopEntity.id,
                        name = shopEntity.name,
                        rating = shopEntity.rating.toString(),
                        reviewCount = shopEntity.reviewCount.toString(),
                        price = shopEntity.price ?: "",
                        phone = shopEntity.phone ?: "",
                        url = shopEntity.url,
                        imageUrl = shopEntity.imageUrl,
                        address1 = shopEntity.address1,
                        city = shopEntity.city,
                        state = shopEntity.state,
                        country = shopEntity.country,
                        latitude = shopEntity.latitude.toString(),
                        longitude = shopEntity.longitude.toString()
                    )

                )
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            AsyncImage(
                model = shopEntity.imageUrl,
                contentDescription = shopEntity.name,
                modifier = Modifier
                    .height(130.dp)
                    .width(130.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(20.dp))


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = shopEntity.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))



                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = shopEntity.city ?: "",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "⭐ ${shopEntity.rating}", fontSize = 14.sp, color = Color(0xFF0184FE)
                )
                Text(
                    text = "Ratting Count:${shopEntity.reviewCount}" ?: "",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            val context = LocalContext.current
            IconButton(
                onClick = {

                   homeViewModel.removeShop(
                        shopId = shopEntity.id,
                        onResult = { condition, message ->
                            if (condition) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                homeViewModel.getSavedShop()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                modifier = Modifier
                    .padding(6.dp)
                    .size(40.dp)
                    .background(
                        color = Color(0xFFE3F2FD),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFF0184FE),
                    modifier = Modifier.size(22.dp)
                )
            }



        }
    }
}


@Preview(showBackground = true, name = "My Shops – With Saved Shops")
@Composable
fun MyShopScreenPreview_WithShops() {
    val sampleShops = listOf(
        ShopEntity(
            id = "1",
            name = "The Coffee Corner",
            rating = "4.8",
            reviewCount = "342",
            price = "$$",
            phone = "+44 1642 123456",
            url = "",
            imageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=800",
            address1 = "123 High Street",
            city = "Middlesbrough",
            state = "England",
            country = "UK",
            latitude = "54.5767",
            longitude = "-1.2346"
        ),
        ShopEntity(
            id = "2",
            name = "Green Leaf Books",
            rating = "4.9",
            reviewCount = "189",
            price = "$",
            phone = "+44 1642 789012",
            url = "",
            imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=800",
            address1 = "45 Albert Road",
            city = "Middlesbrough",
            state = "England",
            country = "UK",
            latitude = "54.5755",
            longitude = "-1.2350"
        ),
        ShopEntity(
            id = "3",
            name = "Bakers Delight",
            rating = "4.6",
            reviewCount = "567",
            price = "$",
            phone = "+44 1642 345678",
            url = "",
            imageUrl = "https://images.unsplash.com/photo-1509440154593-7fc03a7f9d79?w=800",
            address1 = "78 Linthorpe Road",
            city = "Middlesbrough",
            state = "England",
            country = "UK",
            latitude = "54.5772",
            longitude = "-1.2331"
        )
    )

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(Color.White.copy(alpha = 0.95f))
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleShops) { shop ->
                MyShoopCard(
                    shopEntity = shop, navHostController = rememberNavController(),
                    homeViewModel = TODO()
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "My Shops – Empty State")
@Composable
fun MyShopScreenPreview_Empty() {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .background(Color.White.copy(alpha = 0.95f))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No saved shops yet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Browse and save your favorite local shops!",
                    fontSize = 16.sp,
                    color = Color.Gray.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}