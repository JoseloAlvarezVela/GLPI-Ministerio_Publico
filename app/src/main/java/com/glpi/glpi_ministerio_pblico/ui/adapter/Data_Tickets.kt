package com.glpi.glpi_ministerio_pblico.ui.adapter

/*1:
creamos una clase para la lista de tickets y declaramos los atributos que vamos a obtener*/
class Data_Tickets {
    var glpiID_: String? = null
    var GlpiSolReq_: String? = null
    var GlpiDescripcion_: String? = null
    var glpi_currenttime_: String? = null
    var glpiname_: String? = null

    /*inicializamos constructor
    fun Usuario(glpi_currenttime: String?, glpiID: String?,glpiname: String? ) {
        this.glpi_currenttime_ = glpi_currenttime
        this.glpiID_ = glpiID
        this.glpiname_ = glpiname
    }*/

    /*Estos métodos getter y setter nos ayudarán a mantener los datos mientras
    llenamos la vista Recycler en la clase Adapter.*/
    fun getGlpiID(): String {
        return glpiID_.toString()
    }
    fun setGlpiID(GlpiID: String) {
        this.glpiID_ = GlpiID
    }

    fun getGlpiSolReq(): String {
        return GlpiSolReq_.toString()
    }
    fun setGlpiSolReq(GlpiID: String) {
        this.GlpiSolReq_ = GlpiID
    }

    fun getGlpiDescripcion(): String{
        return  GlpiDescripcion_.toString()
    }
    fun setGlpiDescripcion(GlpiDescripcion: String){
        this.GlpiDescripcion_ = GlpiDescripcion
    }

    fun getCurrentTime(): String {
        return glpi_currenttime_.toString()
    }
    fun setCurrentTime(CurrentTime: String) {
        this.glpi_currenttime_ = CurrentTime
    }

    fun getGlpiName(): String {
        return glpiname_.toString()
    }
    fun setGlpiName(GlpiName: String) {
        this.glpiname_ = GlpiName
    }
}