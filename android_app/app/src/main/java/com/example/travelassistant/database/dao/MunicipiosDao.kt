package com.example.travelassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelassistant.models.Municipios.MunicipiosResult

@Dao
interface MunicipiosDao {
    @Query("SELECT * FROM municipios ORDER BY Descritivo")
    fun getMunicipios(): LiveData<List<MunicipiosResult>>

    @Query("SELECT * FROM municipios WHERE Descritivo = :municipioId")
    fun getMunicipioById(municipioId: String): LiveData<MunicipiosResult>

    @Query("SELECT * FROM municipios WHERE Descritivo = :descritivo")
    fun getMunicipioByDescritivo(descritivo: String): LiveData<MunicipiosResult>

    @Query("SELECT * FROM municipios WHERE IdDistrito = :districtId")
    fun getMuncipiosByDistrict(districtId: String): LiveData<List<MunicipiosResult>>

    @Query("SELECT * FROM municipios WHERE IdDistrito = :districtId")
    suspend fun getMuncipiosByDistrictWithoutLiveData(districtId: String): List<MunicipiosResult>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMunicipio(municipio: MunicipiosResult)

    @Update
    suspend fun updateMunicipio(municipio: MunicipiosResult)

    @Delete
    suspend fun deleteMuncipio(municipio: MunicipiosResult)
}