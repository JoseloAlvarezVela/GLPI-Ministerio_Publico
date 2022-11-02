package com.glpi.glpi_ministerio_pblico.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TicketInfo_DataBase::class],
    version = 2
)
abstract class TicketInfoDB: RoomDatabase(){
    abstract fun daoTicketInfo(): TicketInfoDao
}