package com.example.travelassistant.ui.screens.routes

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.database.TravelAssistantDatabase
import com.example.travelassistant.models.Route
import com.example.travelassistant.repository.RoutesRepository
import com.example.travelassistant.ui.shared.components.getLatLongFromAddress
import com.example.travelassistant.utils.getDistanceBetweenCoordinates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutesViewModel(application: Application) : AndroidViewModel(application) {
    val repository: RoutesRepository
    val allRoutes: LiveData<List<Route>>
    val firebaseDB: FirebaseFirestore
    val collectionName: String
    val routesList: MutableLiveData<List<Route>>
    val fAuth: FirebaseAuth
    val mContext: Context

    init {
        mContext = application.baseContext
        firebaseDB = Firebase.firestore
        collectionName = "Routes"
        val roomDB = TravelAssistantDatabase.getDatabase(application)
        repository = RoutesRepository(roomDB.getRoutesDao())
        routesList = MutableLiveData(listOf())
        fAuth = Firebase.auth
        allRoutes = fAuth.currentUser?.email?.let { repository.getRoutesByUserEmail(it) }!!
        fetchDataOnce()
        getRoutesLive()
    }

    //------------------------- ROOM OPERATIONS ---------------------------
    fun getRoutes(): LiveData<List<Route>> {
        return repository.getRoutes()
    }

    fun getRoutesByUserEmail(userEmail: String): LiveData<List<Route>> {
        return repository.getRoutesByUserEmail(userEmail)
    }

    fun insertRoute(route: Route) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRoute(route)
        }
    }

    fun updateRoute(route: Route) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRoute(route)
        }
    }

    fun deleteRoute(route: Route) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRoute(route)
        }
    }

    //---------------------------------- FIREBASE OPERATIONS -------------------

    fun fetchDataOnce() {
        val ref = firebaseDB.collection(collectionName)
            .whereEqualTo("userEmail", fAuth.currentUser?.email)
        val task = ref.get()
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = mutableListOf<Route>()

                for (document in task.result!!) {
                    list.add(
                        Route(
                            document.get("userEmail") as String,
                            document.get("endPoint") as String,
                            document.get("endPointLat") as Double,
                            document.get("endPointLon") as Double,
                        )
                    )
                }
                routesList.postValue(list)
                sincronizarDados()
            }
        }
    }

    fun getRoutesLive() {
        viewModelScope.launch {
            val ref = firebaseDB.collection(collectionName)
            ref.addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
                    val list = mutableListOf<Route>()
                    for (document in snapshot.documents) {
                        list.add(
                            Route(
                                document.get("userEmail") as String,
                                document.get("endPoint") as String,
                                document.get("endPointLat") as Double,
                                document.get("endPointLon") as Double
                            )
                        )
                    }
                    routesList.postValue(list)
                    sincronizarDados()
                }
            }
        }
    }

    fun insertRouteFirebase(route: Route) {
        viewModelScope.launch(Dispatchers.IO) {
            var valid = true
            val endPoint = getLatLongFromAddress(route.endPoint, context = mContext)

            if (endPoint == null) {
                valid = false
                Toast.makeText(mContext, "StartPoint address is invalid", Toast.LENGTH_SHORT).show()
            }

            if (valid) {
                val routeToSave = hashMapOf(
                    "userEmail" to fAuth.currentUser?.email,
                    "endPoint" to route.endPoint,
                    "endPointLat" to endPoint?.first,
                    "endPointLon" to endPoint?.second
                )
                firebaseDB.collection(collectionName).add(routeToSave)
            }
        }
    }

    fun sincronizarDados() {
        viewModelScope.launch(Dispatchers.IO) {
            routesList.value?.forEach {
                repository.insertRoute(it)
            }
        }
    }
}