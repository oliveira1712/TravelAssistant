package com.example.travelassistant.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.database.dao.VehiclesDao
import com.example.travelassistant.models.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VehiclesRepository(val vehiclesDao: VehiclesDao) {
    fun getVehicles(): LiveData<List<Vehicle>>{
        return vehiclesDao.getVehicles()
    }

    fun getUserVehicles(userEmail: String): LiveData<List<Vehicle>>{
        return vehiclesDao.getUserVehicle(userEmail)
    }

    fun getVehicle(licensePlate: String): LiveData<Vehicle>{
        return vehiclesDao.getVehicle(licensePlate)
    }

    suspend fun insertVehicleRoom(vehicle: Vehicle){
        vehiclesDao.insertVehicle(vehicle)
    }

    suspend fun updateVehicle(vehicle: Vehicle){
        vehiclesDao.updateVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle: Vehicle){
        vehiclesDao.deleteVehicle(vehicle)
    }
}