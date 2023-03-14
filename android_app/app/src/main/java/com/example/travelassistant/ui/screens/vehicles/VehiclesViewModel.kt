package com.example.travelassistant.ui.screens.vehicles

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.database.TravelAssistantDatabase
import com.example.travelassistant.models.Vehicle
import com.example.travelassistant.repository.VehiclesRepository
import com.example.travelassistant.ui.shared.components.uploadImageToFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VehiclesViewModel(application: Application) : AndroidViewModel(application) {
    val repository: VehiclesRepository
    val allVehicles: LiveData<List<Vehicle>>
    val firebaseDB: FirebaseFirestore
    val collectionName: String
    val vehiclesList: MutableLiveData<List<Vehicle>>
    val fAuth : FirebaseAuth

    init {
        firebaseDB = Firebase.firestore
        collectionName = "Vehicles"
        val roomDB = TravelAssistantDatabase.getDatabase(application)
        repository = VehiclesRepository(roomDB.getVehiclesDao())
        vehiclesList = MutableLiveData(listOf())
        fAuth = Firebase.auth
        allVehicles = fAuth.currentUser?.email?.let { repository.getUserVehicles(it) }!!
        fetchDataOnce()
        getVehiclesLive()
    }

    //------------ ROOM OPERATIONS -------------------------
    fun getUserVehicle(userEmail: String): LiveData<List<Vehicle>> {
        return repository.getUserVehicles(userEmail)
    }

    fun getVehicles(): LiveData<List<Vehicle>> {
        return repository.getVehicles()
    }

    fun getVehicle(licensePlate: String): LiveData<Vehicle> {
        return repository.getVehicle(licensePlate)
    }

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVehicle(vehicle)
        }
    }

    fun deleVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVehicle(vehicle)
        }
    }

    //------------------ FIREBASE OPERATIONS ---------------
    fun getVehiclesLive() {
        viewModelScope.launch {
            val ref = firebaseDB.collection(collectionName).whereEqualTo("userEmail", fAuth.currentUser?.email)
            ref.addSnapshotListener { snapshot, e ->
                if (snapshot != null  && snapshot.metadata.hasPendingWrites()) {
                    val list = mutableListOf<Vehicle>()

                    for (document in snapshot.documents) {
                        list.add(
                            Vehicle(
                                document.get("licencePlate") as String,
                                document.get("userEmail") as String,
                                document.get("brand") as String,
                                document.get("model") as String,
                                document.get("name") as String,
                                document.get("kms") as Double,
                                document.get("imageUrl") as String,
                            )
                        )
                    }
                    vehiclesList.postValue(list)
                    sincronizarDados()
                }
            }
        }
    }

    fun fetchDataOnce() {
        val ref = firebaseDB.collection(collectionName).whereEqualTo("userEmail", fAuth.currentUser?.email)
        val task = ref.get()
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = mutableListOf<Vehicle>()
                for (document in task.result!!) {
                    list.add(
                        Vehicle(
                            document.get("licencePlate") as String,
                            document.get("userEmail") as String,
                            document.get("brand") as String,
                            document.get("model") as String,
                            document.get("name") as String,
                            document.get("kms") as Double,
                            document.get("imageUrl") as String,
                        )
                    )
                }
                vehiclesList.postValue(list)
                sincronizarDados()
            }
        }
    }

    fun insertVehicle(vehicle: Vehicle, imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            var url2 = imageUri?.let { uploadImageToFirebase(it) }
            if (url2 != null) {
                Log.d("URL", url2)
            }
            val vehicleToSave = hashMapOf(
                "licencePlate" to vehicle.licencePlate,
                "userEmail" to vehicle.userEmail,
                "brand" to vehicle.brand,
                "model" to vehicle.model,
                "name" to vehicle.name,
                "kms" to vehicle.kms,
                "imageUrl" to url2
            )
            firebaseDB.collection(collectionName).add(vehicleToSave)
        }
    }

    fun deleteVehicleFromFirebaseDB(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO) {
            val ref = firebaseDB.collection(collectionName)
                .whereEqualTo("licencePlate", vehicle.licencePlate)
            ref.get().addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.documents.isEmpty()) {
                    val document = querySnapshot.documents[0]
                    document.reference.delete()
                }
                deleVehicle(vehicle)
            }
        }
    }

    fun sincronizarDados() {
        viewModelScope.launch(Dispatchers.IO) {
            vehiclesList.value?.forEach {
                repository.insertVehicleRoom(it)
            }
        }
    }
}