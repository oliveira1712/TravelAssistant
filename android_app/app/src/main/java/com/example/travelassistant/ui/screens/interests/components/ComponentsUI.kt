package com.example.travelassistant.ui.screens.interests.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.travelassistant.R
import com.example.travelassistant.models.googledirections.*
import com.example.travelassistant.models.googleplaces.placesnearby.PlaceType
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResult
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResultSimplifiedRoom
import com.example.travelassistant.models.googleplaces.placesnearby.PointsOfInterestFilters
import com.example.travelassistant.models.location.LocationDetails
import com.example.travelassistant.ui.screens.interests.InterestsViewModel
import com.example.travelassistant.ui.screens.map.MapViewModel
import com.example.travelassistant.ui.shared.components.*
import com.example.travelassistant.ui.theme.Green500
import com.example.travelassistant.ui.theme.Green700
import com.example.travelassistant.ui.theme.Red500
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.TestTags.POI_CARD
import com.example.travelassistant.utils.TestTags.POI_CARD_GO_BUTTON
import com.example.travelassistant.utils.TestTags.POI_CARD_TEXT
import com.example.travelassistant.utils.getDistanceBetweenCoordinates
import com.example.travelassistant.utils.startNavigation
import com.example.travelassistant.viewmodels.TravelAssistantViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun DrawSwipableCards(
    onNavigateToMap: () -> Unit, currentLocation: LocationDetails?, navigationType: String
) {
    val interestsViewModel: InterestsViewModel = viewModel()
    val pointsOfInterest = interestsViewModel.pointsOfInterestAPI.observeAsState()
    val pointsOfInterestData = pointsOfInterest.value?.data?.results!!
    var isResultEmpty by remember { mutableStateOf(pointsOfInterestData.isEmpty()) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isResultEmpty) {
            //Stay behind all cards so that when all cards are swiped it will be displayed
            NoResults(
                title = stringResource(id = R.string.no_more_results),
                description = stringResource(id = R.string.no_more_results_sub_text)
            )
        }

        pointsOfInterestData.let { places ->
            DrawSwipableCard(
                pointsOfInterest = places,
                index = places.size - 1,
                onResultEmpty = { isResultEmpty = true },
                onSwipeToSave = {
                    interestsViewModel.insertInterestIntoFirebaseDB(it)
                },
                onNavigateToMap = onNavigateToMap,
                currentLocation = currentLocation,
                navigationType = navigationType
            )
        }
    }
}

@Preview
@Composable
fun DrawSwipableCardsPreview() {
    val navController = rememberNavController()

    TravelAssistantTheme {
        DrawSwipableCards(
            onNavigateToMap = { navController.navigate("map") { popUpTo(0) } },
            currentLocation = LocationDetails("41.15792728559325", "-8.629091435716628"),
            navigationType = "TravelAssistant"
        )
    }
}

@Composable
private fun DrawSwipableCard(
    pointsOfInterest: List<PointOfInterestResult>,
    index: Int,
    onResultEmpty: () -> Unit,
    onSwipeToSave: (interest: PointOfInterestResult) -> Unit,
    onNavigateToMap: () -> Unit,
    currentLocation: LocationDetails?,
    navigationType: String
) {
    var counter = 0;

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val cardHeight = screenHeight - 100.dp

    if (index == -1) {
        onResultEmpty()
        return
    }

    DraggableCard(item = pointsOfInterest[index],
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .testTag(POI_CARD),
        onSwiped = { swipeResult, swipedItem ->
            println(swipeResult)
            if (swipeResult == SwipeResult.ACCEPTED && counter < 1) {
                counter += 1
                onSwipeToSave(pointsOfInterest[index])
            }
            DrawSwipableCard(
                pointsOfInterest = pointsOfInterest,
                index = index - 1,
                onResultEmpty = onResultEmpty,
                onSwipeToSave = onSwipeToSave,
                currentLocation = currentLocation,
                onNavigateToMap = onNavigateToMap,
                navigationType = navigationType
            )
        }) {
        MyInterestCard(
            place = pointsOfInterest[index],
            currentLocation = currentLocation,
            onNavigateToMap = onNavigateToMap,
            navigationType = navigationType
        )
    }
}

