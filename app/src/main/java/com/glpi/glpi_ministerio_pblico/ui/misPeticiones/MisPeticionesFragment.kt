package com.glpi.glpi_ministerio_pblico.ui.misPeticiones

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_Ticket
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TicketSorts
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisPeticionesBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TaskUsers
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import org.json.JSONArray
import org.json.JSONObject

class MisPeticionesFragment : Fragment(), RecycleView_Adapter_Tickets.ontickteClickListener {

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


        if (MainActivity.flag_edtFindTicketID){//si el EditText de Ticket por Id tiene contenido
            volleyRequestSortByTicketId(MainActivity.urlApi_SortByTicketId,MainActivity.edtFindTicketID)
        }else if (MainActivity.flag_requesterSearch) {
            requestVolleySortByRequester(MainActivity.urlApi_SortByRequester)
        }else{
            if (MainActivity.flagTicketSort){
                requestVolleyTicketSorts(urlApi_Ticket)
            }else if(MainActivity.flagFilterState){

                volleyRequestSortByStatus(
                    urlApi_TicketSorts,
                    MainActivity.checkNewTicket,
                    MainActivity.checkAssignedTicket,
                    MainActivity.checkPlannedTicket,
                    MainActivity.checkWaitTicket,
                    MainActivity.checkSolvedTicket,
                    MainActivity.checkCloseTicket)

            }else if (MainActivity.flagCalendar){
                if (MainActivity.flagCalendarUltModify){
                    Log.i("mensaje flag","${MainActivity.urlApi_SortByModDate} == SortByDate")
                    volleyRequestSortByDate(MainActivity.urlApi_SortByModDate)
                }else if (MainActivity.flagCalendarOpenDate){
                    Log.i("mensaje flag","${MainActivity.urlApi_SortByCreationDate} == SortByCreationDate")
                    volleyRequestSortByDate(MainActivity.urlApi_SortByCreationDate)
                }else if (MainActivity.flagCalendarCloseDate){
                    Log.i("mensaje flag","${MainActivity.urlApi_SortByCloseDate} == SortByCloseDate")
                    volleyRequestSortByDate(MainActivity.urlApi_SortByCloseDate)
                }

            }
        }
        val root = binding.root

