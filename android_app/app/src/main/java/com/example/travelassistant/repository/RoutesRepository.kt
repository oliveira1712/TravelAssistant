package com.example.travelassistant.repository

import androidx.lifecycle.LiveData
import com.example.travelassistant.database.dao.RoutesDao
import com.example.travelassistant.models.Route

class RoutesRepository(val routesDao: RoutesDao){
    fun getRoutes(): LiveData<List<Route>> {
        return routesDao.getRoutes()
    }

    fun getRoutesByUserEmail(userEmail: String): LiveData<List<Route>> {
        return routesDao.getRoutesByUserEmail(userEmail = userEmail)
    }

    suspend fun insertRoute(route: Route){
        routesDao.insertRoute(route)
    }

    suspend fun updateRoute(route: Route){
        routesDao.updateRoute(route)
    }

    suspend fun deleteRoute(route: Route){
        routesDao.deleteRoute(route)
    }
}