@Composable
fun MyInterestCard(
    place: PointOfInterestResult,
    currentLocation: LocationDetails?,
    modifier: Modifier = Modifier,
    onNavigateToMap: () -> Unit,
    navigationType: String
) {
    val mContext = LocalContext.current
    val mapViewModel: MapViewModel = viewModel(mContext as ComponentActivity)
    val currentLat = currentLocation?.latitude?.toDouble()
    val currentLon = currentLocation?.longitude?.toDouble()
    var showProgressIndicator by remember { mutableStateOf(true) }
    val distanceInMeters = if (currentLat != null && currentLon != null) {
        getDistanceBetweenCoordinates(
            startLatitude = currentLat,
            startLongitude = currentLon,
            endLatitude = place.geometry.location.lat,
            endLongitude = place.geometry.location.lng
        ).toInt()
    } else 0
    val imageURL = if (place.photos != null) {
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400" + "&photo_reference=${place.photos[0].photo_reference}" + "&key=${Constants.GOOGLE_API_KEY}"
    } else {
        "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="
    }

    val routeOptions = DirectionRequestBody(
        destination = Destination(
            Location(
                LatLngDirections(
                    place.geometry.location.lat, place.geometry.location.lng
                )
            )
        ),
        routeModifiers = RouteModifiers(avoidHighways = true, avoidTolls = true),
    )


    Column(
        modifier = modifier.border(
            1.dp,
            if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
            RoundedCornerShape(16.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.70f)
                .fillMaxWidth()
        ) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                if (showProgressIndicator) {
                    CircularProgressIndicator()
                }

                AsyncImage(
                    model = imageURL,
                    contentDescription = "Image of the place",
                    contentScale = ContentScale.Crop,
                    onSuccess = {
                        showProgressIndicator = false
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colors.background.copy(0.85f)),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = place.name,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                        .testTag(POI_CARD_TEXT),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = if (MaterialTheme.colors.isLight) Color.DarkGray else Color.LightGray,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start
                    )
                )
                Icon(
                    imageVector = Icons.Outlined.Place,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    tint = Green700,
                    contentDescription = null
                )
                Text(
                    text = if (distanceInMeters < 1000) "$distanceInMeters m" else "${distanceInMeters / 1000} Km",
                    color = Green700
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column() {
                    RatingBar(
                        rating = place.rating, starsColor = Color(0XFFE3A008)
                    )

                    if (place.opening_hours != null) {
                        Card(
                            modifier = Modifier.padding(top = 10.dp),
                            backgroundColor = if (place.opening_hours.open_now) Color(0XFFBCF0DA) else Color(
                                0XFFFEE2E2
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (place.opening_hours.open_now) "Opened" else "Closed",
                                color = if (place.opening_hours.open_now) Color(0XFF014737) else Color(
                                    0XFF991B1B
                                ),
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    } else {
                        Card(
                            modifier = Modifier.padding(top = 10.dp),
                            backgroundColor = Color(0XFFe0f2fe),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = place.business_status.lowercase().replaceFirstChar {
                                    it.titlecase()
                                }, color = Color(0xFF075985), modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }

                MyIconButton(
                    modifier = Modifier.testTag(POI_CARD_GO_BUTTON),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = Green500, contentColor = Color.White
                    ),
                    icon = Icons.Outlined.NearMe,
                    iconColor = Color.White,
                    onClick = {
                        startNavigation(
                            context = mContext,
                            mapViewModel = mapViewModel,
                            routeOptions = routeOptions,
                            navigationType = navigationType,
                            onNavigateToMap = onNavigateToMap
                        )
                    },
                    label = stringResource(id = R.string.go_text)
                )
            }
        }
    }
}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
) {

    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))

    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }

        if (halfStar) {
            Icon(
                imageVector = Icons.Outlined.StarHalf, contentDescription = null, tint = starsColor
            )
        }

        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

