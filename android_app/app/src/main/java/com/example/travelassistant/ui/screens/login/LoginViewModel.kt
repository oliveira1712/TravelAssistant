package com.example.travelassistant.ui.screens.login

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(application: Application): AndroidViewModel(application) {
    val authState : MutableLiveData<AuthStatus>
    val fAuth : FirebaseAuth
    val mContext: Context

    init {
        mContext = application.baseContext
        authState = MutableLiveData(AuthStatus.NOLOGGIN)
        fAuth = Firebase.auth
    }

    fun login(email:String, password:String){
        viewModelScope.launch {
            try{
                val result = fAuth.signInWithEmailAndPassword(email, password).await()
                if (result != null && result.user != null){
                    authState.postValue(AuthStatus.LOGGED)
                    return@launch
                } else {
                    authState.postValue(AuthStatus.INCORRECT)
                }
                Log.d("Login","anonymous")
                authState.postValue(AuthStatus.NOLOGGIN)
                return@launch
            } catch( e:Exception) {}
        }
    }

    enum class AuthStatus {
        LOGGED, NOLOGGIN, INCORRECT
    }
}