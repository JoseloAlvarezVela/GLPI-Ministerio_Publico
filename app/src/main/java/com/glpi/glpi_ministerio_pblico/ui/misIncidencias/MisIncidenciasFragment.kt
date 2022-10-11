package com.glpi.glpi_ministerio_pblico.ui.misIncidencias

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TicketSortByIncident
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisIncidenciasBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [MisIncidenciasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisIncidenciasFragment : Fragment(), RecycleView_Adapter_Tickets.ontickteClickListener {
    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
       esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListIncident: ArrayList<Data_Tickets>
    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var recycleView_Adapter_TicketsIncidencias: RecycleView_Adapter_Tickets? = null

    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentMisIncidenciasBinding? = null
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMisIncidenciasBinding.inflate(inflater, container, false)


        volleyRequestSortByIncident(urlApi_TicketSortByIncident)
        Toast.makeText(context,"MisIncidenciasFragment", Toast.LENGTH_LONG).show()

        //return inflater.inflate(R.layout.fragment_mis_incidencias, container, false)
        val root = binding.root
        return root
    }

    private fun volleyRequestSortByIncident(urlApi_TicketSortByIncident: String) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Request.Method.POST,
            urlApi_TicketSortByIncident, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayListIncident = ArrayList()

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
                        val idTechnician = DataTickets.getJSONObject("ID_TECHNICIAN")
                        playerModel.setTicketSortsIdTechnician(idTechnician.getString("0"))
                        //Log.i("mensaje idTechnician",""+idTechnician.getString("0"))
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
                        val decoded2: Spanned = HtmlCompat.fromHtml(decoded, HtmlCompat.FROM_HTML_MODE_COMPACT)
                        playerModel.setTicketSortsContents(decoded2.toString())
                        //urgencia del ticket
                        playerModel.setTicketSortsUrgency(DataTickets.getString("URGENCIA"))
                        //categoria del ticket
                        playerModel.setTicketSortsCategory(DataTickets.getString("CATEGORIA"))
                        //origen del ticket
                        playerModel.setTicketSortsSource(DataTickets.getString("ORIGEN"))
                        dataModelArrayListIncident.add(playerModel)
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

    fun setupRecycler() {
        recyclerView = binding.recyclerViewMisIncidencias//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        recyclerView!!.layoutManager = layoutManager

        recycleView_Adapter_TicketsIncidencias =
            context?.let { RecycleView_Adapter_Tickets(it,dataModelArrayListIncident,this) }
        recyclerView!!.adapter = recycleView_Adapter_TicketsIncidencias
    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MisIncidenciasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MisIncidenciasFragment().apply {
                arguments = Bundle().apply {
                }
            }
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