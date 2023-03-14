package com.example.travelassistant.models.gasStations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "gasStations", primaryKeys = ["id", "combustivel"])
data class GasStationResult(
    @ColumnInfo(name = "id")
    val Id: String,

    @ColumnInfo(name = "nome")
    val Nome: String,

    @ColumnInfo(name = "tipoPosto")
    val TipoPosto: String,

    @ColumnInfo(name = "municipio")
    val Municipio: String,

    @ColumnInfo(name = "preco")
    val Preco: String,

    @ColumnInfo(name = "marca")
    val Marca: String,

    @ColumnInfo(name = "combustivel")
    val Combustivel: String,

    @ColumnInfo(name = "distrito")
    val Distrito: String,

    @ColumnInfo(name = "morada")
    val Morada: String,

    @ColumnInfo(name = "localidade")
    val Localidade: String,

    @ColumnInfo(name = "latitude")
    val Latitude: Double,

    @ColumnInfo(name = "longitude")
    val Longitude: Double,

    @ColumnInfo(name = "lastUpdate")
    val lastUpdate: String
)