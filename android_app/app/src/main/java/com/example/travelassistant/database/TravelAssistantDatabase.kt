package com.example.travelassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travelassistant.database.dao.*
import com.example.travelassistant.models.Districts.DistrictResult
import com.example.travelassistant.models.Municipios.MunicipiosResult
import com.example.travelassistant.models.Route
import com.example.travelassistant.models.Vehicle
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResult
import com.example.travelassistant.models.gasStations.GasStationResult
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResultSimplifiedRoom

@Database(
    entities = [Vehicle::class, DistrictResult::class, MunicipiosResult::class, FuelTypeResult::class, GasStationResult::class, Route::class, PointOfInterestResultSimplifiedRoom::class],
    version = 28,
    exportSchema = false
)
abstract class TravelAssistantDatabase : RoomDatabase() {
    abstract fun getVehiclesDao(): VehiclesDao
    abstract fun getDistrictsDao(): DistrictsDao
    abstract fun getMunicipiosDao(): MunicipiosDao
    abstract fun getFuelTypesDao(): FuelTypeDao
    abstract fun getGasStationDao(): GasStationsDao
    abstract fun getRoutesDao(): RoutesDao
    abstract fun getSavedInterestsDao(): SavedInterestsDao

    companion object {
        @Volatile
        private var INSTANCE: TravelAssistantDatabase? = null

        fun getDatabase(context: Context): TravelAssistantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelAssistantDatabase::class.java,
                    "travelAssistance_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
