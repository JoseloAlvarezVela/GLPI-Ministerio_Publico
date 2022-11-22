package com.glpi.glpi_ministerio_pblico.ui.shared

import android.content.Context
import org.json.JSONObject

class Prefer(val context: Context) {

    val SHARED_DB = "Mydb"
    val SHARED_ID = "MyIDs"
    val SHARED_USER = "MyUSER"
    val SHARED_VALIDATE = "myTokenValidate"
    val TOKEN_DB = "noToken"
    //---
    val TICKET_ID = "noTicket"
    val TICKET_STATUS = "noTICKET_STATUS"
    val TICKET_NOTIFICATION = "noTICKET_NOTIFICATION"
    val TICKET_INFO = "noTICKET_INFO"
    val TICKET_LOCATION = "noTICKET_INFO"
    val TICKET_CONTENT = "noTICKET_INFO"
    val TICKET_SORTS_CONTENT = "noTICKET_SORT"
    val RECIPIENT_ID = "noRecipiend"

    //---
    val USER_PERFIL = "noPERFIL"
    val USER_ID = "noUserID"

    //json data
    val JSON_ENTITES = "MyJson"
    val storareJson = context.getSharedPreferences(JSON_ENTITES,0)

    val storage = context.getSharedPreferences(SHARED_DB,0)
    val storageTicketSortsId = context.getSharedPreferences(SHARED_ID,0)
    val storageRecipientId = context.getSharedPreferences(SHARED_ID,0)
    val storageNameTechnicianTask = context.getSharedPreferences(TICKET_STATUS,0)
    val storageNotificationTicketId = context.getSharedPreferences(TICKET_NOTIFICATION,0)
    val storageTicketInfo = context.getSharedPreferences(TICKET_INFO,0)
    val storageTicketSortStatus = context.getSharedPreferences(TICKET_SORTS_CONTENT,0)
    val storageNameUser = context.getSharedPreferences(SHARED_USER,0)




    //metodo que guarda el perfil del usuario
    fun saveNameUser(name: String){
        storageNameUser.edit().putString(USER_PERFIL, name).apply()
    }
    //metodo que recupera nombre del sharedPreference
    fun getNameUser(): String? {
        return storageNameUser.getString(USER_PERFIL,"noUserFound")
    }
    fun deleteNameUser(){
        storageNameUser.edit().clear().apply()
    }

    //PUSH NOTIFICATION
    fun saveNotificationTicketId(NotificationTicketId_: String){
        storageNotificationTicketId.edit().putString(TICKET_NOTIFICATION,NotificationTicketId_).apply()
    }
    fun getNotificationTicketId(): String{
        return storageNotificationTicketId.getString(TICKET_NOTIFICATION,"noTICKET_NOTIFICATION")!!
    }
    fun deleteNotificationTicketId(){
        storageNotificationTicketId.edit().clear().apply()
    }


    //guardamos ids
    fun saveTicketSortsId(ticketSortsId: String){
        storageTicketSortsId.edit().putString(TICKET_ID,ticketSortsId).apply()
    }
    fun getTicketSortsId(): String{
        return storageTicketSortsId.getString(TICKET_ID,"noTicket")!!
    }
    fun deleteTicketSortsId(){
        storageTicketSortsId.edit().clear().apply()
    }

    fun saveTicketSortsStatus(ticketSortsStatus: String){
        storageTicketSortStatus.edit().putString(TICKET_SORTS_CONTENT,ticketSortsStatus).apply()
    }
    fun getTicketSortsStatus(): String{
        return storageTicketSortStatus.getString(TICKET_SORTS_CONTENT,"noTicketSortsContent")!!
    }
    fun deleteTicketSortsStatus(){
        storageTicketSortStatus.edit().clear().apply()
    }

        //volleyRequestIdRecipient()
    fun saveRecipientId(RecipientId: String){
        storageRecipientId.edit().putString(RECIPIENT_ID,RecipientId).apply()
    }
    fun getRecipientId(): String{
        return storageRecipientId.getString(RECIPIENT_ID,"noRecipient")!!
    }
    fun deleteRecipientId(){
        storageRecipientId.edit().clear().apply()
    }

    //ticketInfo --
    fun saveNameTechnicianTask(idTechnician: String){
        storageNameTechnicianTask.edit().putString(TICKET_STATUS,idTechnician).apply()
    }
    fun getNameTechnicianTask(): String{
        return storageNameTechnicianTask.getString(TICKET_STATUS,"NameTechnicianTask")!!
    }
    fun deleteNameTechnicianTask(){
        storageNameTechnicianTask.edit().clear().apply()
    }
    //TICKETINFO
        //ubicación del solicitante
    fun saveTicketSortsLocationRequester(ticketSortsLocationRequester_: String){
        storageTicketInfo.edit().putString(TICKET_LOCATION,ticketSortsLocationRequester_).apply()
    }
    fun getTicketSortsLocationRequester(): String{
        return storageTicketInfo.getString(TICKET_LOCATION,"noTicketSortsLocationRequester_")!!
    }
    fun deleteTicketSortsLocationRequester(){
        storageTicketInfo.edit().clear().apply()
    }
        //descripcion completa del ticket



    //metodo que guarda toke de usuario
    fun SaveToken(name:String){
        //editamos el sharedPreference y le pasamos parametro
        storage.edit().putString(TOKEN_DB,name).apply()
    }
    //metodo que recupera toke del sharedPreference
    fun getToken():String{
        return storage.getString(TOKEN_DB,"noToken")!!
    }
    //metodo que borra all content del sharedPreference
    fun deleteToken(){
        storage.edit().clear().apply()
    }

    //metodo que recupera id del sharedPreference
    fun getUserID():String{
        return storage.getString(USER_ID,"noUser")!!
    }

    //json data
    fun setJsonEntities(json_content: String){
        storareJson.edit().putString(JSON_ENTITES,json_content).apply()
    }
    fun getJsonEntities(): String{
        return storage.getString(JSON_ENTITES,"noContent")!!
    }

}