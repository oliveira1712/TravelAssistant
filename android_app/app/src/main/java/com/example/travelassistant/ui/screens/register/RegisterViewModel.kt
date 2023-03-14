package com.example.travelassistant.ui.screens.register

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val firebaseDB: FirebaseFirestore
    val collectionName: String
    val authState: MutableLiveData<AuthStatus>
    val fAuth: FirebaseAuth
    val mContext: Context

    init {
        mContext = application.baseContext
        firebaseDB = Firebase.firestore
        collectionName = "Users"
        authState = MutableLiveData(AuthStatus.NOLOGGIN)
        fAuth = Firebase.auth
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = fAuth.createUserWithEmailAndPassword(email, password).await()
                if (result != null && result.user != null) {
                    insertUserDataIntoFirebase(username, email)
                    authState.postValue(AuthStatus.LOGGED)
                    Log.d("Register", "registed successfuly!")
                    return@launch
                } else{
                    Toast.makeText(mContext, "Error! Something wrong", Toast.LENGTH_SHORT).show()
                }
                Log.d("Register", "anonymous")
                authState.postValue(AuthStatus.NOLOGGIN)
                return@launch
            } catch (e: Exception) {
            }
        }
    }

    fun insertUserDataIntoFirebase(userName: String, userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ref =
                firebaseDB.collection(collectionName).whereEqualTo("userEmail", userEmail)
            ref.get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isEmpty()) {
                    val userToSave = hashMapOf(
                        "userEmail" to userEmail,
                        "userName" to userName,
                    )
                    firebaseDB.collection(collectionName).add(userToSave)
                }
            }
        }
    }

    enum class AuthStatus {
        LOGGED, NOLOGGIN
    }
}