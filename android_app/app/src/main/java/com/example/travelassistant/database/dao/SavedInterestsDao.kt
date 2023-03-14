package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResultSimplifiedRoom

@Dao
interface SavedInterestsDao {
    @Query("SELECT * FROM savedinterests WHERE user_email = :userEmail")
    fun getSavedInterests(userEmail: String): LiveData<List<PointOfInterestResultSimplifiedRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterest(interest: PointOfInterestResultSimplifiedRoom)

    @Delete
    suspend fun removeInterest(interest: PointOfInterestResultSimplifiedRoom)
}