package com.example.sgos.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "cliente")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dataCadastro: Date = Date(),
    val nome: String,
    val cpf: String,
    val rg: String,
    val cep: String,
    val endereco: String,
    val bairro: String,
    val cidade: String,
    val telefone: String
){

    class DateConverter {

        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }
    }
}
