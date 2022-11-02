package com.glpi.glpi_ministerio_pblico.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TicketInfoDao {

    @Query("SELECT * FROM TicketInfo_DataBase")
    suspend fun getTicketInfo():List<TicketInfo_DataBase>

    /*@Query("SELECT * FROM TicketInfo_DataBase WHERE ticketSortsID=:ticketSortsID")
    suspend fun getTicketInfoById(ticketSortsID: String): String*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketInfo(ticketSortsID: TicketInfo_DataBase)

    @Query("UPDATE TicketInfo_DataBase SET ticketSortsStatus=:ticketSortsStatus WHERE  ticketSortsID=:ticketSortsID")
    suspend fun updateTicketInfo(ticketSortsID: String, ticketSortsStatus: String)

    @Query("DELETE FROM TicketInfo_DataBase WHERE ticketSortsID=:ticketSortsID")
    suspend fun deleteTicketInfo(ticketSortsID:String)
}