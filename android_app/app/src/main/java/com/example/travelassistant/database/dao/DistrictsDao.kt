package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelassistant.models.Districts.DistrictResult

@Dao
interface DistrictsDao {
    @Query("SELECT * FROM districts ORDER BY Descritivo")
    fun getDistricts(): LiveData<List<DistrictResult>>

    @Query("SELECT * FROM districts WHERE Id = :districtId")
    fun getDistrictById(districtId: String): LiveData<DistrictResult>

    @Query("SELECT * FROM districts WHERE Descritivo = :descritivo")
    fun getDistrictByDescritivo(descritivo: String): LiveData<DistrictResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistrict(district: DistrictResult)

    @Update
    suspend fun updateDistrict(district: DistrictResult)

    @Delete
    suspend fun deleteDistrict(district: DistrictResult)
}