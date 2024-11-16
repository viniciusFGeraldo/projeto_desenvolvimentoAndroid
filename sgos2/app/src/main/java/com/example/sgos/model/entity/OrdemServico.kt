package com.example.sgos.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import java.util.Date

@Entity(
    tableName = "ordemServico",
    foreignKeys = [
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Funcionario::class,
            parentColumns = ["id"],
            childColumns = ["funcionarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Produto::class,
            parentColumns = ["id"],
            childColumns = ["produtoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrdemServico(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val largura: Float,
    val altura: Float,
    val valorM2: Float,
    val quantidade: Int,
    val valorUnitario: Float,
    val valorTotal: Float,
    val valorDesconto: Float,
    val valorAPagar: Float,
    val observacoes: String,
    val dataHorarioAbertura: Date = Date(),
    @TypeConverters(StatusConverter::class)
    val status: Status,

    @ColumnInfo(name = "clienteId")
    val clienteId: Int,

    @ColumnInfo(name = "funcionarioId")
    val funcionarioId: Int,

    @ColumnInfo(name = "produtoId")
    val produtoId: Int
)

enum class Status(val statusCode: Int) {
    EM_PRODUCAO(0),
    EM_ACABAMENTO(1),
    PRONTO_PARA_ENTREGA(2),
    SOLICITADO_BAIXA(3),
    BAIXADA(4)
}

// TypeConverter para Status (caso o Room não lide com enum diretamente)
class StatusConverter {
    @androidx.room.TypeConverter
    fun fromStatus(status: Status): Int {
        return status.statusCode
    }

    @androidx.room.TypeConverter
    fun toStatus(statusCode: Int): Status {
        return Status.values().first { it.statusCode == statusCode }
    }
}

