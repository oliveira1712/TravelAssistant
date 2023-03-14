package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travelassistant.models.gasStations.GasStationResult

@Dao
interface GasStationsDao {
    @Query("SELECT * FROM gasStations")
    fun getGasStations(): LiveData<List<GasStationResult>>

    @Query(
        "SELECT * FROM gasStations " +
                "WHERE distrito = :distrito " +
                "AND municipio = :municipio " +
                "AND combustivel = :fuelType " +
                "ORDER BY nome"
    )
    fun getGasStationsByDistrictMunicipioAndFuelType(
        distrito: String,
        municipio: String,
        fuelType: String
    ): LiveData<List<GasStationResult>>

    @Query(
        "SELECT * FROM gasStations " +
                "WHERE distrito = :distrito " +
                "AND municipio = :municipio "
    )
    fun getGasStationsByDistrictAndMunicipio(
        distrito: String,
        municipio: String,
    ): LiveData<List<GasStationResult>>

    @Query("SELECT * FROM gasStations WHERE Id = :stationId")
    fun getGasStationById(stationId: String): LiveData<GasStationResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasStation(gasStation: GasStationResult)
}