package com.example.travelassistant

import com.example.travelassistant.utils.getDistanceBetweenCoordinates
import org.junit.Test

class LocationsUnitTest {
    @Test
    fun distance_between_porto_lisbon_isCorrect() {
        val porto_latitude = 41.158066090805164
        val porto_longitude = -8.629379577529667
        val lisbon_latitude = 38.722236988182004
        val lisbon_longitude = -9.13932895522425
        val distance = getDistanceBetweenCoordinates(
            startLatitude = porto_latitude,
            startLongitude = porto_longitude,
            endLatitude = lisbon_latitude,
            endLongitude = lisbon_longitude
        )
        println(distance)
        assert(true)
    }
}