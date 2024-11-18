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

@Database(entities = [Acabamento::class, Cliente::class, Equipamento::class, Funcionario::class,
    OrdemServico::class, Produto::class], version = 3)
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
                )
                    .build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}