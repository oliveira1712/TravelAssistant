package com.example.travelassistant.network.api

import com.example.travelassistant.models.Districts.DistrictResponse
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResponse
import com.example.travelassistant.models.gasStations.GasStationResponse
import com.example.travelassistant.models.Municipios.MunicipiosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GasStationAPI {
    @GET("PrecoComb/PesquisarPostos")
    suspend fun getGasStations(
        @Query("idsTiposComb") idsTiposComb: String,
        @Query("idDistrito") idDistrito: String,
        @Query("idsMunicipios") idsMunicipios: String,
        @Query("qtdPorPagina") qtdPorPagina: String
    ): Response<GasStationResponse>

    @GET("PrecoComb/GetDistritos")
    suspend fun getDistricts(
    ): Response<DistrictResponse>

    @GET("PrecoComb/GetMunicipios")
    suspend fun getMunicipios(
    ): Response<MunicipiosResponse>

    @GET("PrecoComb/GetTiposCombustiveis")
    suspend fun getFuelTypes(
    ): Response<FuelTypeResponse>
}