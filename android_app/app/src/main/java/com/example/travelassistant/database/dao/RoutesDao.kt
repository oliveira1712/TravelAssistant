package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelassistant.models.Districts.DistrictResult
import com.example.travelassistant.models.Route

@Dao
interface RoutesDao {
    @Query("SELECT * FROM routes")
    fun getRoutes(): LiveData<List<Route>>

    @Query("SELECT * FROM routes WHERE userEmail = :userEmail")
    fun getRoutesByUserEmail(userEmail: String): LiveData<List<Route>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: Route)

    @Update
    suspend fun updateRoute(route: Route)

    @Delete
    suspend fun deleteRoute(route: Route)
}