        return root
    }

    private fun requestVolleySortByRequester(urlApi_SortByRequester: String){
        Log.i("mensajeFlag","${MainActivity.urlApi_SortByRequester} == SortByRequester")
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_SortByRequester, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()){

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets,ticketsModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    MainActivity.flagNotFound = "true"
                    if (MainActivity.flagNotFound == "true"){
                        binding.recycler.isVisible = false
                        binding.includeNotFound.notFount.isVisible = true
                    }
                    e.printStackTrace()
                    Toast.makeText(context, "no hay datos para filtro aplicado: $e", Toast.LENGTH_LONG).show()
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

    private fun volleyRequestSortByTicketId(urlApi_SortByTicketId: String, edtFindTicketID: String){
        //metodo que nos devuelve los datos para los tickets
        Log.i("mensajeFlag","${MainActivity.urlApi_SortByTicketId} == SortByTicketId")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_SortByTicketId, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    if (dataTicketsJson.toString()[2] == 'm'){
                        Toast.makeText(context, "Id de Ticket No Existe: "+dataTicketsJson.toString(), Toast.LENGTH_LONG).show()
                        requestVolleyTicketSorts(urlApi_Ticket)
                        MainActivity.flag_edtFindTicketID = false
                    }else{
                        dataModelArrayList = ArrayList()

                        for (i in 0 until dataTicketsJson.length()){

                            val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                            val ticketsModel = Data_Tickets()

                            getDataTicketsJson(dataTickets,ticketsModel)
                        }
                        setupRecycler()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "No Exite Id de Ticket Ingresado: $e", Toast.LENGTH_LONG).show()

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

    private fun requestVolleyTicketSorts(urlApi_TicketSort: String){
        Log.i("mensajeFlag","$urlApi_Ticket == GENERAL")
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Request.Method.POST,
            urlApi_TicketSort, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()){

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets,ticketsModel)
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

        ticketsModel.setTicketSortsID(dataTickets.getString("ID"))
        ticketsModel.setTicketSortsType(dataTickets.getString("TIPO"))
        ticketsModel.setTicketSortsState(dataTickets.getString("ESTADO"))
        ticketsModel.setTicketSortsDescription(dataTickets.getString("DESCRIPCION"))

        //obtenemos el id del operador, creador del ticket
        ticketsModel.setTicketSortsIdRecipient(dataTickets.getString("ID_RECIPIENT"))

        //obtenemos el id del tecnico(usuario logeado) y su nombre
        val idPositionTechnician = dataTickets.getJSONObject("ID_TECHNICIAN")
        val dataTechnician = idPositionTechnician.getJSONObject("0")
        val technician = dataTechnician.getJSONObject("0")
        val idTechnician = technician.getString("ID_USER")
        MainActivity.idUserTechnician = idTechnician
        ticketsModel.setTicketSortsIdTechnician(idTechnician)

        //obtenemos el id del solicitante
        val idPositionResquester = dataTickets.getJSONObject("ID_REQUESTER")
        val dataRequester = idPositionResquester.getJSONObject("0")
        val requester = dataRequester.getJSONObject("0")
        val idRequester = requester.getString("ID_USER")
        ticketsModel.setTicketSortsIdRequester(idRequester)
        val nameRequester = requester.getString("NOMBRE")
        ticketsModel.setTicketSortsNameRequester(nameRequester)
        val cargoRequester = requester.getString("CARGO")
        ticketsModel.setTicketSortsPositionRequester(cargoRequester)

        //obtenemos datos del tecnico asignado al ticket
        MainActivity.userTechnician = dataTickets.getString("USUARIO")
        val technicianName = dataTickets.getString("NOMBRE")
        MainActivity.nameTechnician = technicianName
        val technicianLastName = dataTickets.getString("APELLIDO")
        MainActivity.lastNameTechnician = technicianLastName
        ticketsModel.setTechnicianName("$technicianName $technicianLastName")
        MainActivity.nameLoginUser = "$technicianName $technicianLastName"

        ticketsModel.setTicketSortsCreationDate(dataTickets.getString("FECHA_CREACION"))//fecha de creación
        ticketsModel.setTicketSortsModificationDate(dataTickets.getString("FECHA_MODIFICACION"))//fecha de creación

        //obtenemos la descripción completa del ticket - primero decodificamos el formato html
        val decoded: String = Html.fromHtml(dataTickets.getString("CONTENIDO")).toString()
        val decoded2: Spanned = HtmlCompat.fromHtml(decoded,HtmlCompat.FROM_HTML_MODE_COMPACT)
        ticketsModel.setTicketSortsContents(decoded2.toString())

        ticketsModel.setTicketSortsUrgency(dataTickets.getString("URGENCIA"))
        ticketsModel.setTicketSortsCategory(dataTickets.getString("CATEGORIA"))
        ticketsModel.setTicketSortsSource(dataTickets.getString("ORIGEN"))

        dataModelArrayList.add(ticketsModel)
    }

    private fun requestVolleyByIdRequester(id: String){
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_TasksUsers+id, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONArray(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until JS_DataTickets.length()){

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
    ){
        Log.i("mensajeURL","$urlApi_TicketSorts == SortByStatus")
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Request.Method.POST,
            urlApi_TicketSort, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()){

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets,ticketsModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    MainActivity.flagNotFound = "true"
                    if (MainActivity.flagNotFound == "true"){
                        binding.recycler.isVisible = false
                        binding.includeNotFound.notFount.isVisible = true
                    }
                    e.printStackTrace()
                    Toast.makeText(context, "no hay datos para filtro aplicado: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                if (idStatus1 == "1"){
                    params["idStatus1"] = MainActivity.checkNewTicket
                    Log.i("mensaje idStatus1","${MainActivity.checkNewTicket}")
                }
                if (idStatus2 == "2"){
                    params["idStatus2"] = MainActivity.checkAssignedTicket
                    Log.i("mensaje idStatus2","${MainActivity.checkAssignedTicket}")
                }
                if (idStatus3 == "3"){
                    params["idStatus3"] = MainActivity.checkPlannedTicket
                    Log.i("mensaje idStatus3","${MainActivity.checkPlannedTicket}")
                }
                if (idStatus4 == "4"){
                    params["idStatus4"] = MainActivity.checkWaitTicket
                    Log.i("mensaje idStatus4","${MainActivity.checkWaitTicket}")
                }
                if (idStatus5 == "5"){
                    params["idStatus5"] = MainActivity.checkSolvedTicket
                    Log.i("mensaje idStatus5","${MainActivity.checkSolvedTicket}")
                }
                if (idStatus6 == "6"){
                    params["idStatus6"] = MainActivity.checkCloseTicket
                    Log.i("mensaje idStatus6","${MainActivity.checkCloseTicket}")
                }
                Log.i("mensaje idStatus6","$params")
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    private fun volleyRequestSortByDate(urlApi_Calendar: String){
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_Calendar, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until dataTicketsJson.length()){

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        getDataTicketsJson(dataTickets,ticketsModel)
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
                if (MainActivity.flagCalendarUltModify){
                    params["session_token"] = token.prefer.getToken()
                    params["mod_date_start"] = MainActivity.utlModifyStar
                    params["mod_date_end"] = MainActivity.ultModifyEnd
                }else if (MainActivity.flagCalendarOpenDate){
                    params["session_token"] = token.prefer.getToken()
                    params["opening_date_start"] = MainActivity.utlModifyStar
                    params["opening_date_end"] = MainActivity.ultModifyEnd
                }else if (MainActivity.flagCalendarCloseDate){
                    params["session_token"] = token.prefer.getToken()
                    params["close_date_start"] = MainActivity.utlModifyStar
                    params["close_date_end"] = MainActivity.ultModifyEnd
                }

                Log.i("mensaje fechaParam","${MainActivity.utlModifyStar} hasta ${MainActivity.ultModifyEnd}")

                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    fun setupRecycler() {
        recyclerView = binding.recycler//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        //dataModelArrayList.sortBy { it. }
        recyclerView!!.layoutManager = layoutManager

        recycleView_Adapter_Tickets =
            context?.let { RecycleView_Adapter_Tickets(it,dataModelArrayList,this) }
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
        TicketID: String, //TODO : pasar id y hacer nueva consulta a la API para evitar sobreCargar la vista
        NameOperador: Any,
        CurrentTime: Any,
        ModificationDate: String,
        IdRecipient: String,
        IdTechnician: String,
        IdRequester: String,
        Contenido: Any,

        //-----
        Tipo: String,
        creationDateTicket: String,
        Ubicacion: String,
        Correo: String,
        NameSolicitante: String,
        CargoSolicitante: String,
        TelefonoSolicitante: String,
        LoginName: String,
        TicketEstado: String,
        TicketCategoria: String,
        TicketOrigen: String,
        TicketUrgencia: String,
        taskName: String,
        taskDescription: String
    ) {
        val intent = Intent(context, NavFooterTicketsActivity::class.java)

        val bundle = Bundle()

        bundle.putString("TicketID", TicketID)
        bundle.putString("NameOperador", NameOperador.toString())
        bundle.putString("CurrentTime", CurrentTime.toString())
        bundle.putString("ModificationDate", ModificationDate)
        bundle.putString("IdRecipient",IdRecipient)
        bundle.putString("IdTechnician",IdTechnician)
        bundle.putString("IdRequester",IdRequester)
        bundle.putString("Contenido", Contenido.toString())
        //-----
        bundle.putString("Tipo", Tipo)
        bundle.putString("creationDateTicket", creationDateTicket)
        bundle.putString("Ubicacion", Ubicacion)
        bundle.putString("Correo", Correo)
        bundle.putString("NameSolicitante", NameSolicitante)
        bundle.putString("CargoSolicitante", CargoSolicitante)
        bundle.putString("TelefonoSolicitante", TelefonoSolicitante)
        bundle.putString("LoginName", LoginName)
        bundle.putString("TicketEstado", TicketEstado)
        bundle.putString("TicketCategoria", TicketCategoria)
        bundle.putString("TicketOrigen", TicketOrigen)
        bundle.putString("TicketUrgencia", TicketUrgencia)
        bundle.putString("taskName", taskName)
        bundle.putString("taskDescription", taskDescription)

        intent.putExtras(bundle)

        startActivity(intent)

    }

}