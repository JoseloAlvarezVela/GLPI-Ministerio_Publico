package com.glpi.glpi_ministerio_pblico.ui.adapter

/*1:
creamos una clase para la lista de tickets y declaramos los atributos que vamos a obtener*/
class Data_Tickets {
    var glpiID_: String? = null
    var glpiTipo_ : String? = null
    var GlpiDescripcion_: String? = null
    var GlpiContenido_: String? = null
    var glpiEstado_ : String? = null
    var glpi_currenttime_: String? = null
    var GlpiUbicacionSolicitante_: String? = null
    var GlpiCorreSolicitante_: String? = null
    var glpiOperadorName_: String? = null
    var GlpiLoginName_: String? = null
    var GlpiCategoria_: String? = null
    var GlpiOrigen_: String? = null
    var GlpiUrgencia_: String? = null
    var glpiRequester_Name_: String? = null
    var glpiRequester_Cargo_: String? = null
    var GlpiTelefonoSolicitante_: String? = null


    /*Estos métodos getter y setter nos ayudarán a mantener los datos mientras
    llenamos la vista Recycler en la clase Adapter.*/
    fun getGlpiID(): String {
        return glpiID_.toString()
    }
    fun setGlpiID(GlpiID: String) {
        this.glpiID_ = GlpiID
    }

    fun getGlpiTipo(): String{
        return glpiTipo_.toString()
    }
    fun setGlpiTipo(GlpiTipo: String){
        this.glpiTipo_ = GlpiTipo
    }

    fun getGlpiDescripcion(): String{
        return  GlpiDescripcion_.toString()
    }
    fun setGlpiDescripcion(GlpiDescripcion: String){
        this.GlpiDescripcion_ = GlpiDescripcion
    }

    fun getGlpiContenido(): String{
        return GlpiContenido_.toString()
    }
    fun setGlpiContenido(GlpiContenido: String){
        this.GlpiContenido_ = GlpiContenido
    }

    fun getGlpiEstado(): String{
        return glpiEstado_.toString()
    }
    fun setGlpiEstado(glpiEstado: String){
        this.glpiEstado_ = glpiEstado
    }

    fun getCurrentTime(): String {
        return glpi_currenttime_.toString()
    }
    fun setCurrentTime(CurrentTime: String) {
        this.glpi_currenttime_ = CurrentTime
    }

    fun getGlpiOperadorName(): String {
        return glpiOperadorName_.toString()
    }
    fun setGlpiOperadorName(OperadorName: String) {
        this.glpiOperadorName_ = OperadorName
    }





    fun getGlpiRequesterName(): String{
        return glpiRequester_Name_.toString()
    }
    fun setGlpiRequestreName(glpiRequester_Name: String){
        this.glpiRequester_Name_ = glpiRequester_Name
    }

    fun getGlpiRequesterCargo(): String{
        return glpiRequester_Cargo_.toString()
    }
    fun setGlpiRequestreCargo(glpiRequester_Cargo: String){
        this.glpiRequester_Cargo_ = glpiRequester_Cargo
    }

    //datos para activity_tickets.xml
    fun getGlpiUbicacionSolicitante(): String {
        return GlpiUbicacionSolicitante_.toString()
    }
    fun setGlpiUbicacionSolicitante(GlpiUbicacionSolicitante: String) {
        this.GlpiUbicacionSolicitante_ = GlpiUbicacionSolicitante
    }

    fun getGlpiCorreoSolicitante(): String {
        return GlpiCorreSolicitante_.toString()
    }
    fun setGlpiCorreoSolicitante(GlpiCorreoSolicitante: String) {
        this.GlpiCorreSolicitante_ = GlpiCorreoSolicitante
    }

    fun getGlpiTelefonoSolicitante(): String {
        return GlpiTelefonoSolicitante_.toString()
    }
    fun setGlpiTelefonoSolicitante(GlpiTelefonoSolicitante: String) {
        this.GlpiTelefonoSolicitante_ = GlpiTelefonoSolicitante
    }

    fun getGlpiLoginName(): String{
        return GlpiLoginName_.toString()
    }
    fun setGlpiLoginName(GlpiApellidoLogin: String){
        this.GlpiLoginName_ = GlpiApellidoLogin
    }

    fun getGlpiCategoria(): String{
        return GlpiCategoria_.toString()
    }
    fun setGlpiCategoria(GlpiCategoria: String){
        this.GlpiCategoria_ = GlpiCategoria
    }

    fun getGlpiOrigen(): String{
        return GlpiOrigen_.toString()
    }

    fun setGlpiOrigen(GlpiOrigen: String){
        this.GlpiOrigen_ = GlpiOrigen
    }

    fun getGlpiUrgencia(): String{
        return GlpiUrgencia_.toString()
    }
    fun setGlpiUrgencia(GlpiUrgencia: String){
        this.GlpiUrgencia_ = GlpiUrgencia
    }
}