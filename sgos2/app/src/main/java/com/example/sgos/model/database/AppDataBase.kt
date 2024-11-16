package com.example.sgos.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sgos.model.database.dao.AcabamentoDao
import com.example.sgos.model.database.dao.ClienteDao
import com.example.sgos.model.database.dao.EquipamentoDao
import com.example.sgos.model.database.dao.FuncionarioDao
import com.example.sgos.model.database.dao.OrdemServicoDao
import com.example.sgos.model.database.dao.ProdutoDao
import com.example.sgos.model.entity.Acabamento
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.model.entity.Funcionario
import com.example.sgos.model.entity.OrdemServico
import com.example.sgos.model.entity.Produto

@Database(entities = [Acabamento::class, Cliente::class, Equipamento::class, Funcionario::class, OrdemServico::class, Produto::class], version = 1)
@TypeConverters(Acabamento.DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAcabamentoDao() : AcabamentoDao
    abstract fun getClienteDao() : ClienteDao
    abstract fun getEquipamentoDao() : EquipamentoDao
    abstract fun getFuncionarioDao() : FuncionarioDao
    abstract fun getOrdemServicoDao() : OrdemServicoDao
    abstract fun getProdutoDao() : ProdutoDao

    companion object {

        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sgos_db"
                ).build()
                INSTANCE = tempInstance
                tempInstance
            }

        }

    }

    //EXEMPLO DE MIGRATION CASO FOR NECESSARIO FAZER DEPOIS.
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Deletando as tabelas antigas
            database.execSQL("DROP TABLE IF EXISTS produto")
            database.execSQL("DROP TABLE IF EXISTS ordemServico")

            // Recriando a tabela 'produto'
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS produto (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nome TEXT NOT NULL,
                descricao TEXT NOT NULL,
                criadoEm INTEGER NOT NULL,  -- 'Date' armazenado como LONG (timestamp)
                acabamentoId INTEGER NOT NULL,
                equipamentoId INTEGER NOT NULL,
                FOREIGN KEY(acabamentoId) REFERENCES acabamento(id) ON DELETE CASCADE,
                FOREIGN KEY(equipamentoId) REFERENCES equipamento(id) ON DELETE CASCADE
            )
        """)

            // Recriando a tabela 'ordemServico'
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS ordemServico (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                largura REAL NOT NULL,
                altura REAL NOT NULL,
                valorM2 REAL NOT NULL,
                quantidade INTEGER NOT NULL,
                valorUnitario REAL NOT NULL,
                valorTotal REAL NOT NULL,
                valorDesconto REAL NOT NULL,
                valorAPagar REAL NOT NULL,
                observacoes TEXT NOT NULL,
                dataHorarioAbertura INTEGER NOT NULL,  -- 'Date' armazenado como LONG (timestamp)
                status INTEGER NOT NULL,  -- Usando o código do Status (ENUM)
                clienteId INTEGER NOT NULL,
                funcionarioId INTEGER NOT NULL,
                produtoId INTEGER NOT NULL,
                FOREIGN KEY(clienteId) REFERENCES cliente(id) ON DELETE CASCADE,
                FOREIGN KEY(funcionarioId) REFERENCES funcionario(id) ON DELETE CASCADE,
                FOREIGN KEY(produtoId) REFERENCES produto(id) ON DELETE CASCADE
            )
        """)
        }
    }


}