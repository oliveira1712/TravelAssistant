package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResult

@Dao
interface FuelTypeDao{
    @Query("SELECT * FROM fuelTypes")
    fun getFuelTypes(): LiveData<List<FuelTypeResult>>

    @Query("SELECT * FROM fuelTypes WHERE Id = :fuelTypeId")
    fun getFuelTypeById(fuelTypeId: String): LiveData<FuelTypeResult>

    @Query("SELECT * FROM fuelTypes WHERE descritivo = :descritivo")
    fun getFuelTypeByDescritivo(descritivo: String): LiveData<FuelTypeResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFuelType(fuelType: FuelTypeResult)

    @Update
    suspend fun updateFuelType(fuelType: FuelTypeResult)

    @Delete
    suspend fun deleteFuelType(fuelType: FuelTypeResult)
}