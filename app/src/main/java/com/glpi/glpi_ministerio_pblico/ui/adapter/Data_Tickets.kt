package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.util.Log
import java.util.*

/*1:
creamos una clase para la lista de tickets y declaramos los atributos que vamos a obtener*/
class Data_Tickets {
    var ticketSortsId: String? = null
    var ticketSortsType : String? = null
    var ticketSortsDescription: String? = null
    var ticketSortsContent: String? = null
    var ticketSortsStatus : String? = null
    var ticketSortsDate : String? = null
    var ticketSortsModificationDate: String? = null
    var ticketSortsCloseDate: String? = null
    var ticketSortsCreationDate: String? = null
    var ticketSortsIdRecipient: String? = null

    var ticketSortsIdTechnician: String? = null
    var ticketSortsUserTechnician: String? = null
    var ticketSortsNameTechnician: String? = null
    var ticketSortsLastNameTechnician: String? = null
    var ticketSortsPhoneTechnician: String? = null
    var ticketSortsPositionTechnician: String? = null
    var ticketSortsEmailTechnician: String? = null
    var ticketSortsLocationTechnician: String? = null
    var ticketSortsEntityTechnician: String? = null

    var ticketSortsIdRequester: String? = null
    var ticketSortsUserRequester: String? = null
    var ticketSortsNameRequester: String? = null
    var ticketSortsLastNameRequester: String? = null
    var ticketSortsPhoneRequester: String? = null
    var ticketSortsPositionRequester: String? = null
    var ticketSortsEmailRequester: String? = null
    var ticketSortsLocationRequester: String? = null
    var ticketSortsEntityRequester: String? = null

    var ticketSortsUser: String? = null //usuario login del tecnico a quien se le asignó el ticket
    var ticketSortsNameUser: String? = null //nombre login del tecnico a quien se le asignó el ticket
    var ticketSortsLastNameUser: String? = null //apellido login del tecnico a quien se le asignó el ticket
    var ticketSortsCategory: String? = null
    var ticketSortsSource: String? = null //origen del ticket
    var ticketSortsUrgency: String? = null










    //--------------------------------------------------

    private var ticketSortsNameByIdRequester: String? = null

    private var taskUsersName: String? = null
    private var taskUsersPosition: String? = null //cargo
    private var taskUsersNameRequester: String? = null
    private var taskUsersEstimateDuration: String? = null
    private var taskUsersMillisToHours: String? = null
    private var taskUsersEstatus: String? = null
    private var taskUsersCategory: String? = null




    var GlpiUbicacionSolicitante_: String? = null
    var GlpiCorreSolicitante_: String? = null
    var glpiOperadorName_: String? = null
    var GlpiLoginName_: String? = null




    var glpiRequester_Cargo_: String? = null
    var GlpiTelefonoSolicitante_: String? = null
    //SECTION TASKS
    var ticketInfoPrivate: String? = null
    var ticketTasksDate: String? = null
    var glpiTasksName_: String? = null
    var glpiTasksDescripcion_: String? = null
    var glpiTasksTipo_: String? = null
    var conversationOrderByDate: Date? = null
    var conversationCreation_: Date? = null




    //----------datos tecnico------------------------
    fun getTaskUserName(): String{
        return taskUsersName.toString()
    }
    fun setTaskUserName(taskUsersName_: String){
        this.taskUsersName = taskUsersName_
    }

    //----------datos solicitante------------------------
    fun getTicketSortsNameByIdRequester(): String{
        return  ticketSortsNameByIdRequester.toString()
    }
    fun setTicketSortsNameByIdRequester(ticketSortsNameByIdRequester_: String){
        this.ticketSortsNameByIdRequester = ticketSortsNameByIdRequester_
    }

    //----------datos solicitante------------------------
    fun getTaskUsersNameRequester(): String{
        return taskUsersNameRequester.toString()
    }
    fun setTaskUsersNameRequester(taskUsersNameRequester_: String){
        this.taskUsersNameRequester = taskUsersNameRequester_
    }
    /*fun getTaskUserPosition(): String{
        return taskUsersName.toString()
    }
    fun setTaskUserPosition(taskUserPosition_: String){
        this.taskUsersPosition = taskUserPosition_
    }*/
    //---------datos del ticket-------------------------
    fun getTaskUsersEstimateDuration(): String{
        return taskUsersEstimateDuration.toString()
    }
    fun setTaskUsersEstimateDuration(taskUsersEstimateDuration_ : String){
        this.taskUsersEstimateDuration = taskUsersEstimateDuration_
    }

    fun getTaskUsersMillisToHours(): String{
        return taskUsersMillisToHours.toString()
    }
    fun setTaskUsersMillisToHours(taskUsersMillisToHours_: String){
        this.taskUsersMillisToHours = taskUsersMillisToHours_
    }

    fun getTaskUsersStatus(): String{
        return taskUsersEstatus.toString()
    }
    fun setTaskUsersStatus(taskUsersEstatus_: String){
        this.taskUsersEstatus = taskUsersEstatus_
    }

    fun getTaskUsersCategory(): String{
        return taskUsersCategory.toString()
    }
    fun setTaskUsersCategory(taskUsersCategory_: String){
        this.taskUsersCategory = taskUsersCategory_
    }



//-----------------


    fun getGlpiOperadorName(): String {
        return glpiOperadorName_.toString()
    }
    fun setGlpiOperadorName(OperadorName: String) {
        this.glpiOperadorName_ = OperadorName
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


    //SECTION TASK
    fun getTicketInfo_Private(): String{
        return ticketInfoPrivate.toString()
    }
    fun setTicketInfo_Private(ticketInfoPrivate_: String){
        this.ticketInfoPrivate = ticketInfoPrivate_
    }

    fun getTicketTasksDates(): String{
        return ticketTasksDate.toString()
    }
    fun setTicketTasksDates(ticketTasksDate_: String){
        this.ticketTasksDate = ticketTasksDate_
    }

    fun getGlpiTasksName(): String{
        return glpiTasksName_.toString()
    }
    fun setGlpiTasktName(GlpiTasktName: String){
        this.glpiTasksName_ = GlpiTasktName
    }

    fun getGlpiTasksDescripcion(): String{
        return glpiTasksDescripcion_.toString()
    }
    fun setGlpiTasksDescripcion(GlpiTasksDescripcion: String){
        this.glpiTasksDescripcion_ = GlpiTasksDescripcion
    }

    fun getGlpiTasksTipo(): String{
        return glpiTasksTipo_.toString()
    }
    fun setGlpiTasksTipo(GlpiTasksTipo: String){
        this.glpiTasksTipo_ = GlpiTasksTipo
    }

    fun getConversationOrderByDates(): Date?{
        return conversationOrderByDate
    }
    fun setConversationOrderByDates(conversationOrderByDate_: Date){
        this.conversationOrderByDate = conversationOrderByDate_
    }

    fun getConversationCreation(): Date? {
        return conversationCreation_
    }
    fun setConversationCreation(ConversationCreation: Date) {
        this.conversationCreation_ = ConversationCreation
    }
}