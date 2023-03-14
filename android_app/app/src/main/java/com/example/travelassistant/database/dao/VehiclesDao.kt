package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelassistant.models.Vehicle

@Dao
interface VehiclesDao {
    @Query("SELECT * FROM userVehicles")
    fun getVehicles(): LiveData<List<Vehicle>>

    @Query("SELECT * FROM userVehicles WHERE userEmail = :userEmail")
    fun getUserVehicle(userEmail: String): LiveData<List<Vehicle>>

    @Query("SELECT * FROM userVehicles WHERE licensePlate = :licensePlate")
    fun getVehicle(licensePlate: String): LiveData<Vehicle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: Vehicle)

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)
}