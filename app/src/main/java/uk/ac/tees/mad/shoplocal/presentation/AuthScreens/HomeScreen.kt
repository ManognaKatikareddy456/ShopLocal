package uk.ac.tees.mad.shoplocal.presentation.AuthScreens

import android.R.attr.onClick
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch
import okhttp3.Route
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Business
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Coordinates
import uk.ac.tees.mad.shoplocal.data.remote.yelpDto.Location
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.AuthViewModel
import uk.ac.tees.mad.shoplocal.presentation.Viewmodels.HomeViewModel
import uk.ac.tees.mad.shoplocal.presentation.navigation.Routes
import uk.ac.tees.mad.shoplocal.ui.BottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
) {

    val listOfBusiness by homeViewModel.listOfBusiness.collectAsStateWithLifecycle()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {

//        homeViewModel.fetchYelpData2(
//            term = "restaurant", cityName = "New York"
//        )
//        homeViewModel.fetchYelpData(
//            term = "shop",
////            latitude = 19.0760,
////            longitude = 72.8777,
//        )


    }

    var cityName by remember { mutableStateOf("Toronto") }
    val foucusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSugsetionVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current


    var selectedKeyword by remember { mutableStateOf<String>("shop") }


    val searchKeywords: List<String> = listOf(
        "shop",
        "grocery",
        "supermarket",
        "restaurant",
        "repair shop",
        "cafe",
        "coffee",
        "bakery",
        "fast food",
        "street food",
        "juice",
        "tea shop",
        "electronics",
        "mobile store",
        "fashion",
        "clothing",
        "bookstore",
        "gift shop",
        "florist",
        "pharmacy",
        "clinic",
        "hospital",
        "dentist",
        "salon",
        "barber",
        "spa",
        "gym",
        "hardware",
        "home decor",
        "furniture",
        "toy store",
        "stationery",
        "sports store",
        "laundry",
        "tailor",
        "plumber",
        "electrician",
        "real estate",
        "photographer",
        "travel agency",
        "car rental",
        "hotel",
        "atm",
        "bank",
        "parking",
        "movie theater",
        "park",
        "music",
        "nightlife",
        "internet cafe",
        "printing",
        "repair shop"
    )


    Scaffold(
        modifier = Modifier.fillMaxSize(), bottomBar = {
            BottomNavigation(navController = navController)
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding()
                , contentAlignment = Alignment.Center

        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(), horizontalAlignment = Alignment.CenterHorizontally

            ) {

                SearchBar(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {

                            isSugsetionVisible = it.isFocused
                        },
                    query = cityName,
                    onQueryChange = {
                        cityName = it

                    },
                    onSearch = {


                        if (selectedKeyword.isBlank() || cityName.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please select a category and enter a city name",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            homeViewModel.fetchYelpData(
                                term = selectedKeyword, cityName = cityName
                            )
                        }

                        keyboardController?.hide()
                        foucusManager.clearFocus()

                    },
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text("Search") },
                    trailingIcon = {

                        IconButton(onClick = {
                            if (cityName.isNotEmpty()) {
                                cityName = ""
                                foucusManager.clearFocus()
                            } else {
                                foucusManager.clearFocus()
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(1000)

                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "close")
                        }

                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }) {


                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchKeywords) { keyword ->

                        SuggestionChip(
                            onClick = {
                                selectedKeyword = keyword

                                if (selectedKeyword.isBlank() || cityName.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "Please select a category and enter a city name",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    homeViewModel.fetchYelpData(
                                        term = selectedKeyword, cityName = cityName
                                    )
                                }
                                keyboardController?.hide()
                                foucusManager.clearFocus()
                            }, label = {
                                Text(
                                    text = keyword,
                                    color = if (selectedKeyword == keyword) Color.White else Color.Black
                                )
                            }, colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (selectedKeyword == keyword) Color(0xFF0184FE)
                                else Color(0xFFF2F2F2)
                            )
                        )
                    }

                }
                val listOfBusiness = uiState.data?.businesses
                listOfBusiness?.let() { list ->
                    LazyColumn {
                        items(list) { business ->
                            BusinessCard(business, navController)
                        }
                        items(1){
                            Spacer(modifier.height(70.dp))
                        }
                    }
                }


            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp), color = Color(0xFF0184FE), strokeWidth = 4.dp
                )
            } else {
                Text(uiState.error)
            }

        }

    }


}


@Composable
fun BusinessCard(business: Business, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .height(170.dp)
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 6.dp)
            .clickable {
                navHostController.navigate(
                    Routes.ShopDetailArgs(
                        id = business.id,
                        name = business.name,
                        rating = business.rating.toString(),
                        reviewCount = business.review_count.toString(),
                        price = business.price ?: "",
                        phone = business.phone ?: "",
                        url = business.url,
                        imageUrl = business.image_url,
                        address1 = business.location.address1,
                        city = business.location.city,
                        state = business.location.state,
                        country = business.location.country,
                        latitude = business.coordinates.latitude.toString(),
                        longitude = business.coordinates.longitude.toString()
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
                model = business.image_url,
                contentDescription = business.name,
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
                    text = business.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))



                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = business.location.city ?: "",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "⭐ ${business.rating}", fontSize = 14.sp, color = Color(0xFF0184FE)
                )
                Text(
                    text = "Ratting Count:${business.review_count}" ?: "",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Home Screen - With Sample Data")
@Composable
fun HomeScreenPreview() {
    val primaryBlue = Color(0xFF0184FE)
    var cityName by remember { mutableStateOf("Toronto") }
    var selectedKeyword by remember { mutableStateOf("cafe") }
    val isLoading = false

    val sampleBusinesses = listOf(
        Business(
            id = "1",
            name = "The Morning Brew Cafe",
            rating = 4.8,
            review_count = 342,
            price = "$$",
            phone = "+14165551234",
            url = "",
            image_url = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=800",
            location = Location(
                address1 = "123 Queen St",
                city = "Toronto",
                state = "ON",
                country = "CA"
            ),
            coordinates = Coordinates(latitude = 43.65, longitude = -79.38)
        ),
        Business(
            id = "2",
            name = "Blue Sail Coffee",
            rating = 4.6,
            review_count = 289,
            price = "$",
            phone = "+14169876543",
            url = "",
            image_url = "https://images.unsplash.com/photo-1504754524776-869f2f54a7a8?w=800",
            location = Location(address1 = "456 King St W", city = "Toronto", state = "ON", country = "CA"),
            coordinates = Coordinates(latitude = 43.65, longitude = -79.38)
        )
    )

    val searchKeywords = listOf("shop", "cafe", "restaurant", "grocery", "bakery", "electronics", "salon", "gym")

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            SearchBar(
                query = cityName,
                onQueryChange = { cityName = it },
                onSearch = { },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search city...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (cityName.isNotEmpty()) {
                        IconButton(onClick = { cityName = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {}

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(searchKeywords) { keyword ->
                    SuggestionChip(
                        onClick = { selectedKeyword = keyword },
                        label = {
                            Text(
                                text = keyword.replaceFirstChar { it.uppercase() },
                                color = if (selectedKeyword == keyword) Color.White else Color.Black
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (selectedKeyword == keyword) primaryBlue else Color(0xFFF2F2F2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(sampleBusinesses) { business ->
                    BusinessCard(business = business, navHostController = rememberNavController())
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp), color = primaryBlue, strokeWidth = 4.dp)
        }
    }
}
