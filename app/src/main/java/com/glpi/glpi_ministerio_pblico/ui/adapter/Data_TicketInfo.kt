package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TicketInfo {
    private var ticketInfoType: String? = null
    var ticketInfoId: String? = null // id de la tarea ó seguimiento ó solución
    private var ticketInfoPrivate: String? = null
    private var ticketInfoIdUser: String? = null //id de usuario que esta logeado
    private var ticketInfoUser: String? = null
    private var ticketInfoName: String? = null
    private var ticketInfoLastName: String? = null
    private var ticketInfoDate: String? = null
    private var ticketInfoCreationDate: String? = null
    private var ticketInfoModificationDate: String? = null
    private var ticketInfoContent: String? = null
    private var ticketInfoIdSource: String? = null //id de origen del ticket: 2, telefono,..
    private var ticketInfoSource: String? = null //id de origen del ticket: correo electrónico, whatsapp, telefono,..
    private var ticketInfoDateBegin: String? = null
    private var ticketInfoDateEnd: String? = null
    private var ticketInfoTimeToSolve: String? = null
    private var ticketInfoStatus: String? = null //terminado o pendiente
    private var ticketInfoIdTemplate: String? = null
    private var ticketInfoIdCategory: String? = null
    private var ticketInfoIdEditor: String? = null //id del que esta editando la tarea
    private var ticketInfoIdTechnician: String? = null
}