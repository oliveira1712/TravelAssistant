package com.example.travelassistant.ui.screens.interests

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.database.TravelAssistantDatabase
import com.example.travelassistant.models.Vehicle
import com.example.travelassistant.models.googleplaces.placesnearby.PlaceType
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResponse
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResult
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResultSimplifiedRoom
import com.example.travelassistant.network.api.GooglePlacesAPI
import com.example.travelassistant.network.api.RetrofitHelper
import com.example.travelassistant.repository.PointsOfInterestRepository
import com.example.travelassistant.sensors.LocationSensor
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.ResourceNetwork
import com.example.travelassistant.utils.makeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InterestsViewModel(application: Application) : AndroidViewModel(application) {
    private val restAPI: GooglePlacesAPI =
        RetrofitHelper.getInstance(Constants.BASE_URL_GOOGLE_PLACES_API)
            .create(GooglePlacesAPI::class.java)
    private val _pointsOfInterestAPI = MutableLiveData<ResourceNetwork<PointOfInterestResponse>>()
    val pointsOfInterestAPI: LiveData<ResourceNetwork<PointOfInterestResponse>> =
        _pointsOfInterestAPI
    //So that location only updates after one hour, in this screen the location shouldn't be
    //always updating
    private val durationTime: Long = 60 * 60
    private val locationLiveData = LocationSensor(application, durationTime)
    fun getLocationLiveData() = locationLiveData

    val repository: PointsOfInterestRepository
    val firebaseDB : FirebaseFirestore
    val collectionName : String
    val allSavedInterests: LiveData<List<PointOfInterestResultSimplifiedRoom>>
    val interestsList : MutableLiveData<List<PointOfInterestResultSimplifiedRoom>>
    val fAuth : FirebaseAuth

    init{
        firebaseDB = Firebase.firestore
        collectionName = "Interests"
        val roomDB = TravelAssistantDatabase.getDatabase(application)

        repository = PointsOfInterestRepository(
        restAPI = restAPI,
        savedInterestsDao = roomDB.getSavedInterestsDao(),
        )
        interestsList = MutableLiveData(listOf())
        fAuth = Firebase.auth
        allSavedInterests = fAuth.currentUser?.email?.let { repository.getSavedInterestsByUser(it) }!!
        fetchDataOnce()
        getInterestsLive()
    }

    val placeTypes = listOf(
        PlaceType.Restaurants,
        PlaceType.SuperMarkets,
        PlaceType.Gyms,
        PlaceType.ArtGalleries,
        PlaceType.Parking,
    )

    fun getPlacesFromAPI(
        location: String,
        radius: String = "25000",
        type: String,
        openNow: Boolean = false,
        rankBy: String = "prominence",
    ) {
        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _pointsOfInterestAPI.postValue(value)
        }, request = {
            repository.getPlacesFromAPI(
                location = location,
                radius = radius,
                type = type,
                openNow = openNow,
                rankBy = rankBy,
                key = Constants.GOOGLE_API_KEY,
            )
        })
    }

    //-------------------- ROOM OPERATIONS ----------------------------
    fun insertInterestIntoRoomDB(interest: PointOfInterestResultSimplifiedRoom){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertInterest(interest)
        }
    }

    fun removeInterestFromDB(interest: PointOfInterestResultSimplifiedRoom) {
        viewModelScope.launch(Dispatchers.IO){
            repository.removeInterest(interest)
        }
    }

    //-------------------- FIREBASE OPERATIONS ----------------------------
    fun insertInterestIntoFirebaseDB(interest: PointOfInterestResult){
        viewModelScope.launch(Dispatchers.IO){
            val ref = firebaseDB.collection(collectionName).whereEqualTo("place_id", interest.place_id)
            ref.get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isEmpty()) {
                    val interestToSave = hashMapOf(
                        "place_id" to interest.place_id,
                        "business_status" to interest.business_status,
                        "lat" to interest.geometry.location.lat.toString(),
                        "lng" to interest.geometry.location.lng.toString(),
                        "name" to interest.name,
                        "opened_now" to if(interest.opening_hours != null) interest.opening_hours.open_now else null,
                        "photoURL" to if(interest.photos != null) interest.photos[0].photo_reference else null,
                        "rating" to interest.rating,
                        "user_ratings_total" to interest.user_ratings_total,
                        "user_email" to fAuth.currentUser?.email
                    )
                    firebaseDB.collection(collectionName).add(interestToSave)
                }
            }
        }
    }

    fun deleteInterestFromFirebaseDB(interest: PointOfInterestResultSimplifiedRoom){
        viewModelScope.launch(Dispatchers.IO){
            val ref = firebaseDB.collection(collectionName).whereEqualTo("place_id", interest.place_id)
            ref.get().addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.documents.isEmpty()) {
                    val document = querySnapshot.documents[0]
                    document.reference.delete()
                    println("DELETING FIREBASE " + interest.name)
                    removeInterestFromDB(interest)
                }
            }
        }
    }

    fun fetchDataOnce() {
        val ref = firebaseDB.collection(collectionName).whereEqualTo("userEmail", fAuth.currentUser?.email)
        val task = ref.get()
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = mutableListOf<PointOfInterestResultSimplifiedRoom>()

                for (document in task.result!!) {
                    list.add(
                        PointOfInterestResultSimplifiedRoom(
                            document.get("place_id") as String,
                            document.get("business_status") as String,
                            document.get("lat") as String,
                            document.get("lng") as String,
                            document.get("name") as String,
                            document.get("opened_now") as Boolean,
                            document.get("photoURL") as String,
                            document.get("rating") as Double,
                            document.get("user_ratings_total") as Int,
                            document.get("user_email") as String,
                        )
                    )
                }
                interestsList.postValue(list)
                sincronizarDados()
            }
        }
    }

    fun getInterestsLive(){
        viewModelScope.launch {
            val ref = firebaseDB.collection(collectionName)
            ref.addSnapshotListener{ snapshot, e ->
                if (snapshot!=null && snapshot.metadata.hasPendingWrites()) {
                    val list = mutableListOf<PointOfInterestResultSimplifiedRoom>()

                    for(document in snapshot.documents){
                        val pointToAdd = PointOfInterestResultSimplifiedRoom(
                            document.get("place_id") as String,
                            document.get("business_status") as String,
                            document.get("lat") as String,
                            document.get("lng") as String,
                            document.get("name") as String,
                            document.get("opened_now") as Boolean,
                            document.get("photoURL") as String,
                            document.get("rating") as Double,
                            //document.get("user_ratings_total") as Long,
                            5,
                            document.get("user_email") as String,
                        )

                        if (!list.contains(pointToAdd)) {
                            list.add(pointToAdd)
                        }
                    }

                    interestsList.postValue(list)
                    sincronizarDados()
                }
            }
        }
    }

    fun sincronizarDados(){
        viewModelScope.launch(Dispatchers.IO){
            interestsList.value?.forEach{
                println("INSERTIG LOCALLY " + it.name)
                insertInterestIntoRoomDB(it)
            }
        }
    }
}
