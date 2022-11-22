package com.glpi.glpi_ministerio_pblico.utilities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfo_DataBase
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class NFMbyPass : AppCompatActivity() {

    internal lateinit var dataModelArrayList: ArrayList<Data_Tickets>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfmby_pass)

        volleyRequestSortByTicketId(prefer.getTicketSortsId())


    }

    fun volleyRequestSortByTicketId(edtFindTicketID: String) {
        //metodo que nos devuelve los datos para los tickets
        Log.i("mensajeFlag", "${MainActivity.urlApi_SortByTicketId} == SortByTicketId")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_SortByTicketId, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    if (dataTicketsJson.toString()[2] == 'm') {
                        Toast.makeText(
                            this,
                            "Id de Ticket No Existe: " + dataTicketsJson.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        //requestVolleyTicketSorts()
                        //MainActivity.flag_edtFindTicketID = false
                    } else {
                        dataModelArrayList = ArrayList()
                        var ticketSortsStatus = ""
                        for (i in 0 until dataTicketsJson.length()) {

                            val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                            val ticketsModel = Data_Tickets()

                            getDataTicketsJson(dataTickets, ticketsModel)
                            ticketSortsStatus = dataTickets.getString("ESTADO")
                        }

                        Log.i("mensaje estado",ticketSortsStatus)
                        val intent = Intent(this, NavFooterTicketsActivity::class.java)
                        intent.putExtra("ticketSortsStatus",ticketSortsStatus)
                        startActivity(intent)
                        //setupRecycler()
                        //progressBarMyPetitions.isVisible = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "No Exite Id de Ticket Ingresado: $e",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = prefer.getToken()
                params["ticket_id"] = edtFindTicketID

                return params
            }
        }
        this?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }

        //FIN obtenemos perfil de usuario
    }

    private fun getDataTicketsJson(dataTickets: JSONObject, ticketsModel: Data_Tickets) {

        ticketsModel.ticketSortsId = dataTickets.getString("ID")
        ticketsModel.ticketSortsType = dataTickets.getString("TIPO")
        ticketsModel.ticketSortsDescription = dataTickets.getString("DESCRIPCION")
        ticketsModel.ticketSortsContent =
            MainActivity.decodeHtml(dataTickets.getString("CONTENIDO"))
        ticketsModel.ticketSortsStatus = dataTickets.getString("ESTADO")
        ticketsModel.ticketSortsDate = dataTickets.getString("FECHA")
        ticketsModel.ticketSortsModificationDate = dataTickets.getString("FECHA_MODIFICACION")
        ticketsModel.ticketSortsCloseDate = dataTickets.getString("FECHA_CIERRE")
        ticketsModel.ticketSortsCreationDate = dataTickets.getString("FECHA_CREACION")

        //obtenemos el id del operador, creador del ticket
        ticketsModel.ticketSortsIdRecipient = dataTickets.getString("ID_RECIPIENT")

        //obtenemos los datos del tecnico asigando al ticket
        val idPositionTechnician = dataTickets.getJSONObject("ID_TECHNICIAN")
        val dataTechnician = idPositionTechnician.getJSONObject("0")
        val technician = dataTechnician.getJSONObject("0")
        ticketsModel.ticketSortsIdTechnician = technician.getString("ID_USER")
        val idTechnician = technician.getString("ID_USER")
        ticketsModel.ticketSortsUserTechnician = technician.getString("USUARIO")
        ticketsModel.ticketSortsNameTechnician = technician.getString("NOMBRE")
        ticketsModel.ticketSortsLastNameTechnician = technician.getString("APELLIDO")
        ticketsModel.ticketSortsPhoneTechnician = technician.getString("TELEFONO")
        ticketsModel.ticketSortsPositionTechnician = technician.getString("CARGO")
        ticketsModel.ticketSortsEmailTechnician = technician.getString("CORREO")
        ticketsModel.ticketSortsLocationTechnician = technician.getString("UBICACION")
        ticketsModel.ticketSortsEntityTechnician = technician.getString("ENTIDAD")
        prefer.saveNameTechnicianTask("${technician.getString("NOMBRE")} ${technician.getString("APELLIDO")}")


        //obtenemos los datos del solicitante
        val idPositionResquester = dataTickets.getJSONObject("ID_REQUESTER")
        val dataRequester = idPositionResquester.getJSONObject("0")
        val requester = dataRequester.getJSONObject("0")
        ticketsModel.ticketSortsIdRequester = requester.getString("ID_USER")
        ticketsModel.ticketSortsUserRequester = requester.getString("USUARIO")
        ticketsModel.ticketSortsNameRequester = requester.getString("NOMBRE")
        ticketsModel.ticketSortsLastNameRequester = requester.getString("APELLIDO")
        ticketsModel.ticketSortsPhoneRequester = requester.getString("TELEFONO")
        ticketsModel.ticketSortsPositionRequester = requester.getString("CARGO")
        ticketsModel.ticketSortsEmailRequester = requester.getString("CORREO")
        ticketsModel.ticketSortsLocationRequester = requester.getString("UBICACION")
        ticketsModel.ticketSortsEntityRequester = requester.getString("ENTIDAD")

        //obtenemos datos del tecnico asignado al ticket
        ticketsModel.ticketSortsUser = dataTickets.getString("USUARIO")
        ticketsModel.ticketSortsNameUser = dataTickets.getString("NOMBRE")
        ticketsModel.ticketSortsLastNameUser = dataTickets.getString("APELLIDO")

        val completeName = "${dataTickets.getString("NOMBRE")} ${dataTickets.getString("APELLIDO")}"
        prefer.saveNameUser(completeName)

        /*MainActivity.userTechnician = dataTickets.getString("USUARIO")
        val technicianName = dataTickets.getString("NOMBRE")
        MainActivity.nameTechnician = technicianName
        val technicianLastName = dataTickets.getString("APELLIDO")
        MainActivity.lastNameTechnician = technicianLastName
        ticketsModel.setTechnicianName("$technicianName $technicianLastName")
        MainActivity.nameLoginUser = "$technicianName $technicianLastName"*/

        ticketsModel.ticketSortsCategory = dataTickets.getString("CATEGORIA")
        ticketsModel.ticketSortsSource = dataTickets.getString("ORIGEN")
        ticketsModel.ticketSortsUrgency = dataTickets.getString("URGENCIA")

        dataModelArrayList.add(ticketsModel)

        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            room.daoTicketInfo().insertTicketInfo(
                TicketInfo_DataBase(
                    0,
                    dataTickets.getString("ID"),
                    dataTickets.getString("TIPO"),
                    MainActivity.decodeHtml(dataTickets.getString("CONTENIDO")),
                    dataTickets.getString("ESTADO"),
                    dataTickets.getString("FECHA"),
                    dataTickets.getString("FECHA_MODIFICACION"),
                    dataTickets.getString("ID_RECIPIENT"),

                    idTechnician,
                    technician.getString("NOMBRE"),
                    technician.getString("APELLIDO"),
                    technician.getString("TELEFONO"),
                    technician.getString("CORREO"),

                    requester.getString("ID_USER"),
                    requester.getString("NOMBRE"),
                    requester.getString("APELLIDO"),
                    requester.getString("TELEFONO"),
                    requester.getString("CARGO"),
                    requester.getString("CORREO"),
                    requester.getString("UBICACION"),

                    dataTickets.getString("CATEGORIA"),
                    dataTickets.getString("ORIGEN"),
                    dataTickets.getString("URGENCIA")
                )
            )

            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (element in getTicketInfoDB) {
                Log.i(
                    "mensaje dbTicketInfo",
                    "$element\n"
                )
            }
        }
        prefer.saveTicketSortsId(dataTickets.getString("ID"))
        Log.i("mensaje SaveTicket",prefer.getTicketSortsId())
        prefer.saveRecipientId(dataTickets.getString("ID_RECIPIENT"))
        Log.i("mensaje SaveRequ",dataTickets.getString("ID_RECIPIENT"))
        prefer.saveTicketSortsStatus(dataTickets.getString("ESTADO"))
        Log.i("mensaje SaveState",dataTickets.getString("ESTADO"))
    }
}