@Composable
fun Chip(
    placeType: PlaceType,
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
) {
    val contentColor = if (MaterialTheme.colors.isLight) Color.DarkGray else Color.LightGray
    Surface(
        modifier = Modifier.padding(4.dp),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background
    ) {
        Row(modifier = Modifier.toggleable(value = isSelected, onValueChange = {
            onSelectionChanged(placeType.value)
        }), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                tint = if (isSelected) Color.White else contentColor,
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp),
                imageVector = placeType.icon,
                contentDescription = null
            )
            Text(
                text = stringResource(id = placeType.id),
                color = if (isSelected) Color.White else contentColor,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun ChipGroup(
    placeTypes: List<PlaceType>,
    selectedPlaceType: String = "",
    onSelectedChanged: (String) -> Unit = {},
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
        LazyRow {
            items(placeTypes.size) { placeTypeIt ->
                Chip(
                    placeType = placeTypes[placeTypeIt],
                    isSelected = selectedPlaceType == placeTypes[placeTypeIt].value,
                    onSelectionChanged = {
                        onSelectedChanged(it)
                    },
                )
            }
        }
    }
}


@Preview
@Composable
fun RatingPreview() {
    RatingBar(rating = 2.5)
}

@Preview
@Composable
fun TenStarsRatingPreview() {
    RatingBar(stars = 10, rating = 8.5)
}

@Preview
@Composable
fun RatingPreviewFull() {
    RatingBar(rating = 5.0)
}

@Preview
@Composable
fun RatingPreviewWorst() {
    RatingBar(rating = 1.0)
}

@Preview
@Composable
fun RatingPreviewDisabled() {
    RatingBar(rating = 0.0, starsColor = Color.Gray)
}

@Composable
fun MySwippableCardShimmer(brush: Brush = MyShimmerBrushBackground()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(all = 16.dp),
    ) {
        Column() {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.7f)
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush)
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.5f)
                        .size(20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(brush)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.3f)
                        .size(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.4f)
                            .size(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.2f)
                            .padding(top = 10.dp)
                            .size(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.2f)
                        .size(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MySwippableCardShimmerPreview() {
    MySwippableCardShimmer(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun MySwippableCardShimmerDarkPreview() {
    MySwippableCardShimmer(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}


@Composable
fun FiltersCustomDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (PointsOfInterestFilters) -> Unit,
    filters: PointsOfInterestFilters
) {
    val hoursOptions = listOf(
        stringResource(id = R.string.hours_options_any_hour),
        stringResource(id = R.string.hours_options_opened_now)
    )

    val sortByOptions = listOf(
        stringResource(id = R.string.sortby_options_relevance),
        stringResource(id = R.string.sortby_options_distance)
    )

    var selectedHour by remember {
        mutableStateOf(if (!filters.openNow) hoursOptions[0] else hoursOptions[1])
    }

    var selectedSortBy by remember {
        mutableStateOf(if (filters.rankBy == "prominence") sortByOptions[0] else sortByOptions[1])
    }

    var selectedRadius by remember {
        mutableStateOf(filters.radius.toFloat())
    }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {

            Column {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.dialog_filters_title),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column {
                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                text = stringResource(id = R.string.hours_text)
                            )

                            MyButtonGroup(options = hoursOptions,
                                selectedOption = selectedHour,
                                onOptionSelected = { option ->
                                    selectedHour = option
                                })

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                text = stringResource(id = R.string.sortby_text)
                            )

                            MyButtonGroup(options = sortByOptions,
                                selectedOption = selectedSortBy,
                                onOptionSelected = { option ->
                                    selectedSortBy = option
                                })

                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                text = "${stringResource(id = R.string.radius_text)} ${selectedRadius.toInt()} Km"
                            )

                            Slider(
                                value = selectedRadius,
                                onValueChange = {
                                    selectedRadius = it
                                },
                                valueRange = 1f..50f,
                                onValueChangeFinished = {},
                                enabled = selectedSortBy == sortByOptions[0],
                                colors = SliderDefaults.colors(inactiveTrackColor = Color.LightGray)
                            )
                        }
                    }
                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MyButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            buttonColors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.background,
                                contentColor = MaterialTheme.colors.primary,
                            ),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                            border = BorderStroke(
                                1.dp,
                                if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
                            ),
                            onClick = onNegativeClick,
                            label = stringResource(id = R.string.cancel_text)
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        MyButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                                onPositiveClick(
                                    //The interest filters should match the Google Places API parameters
                                    PointsOfInterestFilters(
                                        openNow = selectedHour == hoursOptions[1],
                                        rankBy = if (selectedSortBy == sortByOptions[0]) "prominence" else "distance",
                                        radius = selectedRadius.toInt()
                                    )

                                )
                            },
                            label = stringResource(id = R.string.apply_text)
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun FiltersCustomDialogPreview() {
    TravelAssistantTheme {
        FiltersCustomDialog(onDismiss = {

        }, onNegativeClick = {


        }, onPositiveClick = { color ->

        }, filters = PointsOfInterestFilters())
    }
}

