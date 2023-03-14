package com.example.travelassistant

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule

private const val WAIT_UNTIL_TIMEOUT = 7_000L

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT
) {
    waitUntil(timeoutMillis) {
        onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT
) = waitUntilNodeCount(matcher, 1, timeoutMillis)

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT
) = waitUntilNodeCount(matcher, 0, timeoutMillis)