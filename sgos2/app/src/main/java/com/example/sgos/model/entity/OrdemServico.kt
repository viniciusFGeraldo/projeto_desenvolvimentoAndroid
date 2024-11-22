package com.example.sgos.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo
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
    val status: Status,

    @ColumnInfo(name = "clienteId")
    val clienteId: Int,

    @ColumnInfo(name = "funcionarioId")
    val funcionarioId: Int,

    @ColumnInfo(name = "produtoId")
    val produtoId: Int
)

enum class Status {
    EM_PRODUCAO,
    EM_ACABAMENTO,
    PRONTO_PARA_ENTREGA,
    SOLICITADO_BAIXA,
    BAIXADA;

    fun nextStatus(): Status {
        return when (this) {
            EM_PRODUCAO -> EM_ACABAMENTO
            EM_ACABAMENTO -> PRONTO_PARA_ENTREGA
            PRONTO_PARA_ENTREGA -> SOLICITADO_BAIXA
            SOLICITADO_BAIXA -> BAIXADA
            BAIXADA -> this // Ou lançar uma exceção
        }
    }
}