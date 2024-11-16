package com.example.sgos.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "produto",
    foreignKeys = [
        ForeignKey(
            entity = Acabamento::class,
            parentColumns = ["id"],
            childColumns = ["acabamentoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Equipamento::class,
            parentColumns = ["id"],
            childColumns = ["equipamentoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nome: String,
    val descricao: String,
    val largura: Float,
    val altura: Float,
    val valorM2: Float,
    val quantidade: Int,
    val valorUnitario: Float,
    val valorSubTotal: Float,
    val criadoEm: Date = Date(),

    @ColumnInfo(name = "acabamentoId")
    val acabamentoId: Int,

    @ColumnInfo(name = "equipamentoId")
    val equipamentoId: Int
)