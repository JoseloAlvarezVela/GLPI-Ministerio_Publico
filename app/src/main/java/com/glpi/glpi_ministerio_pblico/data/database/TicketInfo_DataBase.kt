package com.glpi.glpi_ministerio_pblico.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//@Entity(tableName = "TicketInfo_DataBase")
@Entity
data class TicketInfo_DataBase (
    @PrimaryKey(autoGenerate = true)
    var ticket_ID:Int = 0,

    var ticketSortsID:String,

    var ticketSortsType:String,

    var ticketSortsContent:String,

    var ticketSortsStatus:String,
    var ticketSortsCreationDate: String,
    var ticketSortsModificationDate: String,
    var ticketSortsIdRecipient: String,
    var ticketSortsIdTechnician: String,
    var ticketSortsNameTechnician: String,
    var ticketSortsLastNameTechnician: String,
    var ticketSortsPhoneTechnician: String,
    var ticketSortsEmailTechnician: String,
    var ticketSortsIdRequester: String,
    var ticketSortsNameRequester: String,
    var ticketSortsLastNameRequester: String,
    var ticketSortsPhoneRequester: String,
    var ticketSortsPositionRequester: String,
    var ticketSortsEmailRequester: String,
    var ticketSortsLocationRequester: String,
    var ticketSortsCategory: String,
    var ticketSortsSource: String,
    var ticketSortsUrgency: String
)