package com.glpi.glpi_ministerio_pblico.ui.shared

import android.content.Context
import org.json.JSONObject

class Prefer(val context: Context) {

    val SHARED_DB = "Mydb"
    val SHARED_ID = "MyIDs"
    val SHARED_USER = "MyUSER"
    val SHARED_VALIDATE = "myTokenValidate"
    val TOKEN_DB = "noToken"
    val TICKET_ID = "noTicket"
    val RECIPIENT_ID = "noRecipiend"
    val USER_PERFIL = "noPERFIL"
    val USER_ID = "noUserID"

    //json data
    val JSON_ENTITES = "MyJson"
    val storareJson = context.getSharedPreferences(JSON_ENTITES,0)

    val storage = context.getSharedPreferences(SHARED_DB,0)
    val storageTicketSortsId = context.getSharedPreferences(SHARED_ID,0)
    val storageRecipientId = context.getSharedPreferences(SHARED_ID,0)
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

    //metodo que guarda el nombre del usuario
    /*fun setUserName(name: String){
        storage.edit().putString(USER_NAME,name)
    }*/


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