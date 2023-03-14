package com.example.travelassistant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.interests.InterestsScreen
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.utils.TestTags
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeInterestCardTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.travelassistant", appContext.packageName)
    }

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun startNavigationTest() {
        rule.apply {
            setContent {
//                val interestsViewModel: InterestsViewModel = viewModel()
                TravelAssistantTheme {
                    val navController = rememberNavController()
                    var appBarState by remember {
                        mutableStateOf(AppBarState())
                    }

                    InterestsScreen(onNavigateToMap = { navController.navigate("map") { popUpTo(0) } }) {
                        appBarState = it
                    }
                }
//                val interests = interestsViewModel.pointsOfInterestAPI.observeAsState(null)
//                val interestsData = interests.value?.data?.results
//                waitUntil(10_000) { interestsData != null }
            }

            // Waiting for the cards to show
            waitUntilExists(hasTestTag(TestTags.POI_CARD))

            checkIfCardDetailsExist()
            onRoot().performTouchInput { swipeRight() }
            checkIfCardDetailsExist()
        }
    }

    @Test
    fun checkIfCardDetailsExist() {
        rule.apply {
            onNodeWithTag(TestTags.POI_CARD).assertExists()
            onNodeWithTag(TestTags.POI_CARD_GO_BUTTON).assertExists()
            onNodeWithTag(TestTags.POI_CARD_TEXT).assertExists()
        }
    }
}

