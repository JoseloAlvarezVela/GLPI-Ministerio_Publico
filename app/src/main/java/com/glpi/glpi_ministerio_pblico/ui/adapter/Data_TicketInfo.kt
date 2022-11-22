package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TicketInfo {
    var ticketInfoType: String? = null
    var ticketInfoId: String? = null // id de la tarea ó seguimiento ó solución
    var ticketInfoPrivate: String? = null
    var ticketInfoIdUser: String? = null //id de usuario que esta logeado
    var ticketInfoUser: String? = null //usuario que esta logeado
    var ticketInfoNameUser: String? = null //nombre de usuario que esta logeado
    var ticketInfoLastNameUser: String? = null //apellido de usuario que esta logeado
    var ticketInfoDate: String? = null
    var ticketInfoCreationDate: String? = null
    var ticketInfoModificationDate: String? = null
    var ticketInfoContent: String? = null
    var ticketInfoIdSource: String? = null //id de origen del ticket: 2, telefono,.. SEGUMIENTO
    var ticketInfoSource: String? = null //id de origen del segumiento: correo electrónico, whatsapp, telefono,.. SEGUMIENTO
    var ticketInfoDateBegin: String? = null
    var ticketInfoDateEnd: String? = null
    var ticketInfoTimeToSolve: String? = null
    var ticketInfoStatus: String? = null //terminado o pendiente
    var ticketInfoIdTemplate: String? = null
    var ticketInfoIdCategory: String? = null
    var ticketInfoCategory: String? = null
    var ticketInfoIdEditor: String? = null //id del que esta editando la tarea
    var ticketInfoIdTechnician: String? = null //id del tecnico que esta asignado a la tarea

    var ticketInfoNameTechnician: String? = null
    var ticketInfoCompleteNameTechnician: String? = null

    var ticketInfoCompleteNameRequester: String? = null

    var ticketInfoCompleteNameRecipient: String? = null
    var ticketInfoTaskStatus: String? = null
}