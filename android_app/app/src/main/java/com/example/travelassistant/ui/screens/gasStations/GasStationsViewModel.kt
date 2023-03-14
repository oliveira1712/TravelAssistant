package com.example.travelassistant.ui.screens.gasStations

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.database.TravelAssistantDatabase
import com.example.travelassistant.models.Districts.DistrictResponse
import com.example.travelassistant.models.Districts.DistrictResult
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResponse
import com.example.travelassistant.models.gasStations.GasStationResponse
import com.example.travelassistant.models.Municipios.MunicipiosResponse
import com.example.travelassistant.models.Municipios.MunicipiosResult
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResult
import com.example.travelassistant.models.gasStations.GasStationResult
import com.example.travelassistant.network.api.GasStationAPI
import com.example.travelassistant.network.api.RetrofitHelper
import com.example.travelassistant.repository.GasStationsRepository
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.ResourceNetwork
import com.example.travelassistant.utils.makeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class GasStationsViewModel(application: Application) : AndroidViewModel(application) {
    private val restAPI: GasStationAPI =
        RetrofitHelper.getInstance(Constants.BASE_URL_GAS_STATIONS_API)
            .create(GasStationAPI::class.java)

    private val repository: GasStationsRepository
    val allDistricts: LiveData<List<DistrictResult>>

    init {
        val db = TravelAssistantDatabase.getDatabase(application)
        repository = GasStationsRepository(
            restAPI = restAPI,
            districtsDao = db.getDistrictsDao(),
            municipiosDao = db.getMunicipiosDao(),
            fuelTypeDao = db.getFuelTypesDao(),
            gasStationsDao = db.getGasStationDao()
        )
        allDistricts = getDistrictsFromDB()
    }

    private val _selectedDistrict = MutableLiveData<DistrictResult>()
    val selectedDistrict: LiveData<DistrictResult> = _selectedDistrict

    private val _selectedMunicipio = MutableLiveData<MunicipiosResult>()
    val selectedMunicipio: LiveData<MunicipiosResult> = _selectedMunicipio

    private val _selectedFuelType = MutableLiveData<FuelTypeResult>()
    val selectedFuelType: LiveData<FuelTypeResult> = _selectedFuelType

    private val _municipiosBySelectedDistrict = MutableLiveData<List<MunicipiosResult>>()
    val municipiosBySelectedDistrict: LiveData<List<MunicipiosResult>> =
        _municipiosBySelectedDistrict

    private val _gasStations = MutableLiveData<ResourceNetwork<GasStationResponse>>()
    val gasStations: LiveData<ResourceNetwork<GasStationResponse>> = _gasStations

    private val gasStationsAux = mutableListOf<List<GasStationResult>>(emptyList())

    private val _districts = MutableLiveData<ResourceNetwork<DistrictResponse>>()
    val districts: LiveData<ResourceNetwork<DistrictResponse>> = _districts

    private val _municipios = MutableLiveData<ResourceNetwork<MunicipiosResponse>>()
    val municipios: LiveData<ResourceNetwork<MunicipiosResponse>> = _municipios

    private val _fuelTypes = MutableLiveData<ResourceNetwork<FuelTypeResponse>>()
    val fuelTypes: LiveData<ResourceNetwork<FuelTypeResponse>> = _fuelTypes

    fun getGasStationsFromAPI(
        idDistrito: String,
        idsMunicipios: String,
        idsTiposComb: String
    ) {
        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _gasStations.postValue(value)
            value.data?.resultado?.forEach() {
                insertGasStationIntoDB(
                    GasStationResult(
                        it.Id,
                        it.Nome,
                        it.TipoPosto,
                        it.Municipio,
                        it.Preco,
                        it.Marca,
                        it.Combustivel,
                        it.Distrito,
                        it.Morada,
                        it.Localidade,
                        it.Latitude,
                        it.Longitude,
                        LocalDate.now().toString()
                    )
                )
            }
        }, request = {
            repository.getGasStations(
                idsTiposComb = idsTiposComb,
                idDistrito = idDistrito,
                idsMunicipios = idsMunicipios
            )
        })
    }

    fun getDistrictsFromAPI() {
        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _districts.postValue(value)
        }, request = {
            repository.getDistrictsFromAPI()
        })
    }

    fun getMunicipiosFromAPI() {
        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _municipios.postValue(value)
        }, request = {
            repository.getMunicipiosFromAPI()
        })
    }

    fun getFuelTypesFromAPI() {
        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _fuelTypes.postValue(value)
        }, request = {
            repository.getFuelTypesFromAPI()
        })
    }

    //----------------------------- Gas Stations ----------------------

    fun getGasStationsFromDB(): LiveData<List<GasStationResult>> {
        return repository.getGasStations()
    }

    fun getGasStationByIdFromDB(stationId: String): LiveData<GasStationResult> {
        return repository.getGasStationById(stationId = stationId)
    }

    fun getGasStationsByDistrictMunicipioAndFuelTypeFromDB(
        distrito: String,
        municipio: String,
        fuelType: String
    ): LiveData<List<GasStationResult>> {
        return repository.getGasStationsByDistrictMunicipioAndFuelType(
            distrito = distrito,
            municipio = municipio,
            fuelType = fuelType
        )
    }

    fun getGasStationsByDistrictAndMunicipioFromDB(
        distrito: String,
        municipio: String
    ): LiveData<List<GasStationResult>> {
        return repository.getGasStationsByDistrictAndMunicipio(
            distrito = distrito,
            municipio = municipio
        )
    }

    fun insertGasStationIntoDB(gasStation: GasStationResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertGasStation(gasStation = gasStation)
        }
    }

    fun checkDataIntegrity(
        idDistrito: String,
        distrito: String,
        idMunicipio: String,
        municipio: String,
        idTipoComb: String,
        tipoComb: String,
        gasStationsFromDB: State<List<GasStationResult>?>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getGasStationsFromAPI(idDistrito, idMunicipio, idTipoComb)
        }
    }

        //----------------------------- Districts ------------------

        fun getDistrictsFromDB(): LiveData<List<DistrictResult>> {
            return repository.getDistrictsFromDB()
        }

        fun getDistrictByIdFromDB(districtId: String): LiveData<DistrictResult> {
            return repository.getDistrictByIdFromDB(districtId)
        }

        fun getDistrictByDescritivoFromDB(descritivo: String): LiveData<DistrictResult> {
            return repository.getDistrictByDescritivoFromDB(descritivo)
        }

        fun insertDistrictIntoDB(district: DistrictResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertDistrictFromDB(district)
            }
        }

        fun updateDistrictFromDB(district: DistrictResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateDistrictFromDB(district)
            }
        }

        fun deleteDistrictFromDB(district: DistrictResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteDistrictFromDB(district)
            }
        }

        suspend fun selectDistrict(district: DistrictResult) {
            _selectedDistrict.value = district
            _municipiosBySelectedDistrict.postValue(selectedDistrict.value?.let {
                repository.getMuncipiosByDistrictV2(
                    it.Id
                )
            })
        }
        //--------------------------- municipios ----------------------------

        fun getMunicipiosFromDB(): LiveData<List<MunicipiosResult>> {
            return repository.getMunicipiosFromDB()
        }

        fun getMunicipioByIdFromDB(municipioId: String): LiveData<MunicipiosResult> {
            return repository.getMunicipioByIdFromDB(municipioId = municipioId)
        }

        fun getMunicipioByDescritivoFromDB(descritivo: String): LiveData<MunicipiosResult> {
            return repository.getMunicipioByDescritivoFromDB(descritivo)
        }

        fun getMunicipiosByDistrictFromDB(districtId: String): LiveData<List<MunicipiosResult>> {
            return repository.getMunicipiosByDistrictFromDB(districtId = districtId)
        }

        fun getMunicipiosByDistrictFromDBV2(districtId: String): List<MunicipiosResult> {
            var list = emptyList<MunicipiosResult>()
            viewModelScope.launch(Dispatchers.IO) {
                list = repository.getMuncipiosByDistrictV2(districtId = districtId)
            }

            return list
        }

        fun insertMunicipioIntoDB(municipio: MunicipiosResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertMunicipioFromDB(municipio = municipio)
            }
        }

        fun updateMunicipioFromDB(municipio: MunicipiosResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateMunicipioFromDB(municipio = municipio)
            }
        }

        fun deleteMuncipioFromDB(municipio: MunicipiosResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteMuncipioFromDB(municipio = municipio)
            }
        }

        fun selectMunicipio(municipio: MunicipiosResult) {
            _selectedMunicipio.value = municipio
        }

        //--------------------------------- Fuel Types ------------------------
        fun getFuelTypesFromDB(): LiveData<List<FuelTypeResult>> {
            return repository.getFuelTypes()
        }

        fun getFuelTypeByIdFromDB(fuelTypeId: String): LiveData<FuelTypeResult> {
            return repository.getFuelTypeById(fuelTypeId)
        }

        fun getFuelTypeByDescritivoFromDB(descritivo: String): LiveData<FuelTypeResult> {
            return repository.getFuelTypeByDescritivo(descritivo)
        }

        fun insertFuelTypeIntoDB(fuelType: FuelTypeResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertFuelType(fuelType)
            }
        }

        fun updateFuelTypeFromDB(fuelType: FuelTypeResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateFuelType(fuelType)
            }
        }

        fun deleteFuelTypeFromDB(fuelType: FuelTypeResult) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteFuelType(fuelType)
            }
        }

        fun selectFuelType(fuelType: FuelTypeResult) {
            _selectedFuelType.value = fuelType
        }
    }
