package com.example.sgos.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "equipamento")
data class Equipamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val criadoEm: Date = Date(),
    val nome: String,
    val descricao: String,
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