@Composable
fun MySavedInterestCard(onNavigateToMap: () -> Unit, pointOfInterest: PointOfInterestResultSimplifiedRoom) {
    val interestsViewModel: InterestsViewModel = viewModel()
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val mapViewModel: MapViewModel = viewModel(LocalContext.current as ComponentActivity)
    val mContext = LocalContext.current

    val delete = SwipeAction(icon = rememberVectorPainter(Icons.Default.Delete),
        background = Red500,
        onSwipe = {
            interestsViewModel.deleteInterestFromFirebaseDB(pointOfInterest)
        })

    val navigationType = travelAssistantViewModel.getNavigationType().collectAsState("")

    val currentLocation = interestsViewModel.getLocationLiveData().observeAsState()
    val currentLat = currentLocation.value?.latitude?.toDouble()
    val currentLon = currentLocation.value?.longitude?.toDouble()

    val distanceInMeters = if (currentLat != null && currentLon != null) {
        getDistanceBetweenCoordinates(
            startLatitude = currentLat,
            startLongitude = currentLon,
            endLatitude = pointOfInterest.lat.toDouble(),
            endLongitude = pointOfInterest.lng.toDouble()
        ).toInt()
    } else 0
    val imageURL = if (pointOfInterest.photoURL != null) {
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400" + "&photo_reference=${pointOfInterest.photoURL}" + "&key=${Constants.GOOGLE_API_KEY}"
    } else {
        "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="
    }

    SwipeableActionsBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(bottom = 20.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .shadow(30.dp), endActions = listOf(delete), swipeThreshold = 100.dp
    ) {
        Surface(color = MaterialTheme.colors.background.copy(if (MaterialTheme.colors.isLight) 0.95f else 0.85f)) {
            Row {
                AsyncImage(
                    model = imageURL,
                    contentDescription = "Image of the place",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(150.dp)
                        .fillMaxHeight(1f)
                        .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)),
                )

                Column(modifier = Modifier.padding(5.dp)) {
                    Row(
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    ) {
                        Text(
                            text = pointOfInterest.name,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = if (MaterialTheme.colors.isLight) Color.DarkGray else Color.LightGray,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Start
                            )
                        )
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            tint = Green700,
                            contentDescription = null
                        )
                        Text(
                            text = if (distanceInMeters < 1000) "$distanceInMeters m" else "${distanceInMeters / 1000} Km",
                            color = Green700
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            RatingBar(
                                rating = pointOfInterest.rating, starsColor = Color(0XFFE3A008)
                            )

                            if (pointOfInterest.opened_now != null) {
                                Card(
                                    modifier = Modifier.padding(top = 10.dp),
                                    backgroundColor = if (pointOfInterest.opened_now) Color(
                                        0XFFBCF0DA
                                    ) else Color(
                                        0XFFFEE2E2
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (pointOfInterest.opened_now) "Opened" else "Closed",
                                        color = if (pointOfInterest.opened_now) Color(0XFF014737) else Color(
                                            0XFF991B1B
                                        ),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            } else {
                                Card(
                                    modifier = Modifier.padding(top = 10.dp),
                                    backgroundColor = Color(0XFFe0f2fe),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = pointOfInterest.business_status.lowercase()
                                            .replaceFirstChar {
                                                it.titlecase()
                                            },
                                        color = Color(0xFF075985),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }

                        }

                        MyIconButton(
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            buttonColors = ButtonDefaults.buttonColors(
                                backgroundColor = Green500, contentColor = Color.White
                            ),
                            icon = Icons.Outlined.NearMe,
                            iconColor = Color.White,
                            onClick = {
                                val routeOptions = DirectionRequestBody(
                                    destination = Destination(
                                        Location(
                                            LatLngDirections(
                                                pointOfInterest.lat.toDouble(), pointOfInterest.lng.toDouble()
                                            )
                                        )
                                    ),
                                    routeModifiers = RouteModifiers(avoidHighways = true, avoidTolls = true),
                                )

                                startNavigation(
                                    context = mContext,
                                    mapViewModel = mapViewModel,
                                    routeOptions = routeOptions,
                                    navigationType = navigationType.value,
                                    onNavigateToMap = onNavigateToMap
                                )
                            },
                            label = stringResource(id = R.string.go_text)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MySavedInterestCardPreview() {
    val poi = PointOfInterestResultSimplifiedRoom(
        place_id = "ChIJ-xp5uBxlJA0RvRa9y7VUVkY",
        business_status = "OPERATIONAL",
        lat = "41.1477309",
        lng = "-8.6203258",
        name = "Decomur",
        opened_now = true,
        photoURL = "ARywPAL0zma_sNEJvJdonZw5gChdd6yG59Y0cjZMO4NfdiHWZMR8CDr_peGL47uX1SCiYW0YEhPfc4HdJRVgZz4SCIHZy8DDOCKP4j0oH3QE7GeoPZzaq4J77Wd7Es-X1tClAT5QvPLUzHjLrF0dvu9ojBRzzMwA7CXhP4q7_xjTopHnIV-X",
        rating = 4.2,
        user_ratings_total = 5,
        user_email = "bruno010602@gmail.com"
    )
    TravelAssistantTheme {
        MySavedInterestCard({}, poi)
    }
}