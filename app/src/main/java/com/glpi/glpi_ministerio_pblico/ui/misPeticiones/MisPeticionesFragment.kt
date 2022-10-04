package com.glpi.glpi_ministerio_pblico.ui.misPeticiones

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    //): View? {
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMisPeticionesBinding.inflate(inflater, container, false)

        var checkBoxNewTicket: Int
        var checkBoxAssignedTicket = 0
        var checkBoxPlannedTicket = 0
        var checkBoxWaitTicket = 0
        var checkBoxCloseTicket = 0
        if (MainActivity.checkBoxNewTicket){
            checkBoxNewTicket = 1
        }
        if (!MainActivity.flagFilter){
            Toast.makeText(context, "${MainActivity.checkNewTicket}," +
                    " ${MainActivity.checkAssignedTicket} "+
                    "${MainActivity.checkPlannedTicket}," +
                    "${MainActivity.checkWaitTicket}," +
                    "${MainActivity.checkCloseTicket}  ", Toast.LENGTH_SHORT).show()
            Log.i("mensajeFlag","$urlApi_Ticket == GENERAL")
            requestVolleyTicketSorts(urlApi_Ticket)
        }else{

            Toast.makeText(context, "${MainActivity.checkNewTicket}," +
                    " ${MainActivity.checkAssignedTicket}," +
                    "${MainActivity.checkPlannedTicket}," +
                    "${MainActivity.checkWaitTicket}," +
                    "${MainActivity.checkCloseTicket}  ", Toast.LENGTH_SHORT).show()
            Log.i("mensajeURL","$urlApi_TicketSorts == SortByStatus")
            requestVolleyTicketSorts(urlApi_TicketSorts)
        }


        

        val root = binding.root

        return root
    }

    private fun requestVolleyTicketSorts(urlApi_TicketSort: String){
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Request.Method.POST,
            urlApi_TicketSort, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()

                    for (i in 0 until JS_DataTickets.length()){

                        val DataTickets = JS_DataTickets.getJSONObject(i.toString())
                        //Log.i("mensaje tasks in",""+DataTickets)
                        val playerModel = Data_Tickets()

                        playerModel.setTicketSortsID(DataTickets.getString("ID"))
                        playerModel.setTicketSortsType(DataTickets.getString("TIPO"))
                        playerModel.setTicketSortsState(DataTickets.getString("ESTADO"))
                        playerModel.setTicketSortsDescription(DataTickets.getString("DESCRIPCION")) 
                        //obtenemos el id del operador, creador del ticket
                        playerModel.setTicketSortsIdRecipient(DataTickets.getString("ID_RECIPIENT"))
                        //obtenemos el id del tecnico(usuario logeado) y su nombre
                        playerModel.setTicketSortsIdTechnician(DataTickets.getString("ID_TECHNICIAN"))
                        val technicianName = DataTickets.getString("NOMBRE")
                        val technicianLastName = DataTickets.getString("APELLIDO")
                        playerModel.setTechnicianName("$technicianName $technicianLastName")
                        //obtenemos el id del solicitante(usuario logeado)
                        val idPositionResquester = DataTickets.getJSONObject("ID_REQUESTER")
                        val idRequester = idPositionResquester.getString("0")
                        playerModel.setTicketSortsIdRequester(idRequester)

                        playerModel.setTicketSortsCreationDate(DataTickets.getString("FECHA_CREACION"))//fecha de creación
                        playerModel.setTicketSortsModificationDate(DataTickets.getString("FECHA_MODIFICACION"))//fecha de creación

                        //obtenemos la descripción completa del ticket - primero decodificamos el formato html
                        val decoded: String = Html.fromHtml(DataTickets.getString("CONTENIDO")).toString()
                        val decoded2: Spanned = HtmlCompat.fromHtml(decoded,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        playerModel.setTicketSortsContents(decoded2.toString())
                        //urgencia del ticket
                        playerModel.setTicketSortsUrgency(DataTickets.getString("URGENCIA"))
                        //categoria del ticket
                        playerModel.setTicketSortsCategory(DataTickets.getString("CATEGORIA"))
                        //origen del ticket
                        playerModel.setTicketSortsSource(DataTickets.getString("ORIGEN"))
                        dataModelArrayList.add(playerModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["idStatus1"] = MainActivity.checkNewTicket
                params["idStatus2"] = MainActivity.checkAssignedTicket
                params["idStatus3"] = MainActivity.checkPlannedTicket
                params["idStatus4"] = MainActivity.checkWaitTicket
                params["idStatus5"] = "0"
                params["idStatus6"] = MainActivity.checkCloseTicket
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }
    
    private fun requestVolleySortByStatus(){
        Toast.makeText(context, "aplicar filtro con nuevo volley", Toast.LENGTH_SHORT).show()
    }

    /*private fun volleyRequestRequester(id: String){
        Log.i("mensajeurl: ", "" + urlApi_TasksUsers+id)
        //metodo que nos devuelve los datos del solicitante,operador ó tecnico
        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Request.Method.POST,
            urlApi_TasksUsers+id, Response.Listener { response ->
                try {
                    val jsonIdRequester = JSONArray(response) //obtenemos el objeto json
                    Log.i("mensajeId: ", "" + jsonIdRequester)

                    dataModelArrayList = ArrayList()

                    for (i in 0 until jsonIdRequester.length()){

                        val dataRequester = jsonIdRequester.getJSONObject(i)
                        val playerModel = Data_Tickets()

                        val requesterName = dataRequester.getString("NOMBRE")
                        val requesterLastName = dataRequester.getString("APELLIDO")
                        //playerModel.setTaskUserName("$requesterName $requesterLastName")
                        playerModel.setTaskUserPosition(dataRequester.getString("CARGO"))

                        dataModelArrayList.add(playerModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado: $e", Toast.LENGTH_LONG).show()
                    Log.i("mensaje recyclerid: ", "" + e)
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["order_type"] = "DESC"
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
    }*/

    fun setupRecycler() {
        recyclerView = binding.recycler//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        recyclerView!!.layoutManager = layoutManager

        recycleView_Adapter_Tickets =
            context?.let { RecycleView_Adapter_Tickets(it,dataModelArrayList,this) }
        recyclerView!!.adapter = recycleView_Adapter_Tickets
    }
    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }*/

    override fun onStart() {
        super.onStart()
        /*val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        ft.detach(this).attach(this).commit()*/

    }

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