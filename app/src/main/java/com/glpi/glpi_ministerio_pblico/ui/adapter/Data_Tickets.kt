package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.util.Log
import java.util.*

/*1:
creamos una clase para la lista de tickets y declaramos los atributos que vamos a obtener*/
class Data_Tickets {
    private var ticketSortsId: String? = null
    private var ticketSortsType : String? = null
    private var ticketSortsState : String? = null
    private var ticketSortsDescription: String? = null
    private var ticketSortsIdRecipient: String? = null
    private var ticketSortsIdTechnician: String? = null
    private var technicianName: String? = null
    private var ticketSortsIdRequester: String? = null
    private var ticketSortsCreationDate: String? = null
    private var ticketSortsContents: String? = null
    private var ticketSortsUrgency: String? = null
    private var ticketSortsCategory: String? = null
    private var ticketSortsSource: String? = null
    private var ticketSortsModificationDate: String? = null
    //--------------------------------------------------

    private var ticketSortsNameByIdRequester: String? = null

    private var taskUsersName: String? = null
    private var taskUsersPosition: String? = null //cargo
    private var taskUsersNameRequester: String? = null
    private var taskUsersEstimateDuration: String? = null
    private var taskUsersMillisToHours: String? = null




    var GlpiUbicacionSolicitante_: String? = null
    var GlpiCorreSolicitante_: String? = null
    var glpiOperadorName_: String? = null
    var GlpiLoginName_: String? = null




    var glpiRequester_Cargo_: String? = null
    var GlpiTelefonoSolicitante_: String? = null
    //SECTION TASKS
    var ticketInfoPrivate: String? = null
    var glpiTasksName_: String? = null
    var glpiTasksDescripcion_: String? = null
    var glpiTasksTipo_: String? = null
    var conversationCreation_: Date? = null



    /*Estos métodos getter y setter nos ayudarán a mantener los datos mientras
    llenamos la vista Recycler en la clase Adapter.*/
    fun getTicketSortsID(): String {
        return ticketSortsId.toString()
    }
    fun setTicketSortsID(ticketSortsId_: String) {
        this.ticketSortsId = ticketSortsId_
    }

    fun getTicketSortsType(): String{
        return ticketSortsType.toString()
    }
    fun setTicketSortsType(ticketSortsType_: String){
        this.ticketSortsType = ticketSortsType_
    }

    fun getTicketSortsState(): String{
        return ticketSortsState.toString()
    }
    fun setTicketSortsState(ticketSortsState_: String){
        this.ticketSortsState = ticketSortsState_
    }

    fun getTicketSortsDescription(): String{
        return  ticketSortsDescription.toString()
    }
    fun setTicketSortsDescription(ticketSortsDescription_: String){
        this.ticketSortsDescription = ticketSortsDescription_
    }

    fun getTicketSortsIdRecipient(): String{
        return ticketSortsIdRecipient.toString()
    }
    fun setTicketSortsIdRecipient(ticketSortsIdRecipient_: String){
        this.ticketSortsIdRecipient = ticketSortsIdRecipient_
    }

    fun getTicketSortsIdTechnician(): String{
        return ticketSortsIdTechnician.toString()
    }
    fun setTicketSortsIdTechnician(ticketSortsIdTechnician_: String){
        this.ticketSortsIdTechnician = ticketSortsIdTechnician_
    }

    fun getTechnicianName(): String{
        return technicianName.toString()
    }
    fun setTechnicianName(technicianName_: String){
        this.technicianName = technicianName_
    }

    fun getTicketSortsIdRequester(): String{
        return ticketSortsIdRequester.toString()
    }
    fun setTicketSortsIdRequester(TicketSorts_IdRequester_: String){
        this.ticketSortsIdRequester = TicketSorts_IdRequester_
    }

    fun getTicketSortsCreationDate(): String {
        return ticketSortsCreationDate.toString()
    }
    fun setTicketSortsCreationDate(ticketSortsCreationDate_: String) {
        this.ticketSortsCreationDate = ticketSortsCreationDate_
    }

    fun getTicketSortsContents(): String{
        return ticketSortsContents.toString()
    }
    fun setTicketSortsContents(ticketSortsContents_: String){
        this.ticketSortsContents = ticketSortsContents_
    }

    fun getTicketSortsUrgency(): String{
        return ticketSortsUrgency.toString()
    }
    fun setTicketSortsUrgency(ticketSortsUrgency_: String){
        this.ticketSortsUrgency = ticketSortsUrgency_
    }

    fun getTicketSortsCategory(): String{
        return ticketSortsCategory.toString()
    }
    fun setTicketSortsCategory(ticketSortsCategory_: String){
        this.ticketSortsCategory = ticketSortsCategory_
    }

    fun getTicketSortsSource(): String{
        return ticketSortsSource.toString()
    }

    fun setTicketSortsSource(ticketSortsSource_: String){
        this.ticketSortsSource = ticketSortsSource_
    }

    fun getTicketSortsModificationDate(): String{
        return ticketSortsModificationDate.toString()
    }
    fun setTicketSortsModificationDate(ticketSortsModificationDate_: String){
        this.ticketSortsModificationDate = ticketSortsModificationDate_
    }

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

    fun getConversationCreation(): Date? {
        return conversationCreation_
    }
    fun setConversationCreation(ConversationCreation: Date) {
        this.conversationCreation_ = ConversationCreation
    }
}