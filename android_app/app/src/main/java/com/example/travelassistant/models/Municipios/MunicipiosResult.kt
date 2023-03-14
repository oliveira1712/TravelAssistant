package com.example.travelassistant.models.Municipios

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "municipios", primaryKeys = ["Descritivo"])
data class MunicipiosResult (
    @ColumnInfo(name = "Descritivo")
    val Descritivo: String,

    @ColumnInfo(name = "IdDistrito")
    val IdDistrito: Int,

    @ColumnInfo(name = "Id")
    val Id: Int,
)