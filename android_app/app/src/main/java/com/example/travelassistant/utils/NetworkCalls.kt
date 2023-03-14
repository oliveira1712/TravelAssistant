package com.example.travelassistant.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

fun <T> makeCall(
    viewModelScope: CoroutineScope,
    setValue: (ResourceNetwork<T>) -> Unit,
    request: suspend () -> Response<T>,
) {

    viewModelScope.launch(Dispatchers.IO) {
        setValue(ResourceNetwork.Loading())

        try {
            val response = request()
            if (response.isSuccessful) {
                println(response.body()!!)
                setValue(ResourceNetwork.Success(response.body()!!))
            } else {
                setValue(ResourceNetwork.Error(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            setValue(ResourceNetwork.Error(e.message.toString()))
        }
    }

}