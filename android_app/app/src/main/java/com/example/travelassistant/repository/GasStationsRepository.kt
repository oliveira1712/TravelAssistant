package com.example.travelassistant.repository

import androidx.lifecycle.LiveData
import com.example.travelassistant.database.dao.DistrictsDao
import com.example.travelassistant.database.dao.FuelTypeDao
import com.example.travelassistant.database.dao.GasStationsDao
import com.example.travelassistant.database.dao.MunicipiosDao
import com.example.travelassistant.models.Districts.DistrictResponse
import com.example.travelassistant.models.Districts.DistrictResult
import com.example.travelassistant.models.Municipios.MunicipiosResponse
import com.example.travelassistant.models.Municipios.MunicipiosResult
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResponse
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResult
import com.example.travelassistant.models.gasStations.GasStationResponse
import com.example.travelassistant.models.gasStations.GasStationResult
import com.example.travelassistant.network.api.GasStationAPI
import retrofit2.Response

class GasStationsRepository(
    private val restAPI: GasStationAPI,
    val districtsDao: DistrictsDao,
    val municipiosDao: MunicipiosDao,
    val fuelTypeDao: FuelTypeDao,
    val gasStationsDao: GasStationsDao
) {
    suspend fun getGasStations(
        idsTiposComb: String,
        idDistrito: String,
        idsMunicipios: String,
    ): Response<GasStationResponse> {
        return restAPI.getGasStations(
            idsTiposComb = idsTiposComb,
            idDistrito = idDistrito,
            idsMunicipios = idsMunicipios,
            qtdPorPagina = "13600"
        )
    }

    suspend fun getDistrictsFromAPI(): Response<DistrictResponse> {
        return restAPI.getDistricts()
    }

    suspend fun getMunicipiosFromAPI(): Response<MunicipiosResponse> {
        return restAPI.getMunicipios()
    }

    suspend fun getFuelTypesFromAPI(): Response<FuelTypeResponse> {
        return restAPI.getFuelTypes()
    }

    //------------------------------- GAS STATIONS ---------------------------

    fun getGasStations(): LiveData<List<GasStationResult>> {
        return gasStationsDao.getGasStations()
    }

    fun getGasStationById(stationId: String): LiveData<GasStationResult> {
        return gasStationsDao.getGasStationById(stationId = stationId)
    }

    fun getGasStationsByDistrictMunicipioAndFuelType(
        distrito: String,
        municipio: String,
        fuelType: String
    ): LiveData<List<GasStationResult>> {
        return gasStationsDao.getGasStationsByDistrictMunicipioAndFuelType(
            distrito = distrito,
            municipio = municipio,
            fuelType = fuelType
        )
    }

    fun getGasStationsByDistrictAndMunicipio(
        distrito: String,
        municipio: String,
    ): LiveData<List<GasStationResult>> {
        return gasStationsDao.getGasStationsByDistrictAndMunicipio(
            distrito = distrito,
            municipio = municipio,
        )
    }

    suspend fun insertGasStation(gasStation: GasStationResult) {
        gasStationsDao.insertGasStation(gasStation = gasStation)
    }

    //------------------------------- DISTRICTS ---------------------------

    fun getDistrictsFromDB(): LiveData<List<DistrictResult>> {
        return districtsDao.getDistricts()
    }

    fun getDistrictByIdFromDB(districtId: String): LiveData<DistrictResult> {
        return districtsDao.getDistrictById(districtId)
    }

    fun getDistrictByDescritivoFromDB(descritivo: String): LiveData<DistrictResult> {
        return districtsDao.getDistrictByDescritivo(descritivo)
    }

    suspend fun insertDistrictFromDB(district: DistrictResult) {
        districtsDao.insertDistrict(district)
    }

    suspend fun updateDistrictFromDB(district: DistrictResult) {
        districtsDao.updateDistrict(district)
    }

    suspend fun deleteDistrictFromDB(district: DistrictResult) {
        districtsDao.deleteDistrict(district)
    }

    //------------------------------------ MUNICIPIOS --------------------------

    fun getMunicipiosFromDB(): LiveData<List<MunicipiosResult>> {
        return municipiosDao.getMunicipios()
    }

    fun getMunicipioByIdFromDB(municipioId: String): LiveData<MunicipiosResult> {
        return municipiosDao.getMunicipioById(municipioId = municipioId)
    }

    fun getMunicipioByDescritivoFromDB(descritivo: String): LiveData<MunicipiosResult> {
        println("Descritivo: " + descritivo)
        return municipiosDao.getMunicipioByDescritivo(descritivo)
    }

    fun getMunicipiosByDistrictFromDB(districtId: String): LiveData<List<MunicipiosResult>> {
        println(municipiosDao.getMuncipiosByDistrict(districtId = districtId).value)
        return municipiosDao.getMuncipiosByDistrict(districtId = districtId)
    }

    suspend fun getMuncipiosByDistrictV2(districtId: String): List<MunicipiosResult> {
        return municipiosDao.getMuncipiosByDistrictWithoutLiveData(districtId = districtId)
    }

    suspend fun insertMunicipioFromDB(municipio: MunicipiosResult) {
        municipiosDao.insertMunicipio(municipio = municipio)
    }

    suspend fun updateMunicipioFromDB(municipio: MunicipiosResult) {
        municipiosDao.updateMunicipio(municipio = municipio)
    }

    suspend fun deleteMuncipioFromDB(municipio: MunicipiosResult) {
        municipiosDao.deleteMuncipio(municipio = municipio)
    }

    //------------------------------------ Fuel Types --------------------------

    fun getFuelTypes(): LiveData<List<FuelTypeResult>> {
        return fuelTypeDao.getFuelTypes()
    }

    fun getFuelTypeById(fuelTypeId: String): LiveData<FuelTypeResult> {
        return fuelTypeDao.getFuelTypeById(fuelTypeId)
    }

    fun getFuelTypeByDescritivo(descritivo: String): LiveData<FuelTypeResult> {
        return fuelTypeDao.getFuelTypeByDescritivo(descritivo)
    }

    suspend fun insertFuelType(fuelType: FuelTypeResult) {
        fuelTypeDao.insertFuelType(fuelType)
    }

    suspend fun updateFuelType(fuelType: FuelTypeResult) {
        fuelTypeDao.updateFuelType(fuelType)
    }

    suspend fun deleteFuelType(fuelType: FuelTypeResult) {
        fuelTypeDao.deleteFuelType(fuelType)
    }
}