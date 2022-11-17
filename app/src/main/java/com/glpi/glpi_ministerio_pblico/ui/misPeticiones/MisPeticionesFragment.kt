package com.glpi.glpi_ministerio_pblico.ui.misPeticiones

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_Ticket
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TicketSorts
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfo_DataBase
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisPeticionesBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TaskUsers
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MisPeticionesFragment : Fragment(), RecycleView_Adapter_Tickets.onTicketClickListener {

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayList: ArrayList<Data_Tickets>
    internal lateinit var dataModelArrayListRequester: ArrayList<Data_TaskUsers>

    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var recycleView_Adapter_Tickets: RecycleView_Adapter_Tickets? = null

    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentMisPeticionesBinding? = null

    lateinit var jsonTicketResponse: JSONArray

    lateinit private var progressBarMyPetitions: ProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMisPeticionesBinding.inflate(inflater, container, false)





        progressBarMyPetitions = binding.progressBarMisPeticiones
        //requestVolleyTokens()
        if (MainActivity.flag_edtFindTicketID) {//si el EditText de Ticket por Id tiene contenido
            volleyRequestSortByTicketId(
                MainActivity.urlApi_SortByTicketId,
                MainActivity.edtFindTicketID
            )
        } else if (MainActivity.flag_requesterSearch) {
            requestVolleySortByRequester(MainActivity.urlApi_SortByRequester)
        } else {
            if (MainActivity.flagTicketSort) {
                requestVolleyTicketSorts(urlApi_Ticket)
            } else if (MainActivity.flagFilterState) {

                volleyRequestSortByStatus(
                    urlApi_TicketSorts,
                    MainActivity.checkNewTicket,
                    MainActivity.checkAssignedTicket,
                    MainActivity.checkPlannedTicket,
                    MainActivity.checkWaitTicket,
                    MainActivity.checkSolvedTicket,
                    MainActivity.checkCloseTicket
                )

            } else if (MainActivity.flagCalendar) {
                if (MainActivity.flagCalendarUltModify) {
                    Log.i("mensaje flag", "${MainActivity.urlApi_SortByModDate} == SortByDate")
                    volleyRequestSortByDate(MainActivity.urlApi_SortByModDate)
                } else if (MainActivity.flagCalendarOpenDate) {
                    Log.i(
                        "mensaje flag",
                        "${MainActivity.urlApi_SortByCreationDate} == SortByCreationDate"
                    )
                    volleyRequestSortByDate(MainActivity.urlApi_SortByCreationDate)
                } else if (MainActivity.flagCalendarCloseDate) {
                    Log.i(
                        "mensaje flag",
                        "${MainActivity.urlApi_SortByCloseDate} == SortByCloseDate"
                    )
                    volleyRequestSortByDate(MainActivity.urlApi_SortByCloseDate)
                }

            }
        }
        val root = binding.root

        return root
    }
    private fun requestVolleyTokens() {
        val url = "http://181.176.145.174:8080/api/device_token"
        val idUser = "27164"
        val imei = "456978418412"
        val tokenFirebase = "fkTB4Y0mQmOdUXlraXSKNf:APA91bFJB6JufMEpC0vI9q8bLzh-xKe1wpuKh_WRSrBekfT_OEKJ8h_qp3IkBG6vXg7MmKU5EV2yUmU74WRelm87PLje7qVMttRztfCZS5jlgcnxUA1UQpbkiUHRVwwIT127NKPHVgLL"
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            url, Response.Listener { response ->
                try {
                    //val dataAddFollowup = JSONObject(response) //obtenemos el objeto json

                    Log.i("mensaje json","CORRECTO")

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado_-----------------: $e", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR -------------", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = java.util.HashMap()
                params["id_user"] = idUser
                params["token_firebase"] = tokenFirebase
                params["imei"] = imei

                Log.i("mensaje pAAam","$params")
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }


    private fun requestVolleySortByRequester(urlApi_SortByRequester: String) {
        Log.i("mensajeFlag", "${MainActivity.urlApi_SortByRequester} == SortByRequester")
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_SortByRequester, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()) {

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets, ticketsModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    MainActivity.flagNotFound = "true"
                    if (MainActivity.flagNotFound == "true") {
                        binding.recycler.isVisible = false
                        binding.includeNotFound.notFount.isVisible = true
                    }
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "no hay datos para filtro aplicado: $e",
                        Toast.LENGTH_LONG
                    ).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["requester_completename"] = MainActivity.requesterSearch.toString()
                params["requester_completename_type"] = "1"

                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    private fun volleyRequestSortByTicketId(
        urlApi_SortByTicketId: String,
        edtFindTicketID: String
    ) {
        //metodo que nos devuelve los datos para los tickets
        Log.i("mensajeFlag", "${MainActivity.urlApi_SortByTicketId} == SortByTicketId")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_SortByTicketId, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    if (dataTicketsJson.toString()[2] == 'm') {
                        Toast.makeText(
                            context,
                            "Id de Ticket No Existe: " + dataTicketsJson.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        requestVolleyTicketSorts(urlApi_Ticket)
                        MainActivity.flag_edtFindTicketID = false
                    } else {
                        dataModelArrayList = ArrayList()

                        for (i in 0 until dataTicketsJson.length()) {

                            val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                            val ticketsModel = Data_Tickets()

                            getDataTicketsJson(dataTickets, ticketsModel)
                        }
                        setupRecycler()
                        progressBarMyPetitions.isVisible = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "No Exite Id de Ticket Ingresado: $e",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["ticket_id"] = edtFindTicketID

                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    private fun requestVolleyTicketSorts(urlApi_TicketSort: String) {
        Log.i("mensajeFlag", "$urlApi_Ticket == GENERAL")
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Request.Method.POST,
            urlApi_TicketSort, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()) {

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets, ticketsModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    private fun getDataTicketsJson(dataTickets: JSONObject, ticketsModel: Data_Tickets) {

        ticketsModel.ticketSortsId = dataTickets.getString("ID")
        ticketsModel.ticketSortsType = dataTickets.getString("TIPO")
        ticketsModel.ticketSortsDescription = dataTickets.getString("DESCRIPCION")
        ticketsModel.ticketSortsContent = decodeHtml(dataTickets.getString("CONTENIDO"))
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
    }

    private fun requestVolleyByIdRequester(id: String) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_TasksUsers + id, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONArray(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until JS_DataTickets.length()) {

                        val DataTickets = JS_DataTickets.getJSONObject(i)
                        //Log.i("mensaje tasks in",""+DataTickets)
                        val playerModel = Data_Tickets()

                        playerModel.setTicketSortsNameByIdRequester(DataTickets.getString("NOMBRE"))
                        Log.i("mensaje nameRequester: ", "${DataTickets.getString("NOMBRE")}")
                        dataModelArrayList.add(playerModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()

                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }


    private fun volleyRequestSortByStatus(
        urlApi_TicketSort: String,
        idStatus1: String,
        idStatus2: String,
        idStatus3: String,
        idStatus4: String,
        idStatus5: String,
        idStatus6: String
    ) {
        Log.i("mensajeURL", "$urlApi_TicketSorts == SortByStatus")
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Request.Method.POST,
            urlApi_TicketSort, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()) {

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets, ticketsModel)
                    }
                    //setupRecycler()
                    setupRecyclerCloseTicket()

                } catch (e: Exception) {
                    MainActivity.flagNotFound = "true"
                    if (MainActivity.flagNotFound == "true") {
                        binding.recycler.isVisible = false
                        binding.includeNotFound.notFount.isVisible = true
                    }
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "no hay datos para filtro aplicado: $e",
                        Toast.LENGTH_LONG
                    ).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                if (idStatus1 == "1") {
                    params["idStatus1"] = MainActivity.checkNewTicket
                    Log.i("mensaje idStatus1", "${MainActivity.checkNewTicket}")
                }
                if (idStatus2 == "2") {
                    params["idStatus2"] = MainActivity.checkAssignedTicket
                    Log.i("mensaje idStatus2", "${MainActivity.checkAssignedTicket}")
                }
                if (idStatus3 == "3") {
                    params["idStatus3"] = MainActivity.checkPlannedTicket
                    Log.i("mensaje idStatus3", "${MainActivity.checkPlannedTicket}")
                }
                if (idStatus4 == "4") {
                    params["idStatus4"] = MainActivity.checkWaitTicket
                    Log.i("mensaje idStatus4", "${MainActivity.checkWaitTicket}")
                }
                if (idStatus5 == "5") {
                    params["idStatus5"] = MainActivity.checkSolvedTicket
                    Log.i("mensaje idStatus5", "${MainActivity.checkSolvedTicket}")
                }
                if (idStatus6 == "6") {
                    params["idStatus6"] = MainActivity.checkCloseTicket
                    Log.i("mensaje idStatus6", "${MainActivity.checkCloseTicket}")
                }
                Log.i("mensaje idStatus6", "$params")
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    private fun volleyRequestSortByDate(urlApi_Calendar: String) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_Calendar, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()) {

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets, ticketsModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                if (MainActivity.flagCalendarUltModify) {
                    params["session_token"] = token.prefer.getToken()
                    params["mod_date_start"] = MainActivity.utlModifyStar
                    params["mod_date_end"] = MainActivity.ultModifyEnd
                } else if (MainActivity.flagCalendarOpenDate) {
                    params["session_token"] = token.prefer.getToken()
                    params["opening_date_start"] = MainActivity.utlModifyStar
                    params["opening_date_end"] = MainActivity.ultModifyEnd
                } else if (MainActivity.flagCalendarCloseDate) {
                    params["session_token"] = token.prefer.getToken()
                    params["close_date_start"] = MainActivity.utlModifyStar
                    params["close_date_end"] = MainActivity.ultModifyEnd
                }

                Log.i(
                    "mensaje fechaParam",
                    "${MainActivity.utlModifyStar} hasta ${MainActivity.ultModifyEnd}"
                )

                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    fun setupRecyclerCloseTicket() {
        recyclerView = binding.recycler//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        //dataModelArrayList.sortBy { it.ticketSortsCloseDate }
        recyclerView!!.layoutManager = layoutManager
        recycleView_Adapter_Tickets?.notifyDataSetChanged()
        recycleView_Adapter_Tickets =
            context?.let { RecycleView_Adapter_Tickets(it, dataModelArrayList, this) }
        recyclerView!!.adapter = recycleView_Adapter_Tickets
    }

    fun setupRecycler() {
        recyclerView = binding.recycler//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        //dataModelArrayList.sortBy { it.ticketSortsModificationDate }
        recyclerView!!.layoutManager = layoutManager
        recycleView_Adapter_Tickets?.notifyDataSetChanged()
        recycleView_Adapter_Tickets =
            context?.let { RecycleView_Adapter_Tickets(it, dataModelArrayList, this) }
        recyclerView!!.adapter = recycleView_Adapter_Tickets
    }
    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }*/


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTicketClick(
        ticketSortsId: String,
        ticketSortsType: String,
        ticketSortsContent: String,
        ticketSortsStatus: String,
        ticketSortsCreationDate: String,
        ticketSortsModificationDate: String,
        ticketSortsIdRecipient: String,

        ticketSortsIdTechnician: String,
        ticketSortsNameTechnician: String,
        ticketSortsLastNameTechnician: String,
        ticketSortsPhoneTechnician: String,
        ticketSortsEmailTechnician: String,

        ticketSortsIdRequester: String,
        ticketSortsNameRequester: String,
        ticketSortsLastNameRequester: String,
        ticketSortsPhoneRequester: String,
        ticketSortsPositionRequester: String,
        ticketSortsEmailRequester: String,
        ticketSortsLocationRequester: String,

        ticketSortsCategory: String,
        ticketSortsSource: String,
        ticketSortsUrgency: String
    ) {
        val flagTicketClosed = false
        val room =
            Room.databaseBuilder(requireContext(), TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            room.daoTicketInfo().insertTicketInfo(
                TicketInfo_DataBase(
                    0,
                    ticketSortsId,
                    ticketSortsType,
                    ticketSortsContent,
                    ticketSortsStatus,
                    ticketSortsCreationDate,
                    ticketSortsModificationDate,
                    ticketSortsIdRecipient,

                    ticketSortsIdTechnician,
                    ticketSortsNameTechnician,
                    ticketSortsLastNameTechnician,
                    ticketSortsPhoneTechnician,
                    ticketSortsEmailTechnician,

                    ticketSortsIdRequester,
                    ticketSortsNameRequester,
                    ticketSortsLastNameRequester,
                    ticketSortsPhoneRequester,
                    ticketSortsPositionRequester,
                    ticketSortsEmailRequester,
                    ticketSortsLocationRequester,

                    ticketSortsCategory,
                    ticketSortsSource,
                    ticketSortsUrgency
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
        prefer.saveTicketSortsId(ticketSortsId)
        prefer.saveRecipientId(ticketSortsIdRecipient)
        prefer.saveTicketSortsStatus(ticketSortsStatus)
        val intent = Intent(context, NavFooterTicketsActivity::class.java)
        Log.i("mensaje status",ticketSortsStatus)
        intent.putExtra("ticketSortsStatus",ticketSortsStatus)
        startActivity(intent)
    }
}